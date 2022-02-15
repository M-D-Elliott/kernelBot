package jPlus.util.io;

public class JarUtils {
    public static String version() {
        final String ret = JarUtils.class.getPackage().getImplementationVersion();
        return ret == null ? "DEVELOPMENT" : ret;
    }

    public static String title() {
        final String ret = JarUtils.class.getPackage().getImplementationVersion();
        return ret == null ? "DEVELOPMENTtitle" : ret;
    }
}
