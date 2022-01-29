package com.mk.tv.auth.sessions;

public class Session {
    public long startTime;
    public long lastPing;
    public String username;

    public Session() {
        this.startTime = this.lastPing = System.currentTimeMillis();
    }

    public Session(String username){
        this();
        this.username = username;
    }

    //***************************************************************//
}
