package com.mk.tv.kernel.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jPlusLibs.jackson.JacksonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    public String token = "paste token here...";

    public char commandIndicator = '?';
    public String menuBorder = "+";

    public String[] scriptFormats = new String[]{"bat"};

    public boolean allowFreePress = false;
    public char addDelimiter = '+';
    public char nextDelimiter = '>';
    public boolean allowFreeMix = false;

    public String startup = "";

    public Config() {
    }

    public Config(String token) {
        this.token = token;
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
}
