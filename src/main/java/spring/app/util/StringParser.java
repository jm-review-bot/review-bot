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

    /**
     * Метод преобразует строковое представление даты в формате dd.MM.uuuu HH:mm
     * в LocalDateTime
     * @param strDate
     *        строковое представление даты в формате dd.MM.uuuu HH:mm
     * @return
     *        null - если strDate не является строковым представлением даты в ожидаемом формате
     *        LocalDateTime - если strDate является строковым представлением даты в ожидаемом формате
     */

    public static LocalDateTime stringToLocalDateTime(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(strDate, formatter);
        } catch (DateTimeParseException ignored) {}
        return dateTime;
    }

    /**
     * Метод преобразует LocalDateTime в строку с датой в формате dd.MM.uuuu HH:mm
     * @param localDateTime
     *        дата и время
     * @return Строка с датой в формате dd.MM.uuuu HH:mm
     */

    public static String LocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
        return localDateTime.format(formatter);
    }
}

