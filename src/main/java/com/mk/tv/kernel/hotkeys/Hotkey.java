package com.mk.tv.kernel.hotkeys;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedHashMap;

public class Hotkey {
    public final String code;

    public Hotkey() {
        code = "";
    }

    public Hotkey(String code) {
        this.code = code;
    }

    public static TypeReference<LinkedHashMap<String, Hotkey>> typeRef() {
        return new TypeReference<>() {
        };
    }
}
