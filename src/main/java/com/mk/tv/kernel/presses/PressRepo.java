package com.mk.tv.kernel.presses;

import com.fasterxml.jackson.core.type.TypeReference;
import jPlus.lang.callback.Retrievable;
import jPlusLibs.jackson.JacksonRepo;

import java.util.LinkedHashMap;

public class PressRepo extends JacksonRepo<String> {
    public PressRepo() {
        this(path());
    }

    public PressRepo(String path) {
        super(path, new TypeReference<>() {
        }, PressRepo::newMap);
    }

    public PressRepo(Retrievable<LinkedHashMap<String, String>> newInstance) {
        this(path(), newInstance);
    }

    public PressRepo(String path, Retrievable<LinkedHashMap<String, String>> newInstance) {
        super(path, new TypeReference<>() {
        }, newInstance);
    }

    private static String path() {
        return "repos/presses.txt";
    }

    private static LinkedHashMap<String, String> newMap() {
        final LinkedHashMap<String, String> ret = new LinkedHashMap<>();
        final String preset = "a+b";
        ret.put("ab", preset);
        return ret;
    }
}
