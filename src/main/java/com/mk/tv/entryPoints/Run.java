package com.mk.tv.entryPoints;

import com.mk.tv.auth.AuthConfig;
import com.mk.tv.auth.AuthKernel;
import com.mk.tv.kernel.Config;
import com.mk.tv.kernel.Kernel;
import com.mk.tv.kernel.system.SystemController;
import jPlus.io.out.PrintStreamWrapper;
import jPlus.util.io.ConsoleIOUtils;
import jPlusLibs.discord.DiscordOutListener;
import jPlusLibs.jackson.JacksonUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class Run implements Runnable {

    @Override
    public void run() {
        final AuthConfig config =
                JacksonUtils.readAndUpdateBliss("config.txt",
                        AuthConfig.class, AuthConfig::newInstance);

        final Kernel kernel = new AuthKernel(config);
        kernel.init();

        if (config.system.listenToDiscord) {
            startDiscordListener(kernel, config);
            if (config.system.listenToConsole) startConsoleThread(kernel);
        } else if (config.system.listenToConsole) startConsoleListener(kernel, new CountDownLatch(1));
    }

    private void startDiscordListener(Kernel kernel, Config config) {
        final DiscordOutListener out = new DiscordOutListener(
                Collections.singletonList(kernel)
        );

        try {
            JDABuilder.createDefault(config.system.token)
                    .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .enableCache(CacheFlag.ACTIVITY)
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.listening(
                            String.format(SystemController.ACTIVITY_RAW, config.system.commandIndicator)))
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .addEventListeners(out)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    private void startConsoleThread(Kernel kernel) {

        final CountDownLatch latch = new CountDownLatch(1);

        final Thread th = new Thread(() -> startConsoleListener(kernel, latch));
        th.setDaemon(true);
        th.start();

        Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown, "terminateConsoleOut"));
    }

    private void startConsoleListener(Kernel kernel, CountDownLatch latch) {
        final PrintStreamWrapper api = new PrintStreamWrapper(System.out);
        while (latch.getCount() > 0) {
            api.setIn(ConsoleIOUtils.request());
            kernel.receive(api);
        }
        System.out.println("shutdown yest");
    }
}
