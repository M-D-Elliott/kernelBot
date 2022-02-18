package jPlus.io;

import jPlus.io.file.DirUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static jPlus.JPlus.sendError;

public final class ResourceUtils {

    public static List<String> read(String path) {
        return read(asStream(path));
    }

    public static List<String> read(InputStream inputStream) {
        final List<String> ret = new ArrayList<>();
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String text;
                while ((text = reader.readLine()) != null) ret.add(text);
            } catch (IOException ex) {
                sendError("Cannot read input stream", ex);
            }
        }

        return ret;
    }

    public static InputStream asStream(String path) {
        return ResourceUtils.class.getClassLoader().getResourceAsStream(path);
    }

    //***************************************************************//

    public static void toFile(String name) {
        toFile(name, DirUtils.fromUserDir(name));
    }

    public static void toFile(String name, String pathS) {
        try (InputStream is = asStream(name)) {
            final Path path = Paths.get(pathS);
            Files.deleteIfExists(path);
            Files.copy(is, path);
        } catch (IOException ex) {
            sendError("Cannot create file " + name + " from resource at " + pathS, ex);
        }
    }

}
