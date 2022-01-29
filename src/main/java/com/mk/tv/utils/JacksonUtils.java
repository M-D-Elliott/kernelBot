package com.mk.tv.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jPlus.lang.callback.Retrievable;

import java.io.File;
import java.io.IOException;

public class JacksonUtils {
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final ObjectWriter pretty = mapper.writerWithDefaultPrettyPrinter();

    public static <TYPE> void writeBliss(String path, TYPE obj) {
        final File file = new File(path);
        try {
            JacksonUtils.pretty.writeValue(file, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <TYPE> TYPE readBliss(String path, Class<TYPE> clazz, Retrievable<TYPE> newInstance) {
        final File file = new File(path);
        try {
            return file.exists()
                    ? mapper.readValue(file, clazz)
                    : newInstance.retrieve();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newInstance.retrieve();
    }

    public static <TYPE> TYPE readBliss(String path, TypeReference<TYPE> typeRef, Retrievable<TYPE> newInstance) {
        final File file = new File(path);
        try {
            return file.exists()
                    ? mapper.readValue(file, typeRef)
                    : newInstance.retrieve();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newInstance.retrieve();
    }

    public static <TYPE> TYPE readGenericBliss(String path, Retrievable<TYPE> newInstance) {
        final File file = new File(path);
        try {
            return file.exists()
                    ? mapper.readValue(file, new TypeReference<TYPE>(){})
                    : newInstance.retrieve();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newInstance.retrieve();
    }

    public static <TYPE> TYPE[] readGenericArrBliss(String path, Retrievable<TYPE[]> newInstance) {
        final File file = new File(path);
        try {
            return file.exists()
                    ? mapper.readValue(file, new TypeReference<TYPE[]>(){})
                    : newInstance.retrieve();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newInstance.retrieve();
    }

    public static <TYPE> TYPE readAndUpdateBliss(String path, TypeReference<TYPE> typeRef, Retrievable<TYPE> newInstance) {
        final File file = new File(path);
        try {
            TYPE ret = file.exists()
                    ? mapper.readValue(file, typeRef)
                    : newInstance.retrieve();
            JacksonUtils.pretty.writeValue(file, ret);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newInstance.retrieve();
    }

    public static <TYPE> TYPE readAndUpdateBliss(String path, Class<TYPE> clazz, Retrievable<TYPE> newInstance) {
        final File file = new File(path);
        try {
            TYPE ret = file.exists()
                    ? mapper.readValue(file, clazz)
                    : newInstance.retrieve();
            JacksonUtils.pretty.writeValue(file, ret);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newInstance.retrieve();
    }
}
