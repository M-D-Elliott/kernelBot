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

    private static void upAndSleep(int[] keyEvents) {
        for (int e : keyEvents) if (e >= 0) up(e);
    }

    private static void downAndSleep(int[] keyEvents) throws InterruptedException {
        for (int e : keyEvents) {
            if (e >= 0) down(e);
            else Thread.sleep(-e);
        }
    }

    private static void pressAndSleep(int[] keyEvents) throws InterruptedException {
        downAndSleep(keyEvents);
        upAndSleep(keyEvents);
    }

    public static void press(int[] keyEvents) throws InterruptedException {
        pressAndSleep(keyEvents);
    }

    public static void press(int[][] keyEventSets) throws InterruptedException {
        for (int[] keyEvents : keyEventSets) pressAndSleep(keyEvents);
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
