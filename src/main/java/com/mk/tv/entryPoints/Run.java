package com.mk.tv.entryPoints;

import com.mk.tv.auth.AuthConfig;
import com.mk.tv.auth.AuthKernel;
import com.mk.tv.kernel.Config;
import com.mk.tv.kernel.Kernel;
import com.mk.tv.kernel.KernelGrammar;
import com.mk.tv.kernel.generic.IFuncController;
import com.mk.tv.kernel.system.SystemController;
import jPlus.io.out.IAPIWrapper;
import jPlus.io.out.PrintStreamWrapper;
import jPlus.lang.callback.Receivable1;
import jPlus.util.io.ConsoleIOUtils;
import jPlusLibs.discord.listener.text.DiscordTextListener;
import jPlusLibs.discord.listener.voice.DiscordVoiceListener;
import jPlusLibs.jackson.JacksonUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collection;
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
            writeKernelFuncsToGrammar(kernel);
            if (config.system.listenToConsole) startConsoleThread(kernel);
            discordListener(kernel, config);
        } else if (config.system.listenToConsole)
            consoleListener(kernel, new CountDownLatch(1));
    }

    private void discordListener(Kernel kernel, Config config) {

        final Collection<Receivable1<IAPIWrapper>> recipients = Collections.singletonList(kernel);

        discordVoiceListener(kernel, recipients);

        try {
            JDABuilder.createDefault(config.system.token)
                    .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .enableCache(CacheFlag.ACTIVITY)
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.listening(
                            String.format(SystemController.ACTIVITY_RAW, config.system.commandIndicator)))
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .enableCache(CacheFlag.VOICE_STATE)
                    .addEventListeners(new DiscordTextListener(recipients))
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void discordVoiceListener(Kernel kernel, Collection<Receivable1<IAPIWrapper>> recipients) {
        final DiscordVoiceListener discordVoiceOut = new DiscordVoiceListener(recipients);
        kernel.getSyncFunctions().put("listen", discordVoiceOut::startVoice);
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
            kernel.receive(api);
        }
    }

    private void writeKernelFuncsToGrammar(Kernel kernel) {
        final Collection<String> functions = new ArrayList<>();
        functions.addAll(kernel.getAsyncFunctions().keySet());
        functions.addAll(kernel.getSyncFunctions().keySet());
        KernelGrammar.write(new String[]{"kay bee", "kernel bot"}, functions.toArray(new String[0]));
    }
}
