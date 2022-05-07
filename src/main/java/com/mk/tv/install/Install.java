package com.mk.tv.install;

import jPlusLibs.apache.io.commons.ApacheResourceUtils;

import static jPlus.io.ResourceUtils.toFile;

public class Install implements Runnable {
    private final boolean usingJRE;

    public Install(boolean usingJRE) {
        this.usingJRE = usingJRE;
    }

    @Override
    public void run() {
        toFile((usingJRE) ? "startbotJ.bat" : "startbot.bat", "startbot.bat");
        ApacheResourceUtils.extractZipAsDir("myscripts");
        ApacheResourceUtils.extractZipAsDir("repos", (zE, f) -> !f.exists());
    }
}
