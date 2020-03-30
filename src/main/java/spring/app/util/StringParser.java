package spring.app.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class StringParser {

    private static Pattern numeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return numeric.matcher(strNum).matches();
    }

    // возращает null, если строка не является датой в корректном формате
    // возаращает LocalDateTime если является датой в корректном формате
    // корректный формат - это ДД.ММ.ГГГГ ЧЧ:ММ

    public static LocalDateTime stringToLocalDateTime(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(strDate, formatter);
        } catch (DateTimeParseException ignored) {}
        return dateTime;
    }
}

