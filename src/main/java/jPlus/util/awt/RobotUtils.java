package jPlus.util.awt;

import java.awt.*;

public class RobotUtils {
    public static final int BETWEEN_ACTION_SLEEP = 100000;
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
        robot.waitForIdle();
    }

    public static void up(int keyEvent) {
        start();
        robot.keyRelease(keyEvent);
        robot.waitForIdle();
    }

    public static void press(int keyEvent) {
        down(keyEvent);
        up(keyEvent);
    }

    //***************************************************************//

    private static void up(int[] keyEvents) {
        for (int e : keyEvents) if (e >= 0) up(e);
    }

    private static void downOrSleep(int[] keyEvents) throws InterruptedException {
        for (int e : keyEvents) {
            if (e >= 0) down(e);
            else Thread.sleep(-e);
        }
    }

    public static void press(int[] keyEvents) throws InterruptedException {
        downOrSleep(keyEvents);
        up(keyEvents);
    }

    public static void press(int[][] keyEventSets) throws InterruptedException {
        for (int[] keyEvents : keyEventSets) press(keyEvents);
    }

    public static void pressBliss(int[][] keyEventSets) {
        try {
            press(keyEventSets);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.out.println("Press interrupted.");
        }
    }
}
