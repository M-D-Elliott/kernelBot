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

    //***************************************************************//

    public static void down(int keyEvent) {
        start();
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

    //***************************************************************//

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

    private static void pressAndSleep(int[] keyEvents) {
        downAndSleep(keyEvents);
        Threads.sleepBliss(0, SLEEP_DUR);
        upAndSleep(keyEvents);
    }

    public static void press(int[] keyEvents) {
        pressAndSleep(keyEvents);
    }

    public static void press(int[][] keyEventSets) {
        for(int[] keyEvents : keyEventSets) {
            pressAndSleep(keyEvents);
            Threads.sleepBliss(0, SLEEP_DUR);
        }
    }

    //***************************************************************//

    public static void pressAsync(int[] keyEvents){
        Threads.later(() -> {
            press(keyEvents);
        }, 0);
    }

    public static void pressAsync(int[][] keyEventSets) {
        Threads.later(() -> {
            press(keyEventSets);
        },0);
    }
}
