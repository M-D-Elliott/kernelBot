package jPlus.async.command;

import java.util.HashMap;
import java.util.Map;

public abstract class ThreadCommand implements Command {

    protected Thread thread;

    protected boolean terminated = false;

    @Override
    public final void run() {
        if (isDormant()) {
            terminated = false;
            ACTIVE_THREAD_COMMANDS.put(this, null);
            thread = new Thread(this::startLoop);
            thread.start();
        }
    }

    public final void terminate() {
        terminated = true;
        onTerminate();
    }

    //***************************************************************//

    protected abstract boolean condition();

    protected abstract void loopBody();

    protected void initialize() {
        reverse();
    }

    protected void onEnd() {
    }

    protected void onStandardEnd() {

    }

    protected void onTerminate() {
    }

    private void startLoop() {
        initialize();
        while (baseCondition() && condition()) loopBody();
        if (!terminated) onStandardEnd();
        end();
    }

    private void end() {
        thread = null;
        ACTIVE_THREAD_COMMANDS.remove(this);
        onEnd();
    }

    private boolean baseCondition() {
        return !terminated;
    }

    //***************************************************************//

    public boolean isDormant() {
        return thread == null;
    }

    //***************************************************************//

    private static final Map<ThreadCommand, Void> ACTIVE_THREAD_COMMANDS = new HashMap<>();
    private static final long TERMINATE_AND_WAIT_INCREMENT = 50;

    public static void terminateAll() {
        ACTIVE_THREAD_COMMANDS.keySet().forEach(ThreadCommand::terminate);
    }

    public static void terminateAllAndWait() {
        terminateAll();
        while (activeThreadCommandsExist()) trySleep(TERMINATE_AND_WAIT_INCREMENT);
    }

    public static boolean activeThreadCommandsExist() {
        return ACTIVE_THREAD_COMMANDS.size() > 0;
    }

    public static void trySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
