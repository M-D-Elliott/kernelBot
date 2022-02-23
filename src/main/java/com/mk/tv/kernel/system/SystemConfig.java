package com.mk.tv.kernel.system;

public class SystemConfig {
    public String token = "paste token here...";
    public char commandIndicator = '?';
    private String menuBorder = "+";

    public char addDelimiter = '+';
    public char nextDelimiter = '>';
    public String startup = "splash";
    public boolean listenToDiscord = true;
    public boolean listenToConsole = false;

    private static final String LITERAL_COMMAND = "'%c%s'";

    public String displayLiteralCommand(String s) {
        return String.format(LITERAL_COMMAND, commandIndicator, s);
    }

    public String nextDelimiterS() {
        return Character.toString(nextDelimiter);
    }

    public String getMenuBorder() {
        return menuBorder;
    }

    //TODO I need a regex that turns all single '%' into '%%' but only if they are singles.
    // I used replaceAll but of course that causes the % count to multiply each run.
    // of course I could hide this prop from jackson and have something like menuBorder()
    // or getAdjustedMenuBorder() but I am so nitpicky!!!!
    public void setMenuBorder(String menuBorder) {
        this.menuBorder = menuBorder.equals("%") ? "%%" : menuBorder;
    }
}
