package jPlus.async.command;

public abstract class Command implements ICommand {
    @Override
    public void terminate() {
        onTerminate();
    }

    @Override
    public void reverse() {
        initialize();
    }

    @Override
    public void run() {
        initialize();
        onSuccessfulRun();
        body();
        end();
    }

    protected abstract void body();

    protected void end() {
        onEnd();
    }

    //***************************************************************//

    protected void onSuccessfulRun() {
    }

    protected void onTerminate() {
    }

    protected void initialize() {
    }

    protected void onEnd() {
    }
}
