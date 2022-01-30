package jPlus.util.lang;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public final class StringUtils {

    public static boolean isNullWhiteSpaceOrEmpty(String... strings) {
        boolean anyAreNull = false;
        for (String s : strings) anyAreNull = s == null || s.isEmpty() || s.trim().isEmpty();
        return anyAreNull;
    }

    public static boolean hasContent(String... strings) {
        return !isNullWhiteSpaceOrEmpty(strings);
    }

    public static String capitalizeAllWords(String s) {
        return Arrays.stream(s.toLowerCase().split("\\s+"))
                .map(t -> t.substring(0, 1).toUpperCase() + t.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static String lowerCaseFirstLetter(String str) {
        if (str == null || str.length() == 0) return "";
        if (str.length() == 1) return str.toLowerCase();

        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static int maxLength(String[] strings) {
        if (strings.length == 0) return 0;
        int max = strings[0].length();
        for (int i = 1; i < strings.length; i++) max = Math.max(max, strings[i].length());
        return max;
    }

    public static int maxLength(Collection<String> strings) {
        final Iterator<String> iter = strings.iterator();
        int max = 0;
        while (iter.hasNext()) max = Math.max(max, iter.next().length());
        return max;
    }

    public static String repeat(String s, int count) {
        return new String(new char[count]).replace("\0", s);
    }

    public static String repeat(Character c, int count) {
        return new String(new char[count]).replace("\0", c.toString());
    }

    //***************************************************************//

    private static final String FORMAT_ARG_1 = "%1$";

    public static String addWhiteSpaceL(String str, int count) {
        return String.format(FORMAT_ARG_1 + '+' + (count + str.length()) + "s", str);
    }

    public static String addWhiteSpaceR(String str, int count) {
        return String.format(FORMAT_ARG_1 + '-' + (count + str.length()) + "s", str);
    }
}