package spring.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.app.exceptions.NoNumbersEnteredException;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringParser {
    private final static Logger log = LoggerFactory.getLogger(StringParser.class);

    private static Pattern numeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return numeric.matcher(strNum).matches();
    }

    public static String[] toWordsArray(String text) {
        // разделение идем по пробелам, переносам и знакам пунктуации
        return text.trim().toLowerCase().split("[^a-яА-ЯйЙёЁa-zA-Z0-9/]+");
    }

    public static Set<Integer> toNumbersSet(String text) throws NoNumbersEnteredException {
        // разделение идем по пробелам, переносам и знакам пунктуации и буквам.
        // конвертируем сразу в сет интов
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
}
