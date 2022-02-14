package jPlusLibs.generic;

import java.util.Collection;
import java.util.Map;

public abstract class MapRepo<DATA_TYPE> implements IRepo<DATA_TYPE> {

    protected final Map<String, DATA_TYPE> map;

    public MapRepo(Map<String, DATA_TYPE> map) {
        this.map = map;
    }

    @Override
    public void add(DATA_TYPE newItem) {
        map.put(newItem.toString(), newItem);
        save();
    }

    @Override
    public abstract void save();

    @Override
    public DATA_TYPE get(String name) {
        return map.get(name);
    }

    @Override
    public Collection<String> keys() {
        return map.keySet();
    }
}
