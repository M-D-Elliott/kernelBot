package com.mk.tv.entryPoints;

import com.mk.tv.auth.AuthConfig;
import com.mk.tv.auth.AuthKernel;
import com.mk.tv.io.console.ConsoleMain;
import com.mk.tv.io.discord.DiscordEP;
import com.mk.tv.io.spring.SpringEP;
import com.mk.tv.kernel.controllers.system.IOConfig;
import com.mk.tv.kernel.generic.Config;
import jPlus.async.command.ThreadCommand;
import jPlus.io.file.DirUtils;
import jPlus.util.io.RuntimeUtils;
import jPlusLibs.com.fasterxml.jackson.JacksonUtils;

public class KBMain {
    public static void main(String[] args) {
        final AuthConfig config =
                JacksonUtils.readAndUpdateBliss(DirUtils.fromUserDir(Config.CONFIG_PATH),
                        AuthConfig.class, AuthConfig::newInstance);

        final AuthKernel kernel = new AuthKernel(config);
        kernel.init();

        RuntimeUtils.addOnShutdown(KBMain::onShutdown, "kernelBotShutdown");

        final IOConfig discordIOConfig = config.system.ioConfigs.get(Config.DISCORD_IO_CONFIG_NAME);
        final IOConfig consoleIOConfig = config.system.ioConfigs.get(Config.CONSOLE_IO_CONFIG_NAME);
        final IOConfig webIOConfig = config.system.ioConfigs.get(Config.WEB_IO_CONFIG_NAME);

        if (discordIOConfig != null && discordIOConfig.isActive) {
            if (consoleIOConfig != null && consoleIOConfig.isActive)
                new ConsoleMain(kernel).startConsoleThread();

            DiscordEP.main(kernel, config.system.commandIndicator, discordIOConfig.key);
        } else if (consoleIOConfig != null && consoleIOConfig.isActive)
            new ConsoleMain(kernel).consoleListener();

        if (webIOConfig != null && webIOConfig.isActive)
            SpringEP.main(kernel, webIOConfig.key);
    }

    //***************************************************************//

    protected static void onShutdown() {
        System.out.println("Shutting down...");
        ThreadCommand.terminateAllAndWait();
    }
}
