package com.mk.tv.auth.botusers;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Session {

    public final long endTime;

    public Session() {
        this(0);
    }

    public Session(long durationS) {
        this.endTime = System.currentTimeMillis() + (durationS * 1000);
    }

    //***************************************************************//
    @JsonIgnore
    public static final Session DEFAULT = new Session();

    public boolean expired(){
        return !active();
    }

    public boolean active(){
        return System.currentTimeMillis() <= endTime;
    }
}
