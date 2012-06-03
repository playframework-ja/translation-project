package helper;

public class StringUtils {

    public static String humanize(String camelCase) {
        char[] chars = camelCase.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(chars[0]));
        for (int i = 1; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])) {
                sb.append(' ');
            }
            sb.append(chars[i]);
        }
        return sb.toString();
    }
}
