package com.mk.tv.kernel.presses;

import com.fasterxml.jackson.core.type.TypeReference;
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
