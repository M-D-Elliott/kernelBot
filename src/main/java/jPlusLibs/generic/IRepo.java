package jPlusLibs.generic;

import java.util.Collection;

public interface IRepo<DATA_TYPE> {
    void add(DATA_TYPE item);

    void save();

    DATA_TYPE get(String key);

    Collection<String> keys();
}
