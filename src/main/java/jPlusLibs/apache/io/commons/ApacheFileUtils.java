package jPlusLibs.apache.io.commons;

import jPlus.util.collections.ArrayUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApacheFileUtils {
    public static String fileFormatsReg(String[] formats){
        final StringBuilder building = new StringBuilder("^.*\\.(");
        building.append(formats[0]);
        for(int i = 1; i < formats.length; i++){
            building.append('|').append(formats[i]);
        }
        building.append(")");
        return building.toString();
    }

    private static Collection<File> getRecursiveFiles(File folder, String formatsRegex) {
        return FileUtils.listFiles(
                folder,
                new RegexFileFilter(formatsRegex),
                DirectoryFileFilter.DIRECTORY
        );
    }

    public static Collection<File> getRecursiveFiles(File folder, String[] formatsArr) {
        return getRecursiveFiles(folder, fileFormatsReg(formatsArr));
    }

    public static List<List<File>> getRecursiveFiles(File[] folders, String[] formatsArr){
        final List<List<File>> ret = new ArrayList<>();
        for(File folder : folders) ret.add(new ArrayList<>(getRecursiveFiles(folder, formatsArr)));
        return ret;
    }

    public static File[] getRecursiveFilesArr(File folder, String formatsRegex){
        return FileUtils.convertFileCollectionToFileArray(getRecursiveFiles(folder, formatsRegex));
    }

    public static File[] getRecursiveFilesArr(File folder, String[] formatsArr){
        return FileUtils.convertFileCollectionToFileArray(getRecursiveFiles(folder, formatsArr));
    }

    public static File[][] getRecursiveFilesArr(File[] folders, String[] formatsArr){
        final File[][] ret = ArrayUtils.of(File[].class, folders.length);
        int index = 0;
        for(File folder : folders) ret[index++] = getRecursiveFilesArr(folder, formatsArr);

        return ret;
    }
}
