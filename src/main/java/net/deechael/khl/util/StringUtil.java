package net.deechael.khl.util;

public final class StringUtil {

    public static String safePattern(String string) {
        String[] keywords = new String[]{"*", ".", "?", "+", "$", "^", "[", "]", "(", ")", "{", "}", "|", "\"", "/"};
        for (String keyword : keywords) {
            string = string.replace(keyword, "\\" + keyword);
        }
        return string;
    }

    public static String unsafePattern(String string) {
        String[] keywords = new String[]{"*", ".", "?", "+", "$", "^", "[", "]", "(", ")", "{", "}", "|", "\"", "/"};
        for (String keyword : keywords) {
            string = string.replace("\\" + keyword, keyword);
        }
        return string;
    }

    private StringUtil() {
    }

}
