package jPlus;

import jPlus.lang.callback.Receivable1;

public class JPlus {
    public static Receivable1<String> logger;

    public static void log(String s) {
        if (logger != null) logger.receive(s);
    }
}
