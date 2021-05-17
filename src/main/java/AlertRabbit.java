import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    public static void main(String[] args) {
        try {
            Connection connection = AlertRabbit.getConnection();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(AlertRabbit.getInterval())
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException | IOException | SQLException se) {
            se.printStackTrace();
        }
    }

    private static int getInterval() {
        Properties pr = new Properties();
        try (InputStream io = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            pr.load(io);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(pr.getProperty("rabbit.interval"));
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try (PreparedStatement ps = connection.prepareStatement("insert into rabbit(created_data) values(?)")) {
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                ps.execute();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    private static Connection getConnection() throws SQLException, IOException {
        ClassLoader loader = AlertRabbit.class.getClassLoader();
        try (InputStream io = loader.getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(io);
            String url = config.getProperty("jdbc.url");
            String login = config.getProperty("jdbc.login");
            String password = config.getProperty("jdbc.password");
            return DriverManager.getConnection(url, login, password);
        }
    }
}
