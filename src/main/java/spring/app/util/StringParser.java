package spring.app.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringParser {

    private static Pattern numeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return numeric.matcher(strNum).matches();
    }

    public static String[] toWordsArray(String text) {
        // разделение идем по пробелам, переносам и знакам пунктуации
        return text.trim().split("[^a-яА-ЯйЙёЁa-zA-Z0-9/]+");
    }

    public static Set<Integer> toNumbersSet(String text) {
        // разделение идем по пробелам, переносам и знакам пунктуации и буквам.
        // конвертируем сразу в сет интов
        return Arrays.stream(text.split("[^0-9]+"))
                .filter(string -> !string.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
