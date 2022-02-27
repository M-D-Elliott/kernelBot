package jPlus.util.awt;

import java.awt.*;
import java.awt.image.BufferedImage;

import static jPlus.JPlus.sendError;

public class RobotUtils {
    public static Robot robot;

    public static void start() {
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException ex) {
                sendError("RobotUtils: Error creating Robot.", ex);
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

    protected static void up(int[] keyEvents) {
        for (int e : keyEvents) validUp(e);
    }

    protected static void validUp(int e) {
        if (e >= 0) up(e);
    }

    protected static void downOrSleep(int[] keyEvents) throws InterruptedException {
        for (int e : keyEvents) downOrSleep(e);
    }

    protected static void downOrSleep(int e) throws InterruptedException {
        if (e >= 0) down(e);
        else Thread.sleep(-e);
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
        } catch (InterruptedException ignored) {
        }
    }

    //***************************************************************//

    public static BufferedImage capture() {
        return capture(new Point(0, 0));
    }

    public static BufferedImage capture(Point p) {
        return capture(p, Toolkit.getDefaultToolkit().getScreenSize());
    }

    public static BufferedImage capture(Point p, Dimension dim) {
        start();
        return robot.createScreenCapture(new Rectangle(p.x, p.y, dim.width, dim.height));
    }
}
