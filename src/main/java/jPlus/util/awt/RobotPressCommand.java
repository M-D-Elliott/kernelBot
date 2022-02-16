package jPlus.util.awt;

import jPlus.async.command.LoopCommand;

import java.util.Collection;
import java.util.Iterator;

public class RobotPressCommand extends LoopCommand {

    private final Iterator<Collection<Integer>> keyEvents;

    public RobotPressCommand(Iterator<Collection<Integer>> keyEvents) {
        super();
        this.keyEvents = keyEvents;
    }

    @Override
    protected boolean condition() {
        return keyEvents.hasNext();
    }

    @Override
    protected void loopBody() {
        Collection<Integer> keySets = keyEvents.next();
        try {
            Iterator<Integer> iter = keySets.iterator();
            while (baseCondition() && iter.hasNext()) RobotUtils.downOrSleep(iter.next());
            iter = keySets.iterator();
            while (baseCondition() && iter.hasNext()) RobotUtils.validUp(iter.next());
        } catch (InterruptedException ignored) {
            terminate();
        }
    }
}
