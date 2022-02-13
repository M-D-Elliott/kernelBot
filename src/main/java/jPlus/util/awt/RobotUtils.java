package jPlus.util.awt;

import java.awt.*;

import static jPlus.async.Threads.sleepBliss;

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
        for (int e : keyEvents) {
            upAndSleep(e);
            sleepBliss(0, SLEEP_DUR);
        }
    }

    private static void upAndSleep(int e) {
        if (e >= 0) up(e);
    }

    private static void downAndSleep(int[] keyEvents) {
        for (int e : keyEvents) {
            downAndSleep(e);
            sleepBliss(0, SLEEP_DUR);
        }
    }

    private static void downAndSleep(int e) {
        if (e >= 0) down(e);
        else sleepBliss(-e);
    }

    private static void pressAndSleep(int[] keyEvents) {
        downAndSleep(keyEvents);
        sleepBliss(0, SLEEP_DUR);
        upAndSleep(keyEvents);
    }

    public static void press(int[] keyEvents) {
        pressAndSleep(keyEvents);
    }

    public static void press(int[][] keyEventSets) {
        for (int[] keyEvents : keyEventSets) {
            pressAndSleep(keyEvents);
            sleepBliss(0, SLEEP_DUR);
        }
    }

    //***************************************************************//

    public static void pressAsync(int[] keyEvents) {
        new Thread(() -> {
            press(keyEvents);
        }).start();
    }

    public static void pressAsync(int[][] keyEventSets) {
        new Thread(() -> {
            press(keyEventSets);
        }).start();
    }
}
