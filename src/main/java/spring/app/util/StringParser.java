package spring.app.util;

import java.util.regex.Pattern;

public class StringParser {

    private static Pattern numeric = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return numeric.matcher(strNum).matches();
    }
}
