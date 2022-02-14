package jPlus.util.io;

import jPlus.lang.SystemUtils;

import java.io.IOException;

public class RuntimeUtils {
    protected static boolean exec(String... body) throws IOException {
        return Runtime.getRuntime().exec(body).isAlive();
    }

    public static boolean execBliss(String... body) {
        try {
            return exec(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void execWait(String... process) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(process);
        Process startProcess = pb.inheritIO().start();
        startProcess.waitFor();
    }

    public static void execWaitBliss(String... body) {
        try {
            execWait(body);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //***************************************************************//

    public static String cmdStartString(String path) {
        return "cmd.exe /c start \"\" \"" + path + "\"";
    }

    public static String[] cmdWaitStringArr(String path) {
        return new String[]{"cmd", "/c", "start", "/wait", "\"\"", "\"" + path + "\""};
    }

    //***************************************************************//

    public static boolean web(String url) {
        final SystemUtils.OS os = SystemUtils.getOS();
        switch (os) {
            case WINDOWS:
                return webWin(url);
            case LINUX:
                return webLin(url);
            case MAC:
                return webMac(url);
            case SOLARIS:
                return webSol(url);
            default:
                return false;
        }
    }

    private static boolean webSol(String url) {
        return false;
    }

    private static boolean webMac(String url) {
        return execBliss("open " + url);
    }

    private static boolean webLin(String url) {
        final String[] browsers = {"google-chrome", "firefox", "mozilla", "epiphany", "konqueror",
                "netscape", "opera", "links", "lynx"};

        final StringBuilder cmd = new StringBuilder();
        for (int i = 0; i < browsers.length; i++)
            if (i == 0) cmd.append(String.format("%s \"%s\"", browsers[i], url));
            else cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
        // If the first didn't work, try the next browser and so on

        return execBliss("sh", "-c", cmd.toString());
    }

    private static boolean webWin(String url) {
        return execBliss("rundll32 url.dll,FileProtocolHandler " + url);
    }
}
