package jPlusLibs.apache.io.commons;

import jPlus.io.ResourceUtils;
import jPlus.io.file.DirUtils;
import jPlus.lang.callback.Retrievable2;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ApacheResourceUtils {
    public static void extractZipAsDir(String resourcePath, Retrievable2<Boolean, ZipEntry, File> validator) {

        final ZipInputStream zis = new ZipInputStream(ResourceUtils.asStream(resourcePath + ".zip"));

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

    public static void extractZipAsDir(String resourcePath) {
        extractZipAsDir(resourcePath, (ze, f) -> true);
    }
}
