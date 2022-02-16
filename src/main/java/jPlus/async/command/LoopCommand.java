package jPlus.async.command;

public abstract class LoopCommand extends Command {
    protected boolean terminated = false;

    @Override
    protected final void initialize() {
        terminated = false;
    }

    @Override
    public final void terminate() {
        terminated = true;
        super.terminate();
    }

    //***************************************************************//
    
    @Override
    public void body(){
        while (baseCondition() && condition()) loopBody();
        if (!terminated) onStandardEnd();
    }

    protected boolean baseCondition() {
        return !terminated;
    }

    protected abstract boolean condition();

    protected abstract void loopBody();

    protected void onStandardEnd() {
    }
}
