package jPlus.util.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanPlus extends AtomicBoolean {

    public AtomicBooleanPlus(boolean initialValue) {
        super(initialValue);
    }

    public AtomicBooleanPlus() {
    }

    public void setF(){
        set(false);
    }

    public void setT(){
        set(true);
    }
}
