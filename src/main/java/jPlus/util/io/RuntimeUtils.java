package jPlus.util.io;

import jPlus.lang.callback.Receivable1;
import jPlus.util.lang.SystemUtils;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

import java.io.IOException;
import java.util.Arrays;

import static jPlus.JPlus.sendError;

public class RuntimeUtils {
    protected static boolean exec(String... body) throws IOException {
        return Runtime.getRuntime().exec(body).isAlive();
    }

    public static boolean execBliss(String... body) {
        try {
            return exec(body);
        } catch (IOException ex) {
            sendError("Error executing " + Arrays.toString(body), ex);
        }

        return false;
    }

    public static void execWait(String... body) throws IOException, InterruptedException {
        Runtime.getRuntime().exec(body).waitFor();
    }

    public static void execWaitBliss(String... body) {
        execWaitBliss(Throwable::printStackTrace, body);
    }

    public static void execWaitBliss(Receivable1<InterruptedException> onInterrupt, String... body) {
        try {
            execWait(body);
        } catch (IOException ex) {
            sendError("Error executing " + Arrays.toString(body), ex);
        } catch (InterruptedException e) {
            onInterrupt.receive(e);
        }
    }

    public static void addOnShutdown(Runnable runnable, String key) {
        Runtime.getRuntime().addShutdownHook(new Thread(runnable, key));
    }

    //***************************************************************//

    public static String[] startSWin(String path, String title) {
        return new String[]{"cmd", "/c", "start", "\"" + TASK_LIST_PREPEND + title + "\"", "/wait", "\"" + path + "\""};
    }

    //***************************************************************//

    public static boolean kill(String path, String title) throws NotImplementedException, IOException {
        final SystemUtils.OS os = SystemUtils.getOS();
        switch (os) {
            case WINDOWS:
                return exec(killWin(path, title));
            case LINUX:
                throw new NotImplementedException("result linux task");
            case MAC:
                throw new NotImplementedException("result mac task");
            case SOLARIS:
                throw new NotImplementedException("result solaris task");
            default:
                return false;
        }
    }

    public static boolean killBliss(String path, String title) {
        try {
            return kill(path, title);
        } catch (IOException ex) {
            sendError("Error killing " + title + " at path " + path, ex);
        } catch (NotImplementedException ex) {
            sendError(ex);
        }
        return false;
    }

    public static String[] killWin(String path, String title) {
        return new String[]{"cmd", "/c", "Taskkill /FI \"WINDOWTITLE eq " + TASK_LIST_PREPEND + title + " - " + path + "\""};
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

    //***************************************************************//

    public static String TASK_LIST_PREPEND = "SOMECUSTOMIDHERE";

    private static String sep() {
        return System.lineSeparator();
    }
}
