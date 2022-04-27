package com.mk.tv.kernel.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mk.tv.kernel.mixes.MixConfig;
import com.mk.tv.kernel.presses.PressConfig;
import com.mk.tv.kernel.scripts.ScriptConfig;
import com.mk.tv.kernel.system.SystemConfig;
import com.mk.tv.kernel.tools.ToolsConfig;
import jPlusLibs.com.fasterxml.jackson.JacksonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    public SystemConfig system = new SystemConfig();
    public ScriptConfig script = new ScriptConfig();
    public PressConfig press = new PressConfig();
    public MixConfig mix = new MixConfig();
    public ToolsConfig tools = new ToolsConfig();

    public Config() {
    }

    public void store() {
        JacksonUtils.writeBliss("config.txt", this);
    }

    public static Config newConfigInstance() {
        return new Config();
    }
}
