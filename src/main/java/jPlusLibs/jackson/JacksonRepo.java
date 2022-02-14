package jPlusLibs.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import jPlusLibs.jackson.JacksonUtils;
import jPlus.lang.callback.Retrievable;
import jPlus.util.map.MapUtils;
import jPlusLibs.jackson.Repo;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class JacksonRepo<DATA_TYPE> implements Repo<DATA_TYPE> {
    protected final String path;
    protected final Map<String, DATA_TYPE> map;

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

    @Override
    public void add(DATA_TYPE newUser) {
        map.put(newUser.toString(), newUser);
        save();
    }

    @Override
    public void save() {
        JacksonUtils.writeBliss(path, map);
    }

    @Override
    public DATA_TYPE get(String name) {
        return map.get(name);
    }

    @Override
    public Collection<String> keys() {
        return map.keySet();
    }
}
