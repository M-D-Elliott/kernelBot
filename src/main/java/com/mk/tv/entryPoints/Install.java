package com.mk.tv.entryPoints;

import jPlus.io.ResourceUtils;

import static jPlus.io.ResourceUtils.toFile;

public class Install implements Runnable {
    private final boolean usingJRE;

    public Install(boolean usingJRE) {
        this.usingJRE = usingJRE;
    }

    @Override
    public void run() {
        toFile((usingJRE) ? "startbotJ.bat" : "startbot.bat", "startbot.bat");
        ResourceUtils.extractZipAsDir("myscripts");
        ResourceUtils.extractZipAsDir("repos", (zE, f) -> !f.exists());
    }
}
