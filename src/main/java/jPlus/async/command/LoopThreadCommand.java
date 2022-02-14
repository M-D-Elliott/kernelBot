package jPlus.async.command;

public abstract class LoopThreadCommand extends ThreadCommand {

    protected boolean terminated = false;

    @Override
    protected void beforeSuccessfulRun() {
        terminated = false;
        super.beforeSuccessfulRun();
    }

    @Override
    public final void terminate() {
        terminated = true;
        onTerminate();
    }

    //***************************************************************//

    protected abstract boolean condition();

    protected abstract void loopBody();

    protected void onStandardEnd() {

    }

    protected void onTerminate() {
    }

    @Override
    protected void body() {
        initialize();
        while (baseCondition() && condition()) loopBody();
        if (!terminated) onStandardEnd();
    }

    private boolean baseCondition() {
        return !terminated;
    }
}
