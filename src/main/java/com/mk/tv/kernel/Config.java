package com.mk.tv.kernel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mk.tv.kernel.mixes.MixConfig;
import com.mk.tv.kernel.presses.PressConfig;
import com.mk.tv.kernel.scripts.ScriptConfig;
import jPlusLibs.jackson.JacksonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    public String token = "paste token here...";
    public char commandIndicator = '?';
    private String menuBorder = "+";

    public char addDelimiter = '+';
    public char nextDelimiter = '>';
    public String startup = "";

    public ScriptConfig script = new ScriptConfig();
    public PressConfig press = new PressConfig();
    public MixConfig mix = new MixConfig();

    public Config() {
    }

    public void store() {
        JacksonUtils.writeBliss("config.txt", this);
    }

    public static Config newConfigInstance() {
        return new Config();
    }

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
