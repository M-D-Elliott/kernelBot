package com.mk.tv.entryPoints;

import com.mk.tv.auth.AuthConfig;
import com.mk.tv.auth.AuthKernel;
import com.mk.tv.kernel.Kernel;
import com.mk.tv.kernel.generic.Config;
import com.mk.tv.kernel.generic.IFuncController;
import com.mk.tv.kernel.generic.KernelGrammar;
import com.mk.tv.kernel.system.SystemController;
import com.mk.tv.spring.IOController;
import com.mk.tv.spring.SpringApp;
import jPlus.async.command.ThreadCommand;
import jPlus.io.file.DirUtils;
import jPlus.io.in.PrintStreamWrapper;
import jPlus.util.io.ConsoleIOUtils;
import jPlus.util.io.RuntimeUtils;
import jPlusLibs.com.edu.sphinx.SphinxUtils;
import jPlusLibs.com.fasterxml.jackson.JacksonUtils;
import jPlusLibs.discord.listener.text.DiscordTextListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.boot.SpringApplication;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.concurrent.CountDownLatch;

public class Run implements Runnable {

    @Override
    public void run() {
        final AuthConfig config =
                JacksonUtils.readAndUpdateBliss("config.txt",
                        AuthConfig.class, AuthConfig::newInstance);

        final Kernel kernel = new AuthKernel(config);
        kernel.init();

        RuntimeUtils.addOnShutdown(this::onShutdown, "kernelBotShutdown");

        if (config.system.listenToDiscord) {
            if (config.system.listenToConsole) startConsoleThread(kernel);
            discordListener(kernel, config);
        } else if (config.system.listenToConsole)
            consoleListener(kernel, new CountDownLatch(1));

        final File hostsFile = new File(DirUtils.fromUserDir(".hosts"));
        if (hostsFile.exists()) {
            SpringApplication.run(SpringApp.class);
            IOController.instance.setRecipient(kernel);
        }
    }

    private void discordListener(Kernel kernel, Config config) {

        discordVoiceListener(kernel);

        try {
            JDABuilder.createDefault(config.system.token)
                    .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .enableCache(CacheFlag.ACTIVITY)
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.listening(
                            String.format(SystemController.ACTIVITY_RAW, config.system.commandIndicator)))
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .enableCache(CacheFlag.VOICE_STATE)
                    .addEventListeners(new DiscordTextListener(kernel))
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void discordVoiceListener(Kernel kernel) {
        final String grammarPath = DirUtils.fromUserDir("repos" + File.separatorChar + "system");
        System.out.println(grammarPath);
        DirUtils.make(grammarPath);
        KernelGrammar.write(kernel, grammarPath);
        SphinxUtils.conf.setGrammarPath("file:" + grammarPath);

//        final DiscordVoiceListener discordVoiceOut = new DiscordVoiceListener(kernel);
//        kernel.getSyncFunctions().put("listen", discordVoiceOut::listenToVoiceChannel);
        kernel.getSyncFunctions().put("listen", (api, args) -> api.println("Sorry, this feature is not yet working quite right..."));
        final IFuncController systemController = kernel.getController(SystemController.class);
        if (systemController != null) systemController.menu().add("listen");
    }

    private void startConsoleThread(Kernel kernel) {
        final Thread th =
                new Thread(() -> consoleListener(kernel, new CountDownLatch(1)));
        th.setDaemon(true);
        th.start();
    }

    private void consoleListener(Kernel kernel, CountDownLatch latch) {
        Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown, "terminateConsoleOut"));
        final PrintStreamWrapper api = new PrintStreamWrapper(System.out);
        while (latch.getCount() > 0) {
            api.setIn(ConsoleIOUtils.request());
            kernel.retrieve(api);
        }
    }

    private void onShutdown() {
        System.out.println("Shutting down...");
        ThreadCommand.terminateAllAndWait();
    }
}
