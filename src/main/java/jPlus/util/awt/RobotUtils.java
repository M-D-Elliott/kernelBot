package jPlus.util.awt;

import jPlus.async.Threads;

import java.awt.*;

public class RobotUtils {
    public static final int DUR = 1;
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
        robot.keyPress(keyEvent);
    }

    private static void down(int[] keyEvents, int i) {
        down(keyEvents, i, 1);
    }

    private static void down(int[] keyEvents, int i, int dir) {
        if (i < keyEvents.length && i >= 0) {
            final int key = keyEvents[i];
            down(key);
            Threads.later(() -> {
                down(keyEvents, i + dir, dir);
            }, DUR);
        }
    }

    public static void up(int keyEvent) {
        start();
        robot.keyRelease(keyEvent);
    }

    private static void up(int[] keyEvents, int i) {
        up(keyEvents, i, 1);
    }

    private static void up(int[] keyEvents, int i, int dir) {
        if (i < keyEvents.length && i >= 0) {
            final int key = keyEvents[i];
            up(key);
            Threads.later(() -> {
                up(keyEvents, i + dir, dir);
            }, DUR);
        }
    }

    public static void press(int keyEvent) {
        down(keyEvent);
        up(keyEvent);
    }

    public static void press(int[] keyEvents) {
        down(keyEvents, 0);
        Threads.later(() ->{
            up(keyEvents, keyEvents.length - 1, -1);
        }, (keyEvents.length -1) * DUR);
    }
}
