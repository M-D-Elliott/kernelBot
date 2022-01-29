package com.mk.tv.kernel.generic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mk.tv.utils.JacksonUtils;
import jPlus.lang.callback.Retrievable;
import jPlus.util.map.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class JacksonRepo<DATA_TYPE> {
    private final String path;
    public final Map<String, DATA_TYPE> map;

    public JacksonRepo(String path,
                       TypeReference<LinkedHashMap<String, DATA_TYPE>> ref) {
        this(path, ref, MapUtils::newLinkedInstance);
    }

    public JacksonRepo(String path,
                       TypeReference<LinkedHashMap<String, DATA_TYPE>> ref,
                       Retrievable<LinkedHashMap<String, DATA_TYPE>> newInstance) {
        this.path = path;
        map = JacksonUtils.readAndUpdateBliss(path, ref, newInstance);
    }

    public void add(DATA_TYPE newUser) {
        map.put(newUser.toString(), newUser);
        save();
    }

    public void save() {
        JacksonUtils.writeBliss(path, map);
    }

    public DATA_TYPE get(String name) {
        return map.get(name);
    }
}
