package spring.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.app.core.BotContext;
import spring.app.exceptions.NoNumbersEnteredException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Утилитный класс для обработки ввода для получения тех или иных данных
 */
public class StringParser {
    private final static Logger log = LoggerFactory.getLogger(StringParser.class);

    private static Pattern numeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * Проверка что введены только числовые данные. На данный момент метод не используется, вместо него есть {@link #toNumbersSet(String)}.
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return numeric.matcher(strNum).matches();
    }

    /**
     * Разделение текста в массив строк по пробелам, переносам и знакам пунктуации (кроме /).
     * Удобно для считывания команд.
     */
    public static String[] toWordsArray(String text) {
        return text.trim().toLowerCase().split("[^a-яА-ЯйЙёЁa-zA-Z0-9/]+");
    }


    /**
     * Из текста удаляется все, кроме чисел, которые помещаются с сэт.
     * Используется, например, для парсинга нескольких vkId для удаления из БД.
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
            log.debug("Введенные данные не содержат чисел: {}", text);
            throw new NoNumbersEnteredException("Введенные данные не содержат чисел.");
        }
        return integerSet;
    }

    /**
     * Этот метод необходим для парсинга vk_Id или onscreen_name из приведенных ссылок
     * для поиска информации о них в ВК перед добавлением в БД.
     * @param text
     * @return List<String>, который сразу может быть передан в метод запроса информации из ВК о данных юзерах.
     */
    public static List<String> toVkIdsList(String text) {
        String[] words = text.trim().toLowerCase().split("[\\s,]+");
        List<String> result = new ArrayList<>();
        for (String link: words) {
            link = link.replaceAll("https://vk.com/id", "");
            link = link.replaceAll("https://vk.com/", "");
            result.add(link);
        }
        return result;
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

}
