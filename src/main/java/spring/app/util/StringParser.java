package spring.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.app.core.BotContext;
import spring.app.exceptions.NoDataEnteredException;
import spring.app.exceptions.NoNumbersEnteredException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Утилитный класс для обработки ввода для получения тех или иных данных
 */
public class StringParser {
    private final static Logger log = LoggerFactory.getLogger(StringParser.class);

    private static Pattern numeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    //обязательно первая цифра от 0 до 9, далее только цифры с + или без него, разделенные пробелом, max 3 цифры
    private static Pattern validReviewerInputFormat = Pattern.compile("^\\d\\+?\\s?\\d?\\+?\\s?\\d?\\+?");

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
            log.debug("Введенные данные не содержат чисел: {}", text);
            throw new NoNumbersEnteredException("Введенные данные не содержат чисел.");
        }
        return integerSet;
    }

    /**
     * Этот метод необходим для парсинга vk_Id или onscreen_name из приведенных ссылок
     * для поиска информации о них в ВК перед добавлением в БД.
     *
     * @param text
     * @return List<String>, который сразу может быть передан в метод запроса информации из ВК о данных юзерах.
     */
    //TODO: убрать при рефакторинге
//    public static List<String> toVkIdsList(String text) {
//        String[] words = text.trim().toLowerCase().split("[\\s,]+");
//        List<String> result = new ArrayList<>();
//        for (String link : words) {
//            link = link.replaceAll("https://vk.com/id", "");
//            link = link.replaceAll("https://vk.com/", "");
//            result.add(link);
//        }
//        return result;
//    }

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

    public static boolean isValidReviewerInput(String input, int numberOfStudents) {
        if (validReviewerInputFormat.matcher(input).matches()) {
            Set<Integer> uniqueNumbers = Arrays.stream(input.split("[^0-9]+"))
                    .filter(string -> !string.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet()); // множество уникальных чисел в input
            int numbersWithPluses = input.split(" ").length; // кол-во чисел в input со знаком + или без него
            int plusesLength = input.replaceAll("[1-9 ]", "").length(); // кол-во плюсов в input
            for (Integer integer : uniqueNumbers) {
                if (integer < 1 || integer > numberOfStudents) {
                    return false;
                }
            }
            if (plusesLength <= 1 && numbersWithPluses >= 1 && numbersWithPluses <= numberOfStudents && uniqueNumbers.size() == numbersWithPluses) {
                if (numbersWithPluses < numberOfStudents && plusesLength == 1 || numberOfStudents == 1 || numbersWithPluses == numberOfStudents) {
                    if (plusesLength == 0 || input.lastIndexOf("+") == input.length() - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
