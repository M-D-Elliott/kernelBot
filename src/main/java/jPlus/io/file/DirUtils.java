package jPlus.io.file;

import java.io.File;

public class DirUtils {
    public static File[] make(String... paths) {
        return make(paths, false);
    }

    public static File[] make(String[] paths, boolean emptyDirectory) {
        final File[] ret = new File[paths.length];
        for (int i = 0; i < paths.length; i++) ret[i] = make(paths[i], emptyDirectory);
        return ret;
    }

    public static File make(String path) {
        return make(path, false);
    }

    public static File make(String path, boolean emptyDirectory) {
        File directory = new File(path);

        String absolutePath = directory.getAbsolutePath();

        if (!directory.exists()) {
            if (!directory.mkdirs())
                System.err.println("FileOut failed to construct directory at " + absolutePath);
        } else if (emptyDirectory) empty(directory);


        return directory;
    }

    public static boolean make(File dir, boolean emptyDir) {
        if (!dir.exists()) {
            if (!dir.mkdirs())
            {
                System.err.println("FileOut failed to construct directory at " + dir.getAbsolutePath());
                return false;
            }
        } else if (emptyDir) empty(dir);

        return true;
    }

    public static void empty(File directory) {
        final File[] files = directory.listFiles();
        if (files != null && files.length > 0) FileUtils.delete(files);
    }

    public static boolean delete(File folder) {
        boolean ret = true;
        folder.deleteOnExit();
        final File[] files = folder.listFiles();
        if (files != null)
            for (File file : files)
                ret &= delete(file);
        return ret && folder.delete();
    }

    public static String fromUserDir(String path) {
        return System.getProperty("user.dir") + File.separatorChar + path;
    }
}
