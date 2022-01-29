package jPlus.io;

import jPlus.io.file.DirUtils;
import jPlus.lang.callback.Retrievable2;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        return ret;
    }

    public static InputStream asStream(String path) {
        return ResourceUtils.class.getClassLoader().getResourceAsStream(path);
    }

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void copyInputStreamToFile(InputStream inputStream, File file) {

        if (inputStream != null) {
            try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
                int read;
                byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else System.err.println("Resource stream null.");
    }

    public static void toFile(String name) {
        final String path = DirUtils.fromUserDir(name);
        toFile(name, path);
    }

    public static void toFile(String name, String pathS) {
        try (InputStream is = asStream(name)) {
            final Path path = Paths.get(pathS);
            Files.deleteIfExists(path);
            Files.copy(is, path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void extractZipAsDir(String resourcePath) {
        extractZipAsDir(resourcePath, (ze, f) -> true);
    }

    public static void extractZipAsDir(String resourcePath, Retrievable2<Boolean, ZipEntry, File> validator) {

        final ZipInputStream zis = new ZipInputStream(asStream(resourcePath + ".zip"));

        try {
            final File outDir = DirUtils.make(System.getProperty("user.dir") + File.separator + resourcePath);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                final File outFile = new File(outDir, entry.getName());
                if (validator.retrieve(entry, outFile) && outFile.createNewFile()) {
                    try (FileOutputStream fis = new FileOutputStream(outFile)) {
                        IOUtils.copy(zis, fis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
