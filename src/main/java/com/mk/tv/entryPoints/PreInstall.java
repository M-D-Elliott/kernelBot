package com.mk.tv.entryPoints;

import jPlus.io.ResourceUtils;

public class PreInstall implements Runnable {
    public void run() {
        System.out.println("Producing install batches...");
        ResourceUtils.toFile("install.bat");
        ResourceUtils.toFile("installWizard.bat");
    }
}
