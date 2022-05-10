package com.mk.tv.kernel.controllers.system;

public class IOConfig {
    public boolean isActive = false;
    public String key = "";

    public IOConfig() {
    }

    public IOConfig(boolean isActive) {
        this.isActive = isActive;
    }

    public IOConfig(String key) {
        this.key = key;
    }

    public IOConfig(boolean isActive, String key) {
        this.isActive = isActive;
        this.key = key;
    }
}
