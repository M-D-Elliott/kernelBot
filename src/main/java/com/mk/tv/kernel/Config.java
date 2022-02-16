package com.mk.tv.kernel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mk.tv.kernel.mixes.MixConfig;
import com.mk.tv.kernel.presses.PressConfig;
import com.mk.tv.kernel.scripts.ScriptConfig;
import com.mk.tv.kernel.system.SystemConfig;
import jPlusLibs.jackson.JacksonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    public SystemConfig system = new SystemConfig();
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
}
