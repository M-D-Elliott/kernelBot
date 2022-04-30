package com.mk.tv.kernel.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mk.tv.kernel.controllers.mixes.MixConfig;
import com.mk.tv.kernel.controllers.presses.PressConfig;
import com.mk.tv.kernel.controllers.scripts.ScriptConfig;
import com.mk.tv.kernel.controllers.system.IOConfig;
import com.mk.tv.kernel.controllers.system.SystemConfig;
import com.mk.tv.kernel.controllers.tools.ToolsConfig;
import jPlusLibs.com.fasterxml.jackson.JacksonUtils;

import java.util.HashMap;
import java.util.Map;

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

    //***************************************************************//

    public static final String CONFIG_PATH = "repos/config.txt";
    public static final String DISCORD_IO_CONFIG_NAME = "discord";
    public static final String CONSOLE_IO_CONFIG_NAME = "console";

    protected static Map<String, IOConfig> defaultIOConfigs() {
        final Map<String, IOConfig> ret = new HashMap<>();
        ret.put(Config.DISCORD_IO_CONFIG_NAME, new IOConfig());
        ret.put(Config.CONSOLE_IO_CONFIG_NAME, new IOConfig());

        return ret;
    }

    public static Config newConfigInstance() {
        final Config config = new Config();
        config.system.ioConfigs = defaultIOConfigs();
        return config;
    }


}
