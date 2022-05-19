package jPlus.io.file;

import jPlus.util.lang.StringUtils;
import jPlus.util.lang.SystemUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static jPlus.JPlus.sendError;

public final class FileUtils {

    public static List<String> read(String path) {
        return read(new File(path));
    }

    public static List<String> read(File file) {
        List<String> lines = new ArrayList<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException ex) {
                sendError("Cannot read file at " + file.getAbsolutePath(), ex);
            }
        }

        return lines;
    }

    public static void write(String[] lines, String path) {
        write(String.join("\n", lines), path);
    }

    public static void write(String lines, String path) {
        write(lines, path, false);
    }

    public static void write(String lines, String path, boolean append) {
        try (
                FileWriter write = new FileWriter(path, append);
                PrintWriter printLine = new PrintWriter(write)
        ) {
            printLine.printf(lines);
        } catch (IOException ex) {
            sendError(String.format("Error writing to path %s.", path), ex);
        }
    }

    public static boolean delete(File... files) {
        boolean successful = true;
        for (File f : files) successful &= f.delete();
        return successful;
    }

    //***************************************************************//

    public static String simpleName(File f) {
        return f.getName().replace('.' + ext(f), "");
    }

    public static String ext(File f) {
        return ext(f.getName());
    }

    public static String ext(String path) {
        final String[] split = path.split("\\.");
        return split[split.length - 1];
    }

    //***************************************************************//

    public static boolean validate(String path) {
        String valid = getPathRegex();
        return validateRegex(path, valid, true);
    }

    public static boolean validateFileName(String name) {
        String valid = "([-_.A-Za-z0-9]){3,}";
        return validateRegex(name, valid, true);
    }

    private static String getPathRegex() {
        String os = System.getProperty("os.name");
        String linuxRegex = "^(/[^/ ]*)+/?$";
        String windowsRegex = "([a-zA-Z]:)?(\\\\[a-zA-Z0-9._-]+)+\\\\?";

        if (os.equals("Linux")) {
            return linuxRegex;
        }
        return windowsRegex;
    }

    public static boolean isValidFilePath(String path) throws IOException {
        return StringUtils.hasContent(new File(path).getCanonicalPath());
    }

    public static boolean isValidFilePathBliss(String path) {
        try {
            return isValidFilePath(path);
        } catch (IOException ex) {
            sendError("Cannot retrieve/validate file path " + path, ex);
        }
        return false;
    }

    public static boolean isValidFilePathAndExists(String path) throws IOException {
        return isValidFilePath(path) && new File(path).exists();
    }

    public static boolean isValidFilePathAndExistsBliss(String path) {
        try {
            return isValidFilePathAndExists(path);
        } catch (IOException ex) {
            sendError("Cannot retrieve/validate file path " + path, ex);
        }
        return false;
    }

    public static boolean validateRegex(final String text, final String regex, boolean positiveValidation) {
        return Pattern.compile(regex).matcher(text).matches() == positiveValidation;
    }

    public static File[] asFiles(String[] paths) {
        return Arrays.stream(paths).map(File::new).toArray(File[]::new);
    }

    public static String addExtSafe(String filePath, String ext) {
        return ext(filePath).equals('.' + ext) ? filePath : filePath + '.' + ext;
    }
}
