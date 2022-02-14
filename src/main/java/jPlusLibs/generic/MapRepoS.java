package jPlusLibs.generic;

import java.util.Map;

public class MapRepoS<DATA_TYPE> extends MapRepo<DATA_TYPE> {
    public MapRepoS(Map<String, DATA_TYPE> map) {
        super(map);
    }

    @Override
    public void save() {
    }
}
