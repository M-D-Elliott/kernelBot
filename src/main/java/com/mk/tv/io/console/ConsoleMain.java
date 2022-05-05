package com.mk.tv.io.console;

import com.mk.tv.kernel.Kernel;
import jPlus.util.io.ConsoleIOUtils;

import java.util.concurrent.CountDownLatch;

public class ConsoleMain {
    private final Kernel kernel;

    public ConsoleMain(Kernel kernel) {
        this.kernel = kernel;
    }

    public void startConsoleThread() {
        final Thread th = new Thread(this::consoleListener);
        th.setDaemon(true);
        th.start();
    }

    public void consoleListener() {
        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown, "terminateConsoleOut"));
        final PrintStreamWrapper api = new PrintStreamWrapper(System.out);
        while (latch.getCount() > 0) {
            api.setIn(ConsoleIOUtils.request());
            kernel.retrieve(api);
        }
    }
}
