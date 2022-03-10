package com.mk.tv.kernel.generic;

import jPlus.async.command.ThreadCommand;

public class BusyCommand extends ThreadCommand {
    public String busyMessage = "";
    public Runnable body = null;

    @Override
    protected void threadBody() {
        body.run();
    }
}
