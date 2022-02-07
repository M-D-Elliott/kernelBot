package jPlus.util.io;

import java.util.Scanner;

public class ConsoleIOUtils {

    public static final String NOT_YET_IMPLEMENTED = "Not yet implemented!";
    public static final char INDICATOR = '>';
    private static final Scanner inScanner = new Scanner(System.in);

    public static boolean validateString(String[] args, int index) {
        return args.length > index && args[index].length() >= 1;
    }

    public static boolean validateChar(String[] args, int index) {
        return args.length > index && args[index].length() == 1;
    }

    public static String request(String s) {
        System.out.println(s);
        System.out.print(INDICATOR);
        return inScanner.nextLine();
    }

    public static boolean confirm(String s) {
        final String in = request(s + " (y/n)");
        return in.charAt(0) == 'y';
    }

    public static void confirm(String s, Runnable run) {
        if (confirm(s)) run.run();
    }

    //***************************************************************//
}
