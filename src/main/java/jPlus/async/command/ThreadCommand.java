package jPlus.async.command;

import java.util.HashSet;
import java.util.Set;

import static jPlus.async.Threads.sleepBliss;

public abstract class ThreadCommand implements Command {
    protected Thread thread;

    protected abstract void body();

    //***************************************************************//

    @Override
    public final void run() {
        if (isDormant()) {
            initialize();
            beforeSuccessfulRun();
            thread = new Thread(this::start);
            thread.start();
        } else onBusy();
    }

    protected void beforeSuccessfulRun() {
        ACTIVE_THREAD_COMMANDS.add(this);
    }

    protected void start() {
        body();
        end();
    }

    protected void onBusy(){
    }

    public void terminate() {
    }

    protected void initialize() {
    }

    @Override
    public void reverse() {
        initialize();
    }

    protected void end() {
        thread = null;
        ACTIVE_THREAD_COMMANDS.remove(this);
        onEnd();
    }

    protected void onEnd() {
    }

    //***************************************************************//

    public boolean isDormant() {
        return thread == null;
    }

    //***************************************************************//

    static final Set<ThreadCommand> ACTIVE_THREAD_COMMANDS = new HashSet<>();
    private static final int TERMINATE_AND_WAIT_INCREMENT = 50;

    public static void terminateAll() {
        ACTIVE_THREAD_COMMANDS.forEach(ThreadCommand::terminate);
    }

    public static void terminateAllAndWait() {
        terminateAll();
        while (activeThreadCommandsExist()) sleepBliss(TERMINATE_AND_WAIT_INCREMENT);
    }

    public static boolean activeThreadCommandsExist() {
        return ACTIVE_THREAD_COMMANDS.size() > 0;
    }
}
