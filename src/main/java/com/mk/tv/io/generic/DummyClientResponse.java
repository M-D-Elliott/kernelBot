package com.mk.tv.io.generic;

import jPlus.io.Resolution;

public class DummyClientResponse implements IClientResponse {

    protected final Resolution resolution;

    public DummyClientResponse() {
        this(Resolution.FAILURE);
    }

    public DummyClientResponse(Resolution resolution) {
        this.resolution = resolution;
    }

    public static IClientResponse success() {
        return new DummyClientResponse(Resolution.SUCCESS);
    }

    @Override
    public Resolution resolution() {
        return resolution;
    }
}
