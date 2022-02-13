package com.mk.tv.kernel.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mk.tv.utils.JacksonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    public String token = "paste token here...";
    public char commandIndicator = '?';
    public String[] scriptFormats = new String[]{"bat"};
    public String menuBorder = "+";
    public boolean allowFreePress = false;
    public char addDelimiter = '+';
    public char nextDelimiter = '>';
    public boolean allowFreeMix = false;

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
}
