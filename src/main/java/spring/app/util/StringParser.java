package spring.app.util;

import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Утилитный класс для обработки ввода для получения тех или иных данных
 */
public class StringParser {
    private static final Pattern numeric = Pattern.compile("\\d*");

    private static final Pattern realNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    //обязательно первая цифра от 0 до 9, далее только цифры с + или без него, разделенные пробелом, max 3 цифры
    private static final Pattern validReviewerInputFormat = Pattern.compile("^\\d\\+?\\s?\\d?\\+?\\s?\\d?\\+?");

    /**
     * Проверка что введены только числовые данные. На данный момент метод не используется, вместо него есть {@link #toNumbersSet(String)}.
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return numeric.matcher(strNum).matches();
    }

    public static boolean isRealNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return realNumeric.matcher(strNum).matches();
    }

    /**
     * Разделение текста в массив строк по пробелам, переносам и знакам пунктуации (кроме /).
     * Удобно для считывания команд.
     */
    public static String[] toWordsArray(String text) {
        String[] strArray = text.trim().toLowerCase().split("[^a-яА-ЯйЙёЁa-zA-Z0-9/]+");
        if (strArray.length == 0) {
            strArray = new String[]{""};
        }
        return strArray;
    }


    /**
     * Из текста удаляется все, кроме чисел, которые помещаются с сэт.
     * Используется, например, для парсинга нескольких vkId для удаления из БД.
     *
     * @param text
     * @return
     * @throws NoNumbersEnteredException - его обработка идет в {@link spring.app.core.steps.AdminRemoveUser#processInput(BotContext)}
     */
    public static Set<Integer> toNumbersSet(String text) throws NoNumbersEnteredException {
        Set<Integer> integerSet = Arrays.stream(text.split("[^0-9]+"))
                .filter(string -> !string.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        if (integerSet.isEmpty()) {
            throw new NoNumbersEnteredException("Введенные данные не содержат чисел.");
        }
        return integerSet;
    }
    
    /**
     * Отсекает доменную часть ссылки на профиль юзера
     * @param text ссылку на профиль
     * @return null, если строка неподходящего формата
     */
    public static String toVkId(String text) {
        if (text != null && !text.isEmpty()) {
            text = text.trim();
            if (text.startsWith("https://vk.com/")) {
                text = text.replaceAll("https://vk.com/id", "");
                text = text.replaceAll("https://vk.com/", "");
                return text;
            }
        }
        return null;
    }


    /**
     * Метод преобразует строковое представление даты в формате dd.MM.uuuu HH:mm
     * в LocalDateTime
     *
     * @param strDate строковое представление даты в формате dd.MM.uuuu HH:mm
     * @return LocalDateTime - если strDate является строковым представлением даты в ожидаемом формате
     * @throws NoDataEnteredException если strDate не является строковым представлением даты в ожидаемом формате
     */

    public static LocalDateTime stringToLocalDateTime(String strDate) throws NoDataEnteredException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(strDate, formatter);
        } catch (DateTimeParseException e) {
            throw new NoDataEnteredException("\"Некорректный ввод данных...\\n\\n Пример корректного ответа 02.06.2020 17:30\"");
        }
        return dateTime;
    }

    /**
     * Метод преобразует LocalDateTime в строку с датой в формате dd.MM.uuuu HH:mm
     *
     * @param localDateTime дата и время
     * @return Строка с датой в формате dd.MM.uuuu HH:mm
     */

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
        return localDateTime.format(formatter);
    }

    /**
     * Метод проверяет является ли переданная строка ссылкой на разговор в Google Hangouts
     */

    public static boolean isHangoutsLink(String link) {
        String prefix = "https://hangouts.google.com/call/";
        return link.startsWith(prefix);
    }
}
