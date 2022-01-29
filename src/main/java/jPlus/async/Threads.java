package jPlus.async;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Threads {
    public static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void later(Runnable run, int dur){
        later(run, dur, TimeUnit.MILLISECONDS);
    }

    public static void later(Runnable run, int dur, TimeUnit tUnit){
        executorService.schedule(run, dur, tUnit);
    }
}
