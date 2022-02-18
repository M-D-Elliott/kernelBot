package jPlus;

import jPlus.lang.callback.Receivable3;

public class JPlus {
    public static Receivable3<String, Throwable, Integer> logger = null;

    public static void sendError(Throwable t) {
        sendError("", t, 1);
    }

    public static void sendError(String message, Throwable t) {
        sendError(message, t, 1);
    }

    public static void sendError(String message, Throwable t, Integer level) {
        if (logger == null) {
            System.err.println(message);
            t.printStackTrace();
        } else {
            logger.receive(message, t, level);
        }
    }


}
