package com.mk.tv.entryPoints;

import com.mk.tv.auth.AuthConfig;
import com.mk.tv.auth.AuthKernel;
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
    private final AuthConfig config =
            JacksonUtils.readAndUpdateBliss("config.txt",
                    AuthConfig.class, AuthConfig::newInstance);

    private final Kernel kernel = new AuthKernel(config);

    public void run() {
        try {
            start();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void start() throws LoginException {

        kernel.init();

        startConsoleThread(kernel);

        final DiscordOutListener out = new DiscordOutListener(
                Collections.singletonList(kernel)
        );

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

    }

    protected void startConsoleThread(Kernel kernel) {
        final PrintStreamWrapper api = new PrintStreamWrapper(System.out);

        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            while (latch.getCount() > 0) {
                api.setIn(ConsoleIOUtils.request());
                kernel.receive(api);
            }
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown, "terminateConsoleOut"));
    }
}
