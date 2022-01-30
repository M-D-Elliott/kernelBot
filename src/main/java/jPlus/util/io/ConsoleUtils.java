package jPlus.util.io;

import jPlus.lang.SystemUtils;
import jPlus.lang.callback.Retrievable2;
import jPlus.util.lang.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class ConsoleUtils {

    public static String sep() {
        return System.lineSeparator();
    }

    public static String sep(int repeat) {
        final String sep = sep();
        final StringBuilder ret = new StringBuilder();
        while (repeat-- > 0) ret.append(sep);
        return ret.toString();
    }

    public static String encase(String s, char edge) {
        return edge + s + edge;
    }

    //***************************************************************//

    public static final char STANDARD_BANNER_COMP = '*';
    private static final String BANNER = "%1$s" + sep() + "%2$s" + "%1$s";

    public static String encaseInBanner(String s) {
        return encaseInBanner(s, STANDARD_BANNER_COMP);
    }

    public static String encaseInBanner(String str, char bannerComp) {
        return encaseInBanner(str.split(sep()), bannerComp, (s, i) -> s);
    }

    public static String encaseInBanner(String[] arr, char bannerComp) {
        return encaseInBanner(arr, bannerComp, (s, i) -> s);
    }


    public static String encaseInBanner(Collection<String> lines, char bannerComp, Retrievable2<String, String, Integer> itemFormatter) {
        return encaseInBanner(lines.toArray(new String[0]), bannerComp, itemFormatter);
    }

    public static String encaseInBanner(String[] lines, char bannerComp, Retrievable2<String, String, Integer> itemFormatter) {
        final String lineFormat = encase("%1$s", bannerComp) + sep();
        final StringBuilder formattedLines = new StringBuilder();

        final int maxLength = StringUtils.maxLength(lines);
        int maxLengthF = maxLength;
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];
            final int length = line.length();

            final String formattedLine = String.format(lineFormat,
                    StringUtils.addWhiteSpaceR(itemFormatter.retrieve(line, i),
                            maxLength - length));
            maxLengthF = Math.max(maxLengthF, formattedLine.length());

            formattedLines.append(formattedLine);
        }
        final String border = StringUtils.repeat(bannerComp, maxLengthF - 2);

        return String.format(BANNER, border, formattedLines.toString());
    }

    public static String banner(int bannerCount) {
        return banner(STANDARD_BANNER_COMP, bannerCount);
    }

    public static String banner(char bannerComp, int bannerCount) {
        final char[] bannerC = new char[bannerCount];
        Arrays.fill(bannerC, bannerComp);
        return new String(bannerC);
    }

    public static void clsBliss() {
        try {
            cls();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void cls() throws IOException, InterruptedException {
        final SystemUtils.OS os = SystemUtils.getOS();
        switch (os) {
            case WINDOWS:
                clsWin();
                break;
            default:
            case LINUX:
            case MAC:
            case SOLARIS:
                clsLinux();
                break;
        }
    }

    private static void clsWin() throws IOException, InterruptedException {
        RuntimeUtils.waitForProcess("cmd", "/c", "cls");
    }

    private static void clsLinux() throws IOException, InterruptedException {
        RuntimeUtils.waitForProcess("clear");
    }

}
