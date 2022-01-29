package jPlus.util.io;

public class JarUtils {
    public static String version() {
        return JarUtils.class.getPackage().getImplementationVersion();
    }
}
