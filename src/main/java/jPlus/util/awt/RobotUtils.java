package jPlus.util.awt;

import jPlus.async.Threads;

import java.awt.*;

public class RobotUtils {
    public static final int SLEEP_DUR = 100000;
    public static Robot robot;

    public static void start() {
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException ignored) {
            }
        }
    }

    public static void down(int keyEvent) {
        start();
        System.out.println(keyEvent);
        robot.keyPress(keyEvent);
    }

    public static void up(int keyEvent) {
        start();
        robot.keyRelease(keyEvent);
    }

    public static void press(int keyEvent) {
        down(keyEvent);
        up(keyEvent);
    }

    public static void press(int[] keyEvents){
        Threads.later(() -> {
            pressAndSleep(keyEvents);
        }, 0);
    }

    private static void pressAndSleep(int[] keyEvents) {
        downAndSleep(keyEvents);
        Threads.sleepBliss(0, SLEEP_DUR);
        upAndSleep(keyEvents);

    }

    private static void upAndSleep(int[] keyEvents) {
        for(int e : keyEvents) {
            upAndSleep(e);
            Threads.sleepBliss(0, SLEEP_DUR);
        }
    }

    private static void upAndSleep(int e) {
        if(e >= 0) up(e);
    }

    private static void downAndSleep(int[] keyEvents) {
        for(int e : keyEvents) {
            downAndSleep(e);
            Threads.sleepBliss(0, SLEEP_DUR);
        }
    }

    private static void downAndSleep(int e) {
        if(e >= 0) down(e);
        else Threads.sleepBliss(-e);
    }

    public static void press(int[][] keyEventSets) {
        Threads.later(() -> {
            for(int[] keyEvents : keyEventSets) {
                pressAndSleep(keyEvents);
                Threads.sleepBliss(0, SLEEP_DUR);
            }
        },0);
    }
}
