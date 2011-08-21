package util;

public class StringUtils {

    public static String humanize(String camelCase) {
        char[] chars = camelCase.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(toUpper(chars[0]));
        for (int i = 1; i < chars.length; i++) {
            if (isUpperCase(chars[i])) {
                sb.append(' ');
            }
            sb.append(chars[i]);
        }
        return sb.toString();
    }

    public static boolean isLowerCase(char c) {
        return c >= 'a' && c <= 'z' ? true : false;
    }

    public static boolean isUpperCase(char c) {
        return c >= 'A' && c <= 'Z' ? true : false;
    }

    public static char toUpper(char c) {
        return isLowerCase(c) ? (char) (c - 0x20) : c;
    }

}
