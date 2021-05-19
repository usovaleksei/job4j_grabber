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

    public static void main(String[] args) throws Exception {
        Properties config = getProperties("rabbit.properties");
        try (Connection connection = AlertRabbit.getConnection(config)) {
            int interval = AlertRabbit.getInterval(config);
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        }
    }

    private static int getInterval(Properties config) {
        return Integer.parseInt(config.getProperty("rabbit.interval"));
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

    private static Properties getProperties(String path) throws IOException {
        ClassLoader loader = AlertRabbit.class.getClassLoader();
        try (InputStream io = loader.getResourceAsStream(path)) {
            Properties config = new Properties();
            config.load(io);
            return config;
        }
    }

    private static Connection getConnection(Properties config) throws SQLException, ClassNotFoundException {
        Class.forName(config.getProperty("jdbc.driver"));
        String url = config.getProperty("jdbc.url");
        String login = config.getProperty("jdbc.login");
        String password = config.getProperty("jdbc.password");
        return DriverManager.getConnection(url, login, password);
    }
}
