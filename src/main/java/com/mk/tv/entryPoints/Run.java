package com.mk.tv.entryPoints;

import com.mk.tv.auth.AuthKernel;
import com.mk.tv.auth.config.AuthConfig;
import com.mk.tv.kernel.system.SystemController;
import jPlusLibs.jackson.JacksonUtils;
import jPlusLibs.discord.DiscordOutListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Collections;

public class Run implements Runnable {
    private final AuthConfig config =
            JacksonUtils.readAndUpdateBliss("config.txt",
                    AuthConfig.class, AuthConfig::newInstance);

    private final AuthKernel kernel = new AuthKernel(config);

    public void run() {
        try {
            start();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void start() throws LoginException {
        kernel.init();

        final DiscordOutListener out = new DiscordOutListener(
                Collections.singletonList(kernel));

        JDABuilder.createDefault(config.token)
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                .enableCache(CacheFlag.ACTIVITY)
                .setBulkDeleteSplittingEnabled(false)
                .setCompression(Compression.NONE)
                .setActivity(Activity.listening(
                        String.format(SystemController.ACTIVITY_RAW, config.commandIndicator)))
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .addEventListeners(out)
                .build();
    }
}
