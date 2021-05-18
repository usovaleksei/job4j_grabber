import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SqlRuDateTimeParser implements DateTimeParser {

    private final Map<String, Integer> months = new HashMap<>();

    public SqlRuDateTimeParser() {
        months.put("янв", 1);
        months.put("фев", 2);
        months.put("мар", 3);
        months.put("апр", 4);
        months.put("май", 5);
        months.put("июн", 6);
        months.put("июл", 7);
        months.put("авг", 8);
        months.put("сен", 9);
        months.put("окт", 10);
        months.put("ноя", 11);
        months.put("дек", 12);
    }

    private int getMonth(String month) {
        return this.months.get(month);
    }

    @Override
    public LocalDateTime parse(String parse) {

        String[] dateTime = parse.split(", ");

        if (dateTime.length != 2) {
            throw new IllegalArgumentException("Wrong date format");
        }

        String date = dateTime[0];
        String time = dateTime[1];
        LocalTime localTime = LocalTime.parse(time);

        if (date.equals("сегодня")) {
            return LocalDateTime.of(LocalDate.now(), localTime);
        }
        if (date.equals("вчера")) {
            return LocalDateTime.of(LocalDate.now().minusDays(1), localTime);
        }

        String[] dayMonthYear = date.split(" ");
        if (dayMonthYear.length != 3) {
            throw new IllegalArgumentException("Wrong date format");
        }
        String dateFormat = dayMonthYear[0] + " " + getMonth(dayMonthYear[1]) + " " + dayMonthYear[2];

        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("d M yy");
        LocalDate localDate = LocalDate.parse(dateFormat, dTF);

        return LocalDateTime.of(localDate, localTime);
    }
}
