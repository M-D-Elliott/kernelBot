package jPlusLibs.com.fasterxml.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import jPlus.lang.callback.Retrievable;
import jPlus.util.map.MapUtils;
import jPlusLibs.generic.MapRepo;

import java.util.LinkedHashMap;

import static jPlusLibs.com.fasterxml.jackson.JacksonUtils.readAndUpdateBliss;

public class JacksonRepo<DATA_TYPE> extends MapRepo<DATA_TYPE> {
    protected final String path;

    public JacksonRepo(String path,
                       TypeReference<LinkedHashMap<String, DATA_TYPE>> ref) {
        this(path, ref, MapUtils::newLinkedInstance);
    }

    public JacksonRepo(String path,
                       TypeReference<LinkedHashMap<String, DATA_TYPE>> ref,
                       Retrievable<LinkedHashMap<String, DATA_TYPE>> newInstance) {
        super(readAndUpdateBliss(path, ref, newInstance));
        this.path = path;
    }

    @Override
    public void save() {
        JacksonUtils.writeBliss(path, map);
    }

}
