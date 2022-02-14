package com.mk.tv.kernel;

import jPlus.async.command.ThreadCommand;

public class BusyCommand extends ThreadCommand {
    protected String busyMessage = "";
    protected Runnable body = null;
    protected Runnable onBusy = null;

    @Override
    protected void body() {
        body.run();
    }

    @Override
    protected void onBusy() {
        onBusy.run();
    }
}
