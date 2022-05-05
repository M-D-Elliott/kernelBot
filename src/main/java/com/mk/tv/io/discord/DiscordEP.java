package com.mk.tv.io.discord;

import com.mk.tv.io.discord.text.DiscordTextListener;
import com.mk.tv.kernel.Kernel;
import com.mk.tv.kernel.controllers.IFuncController;
import com.mk.tv.kernel.controllers.system.SystemController;
import com.mk.tv.kernel.generic.KernelGrammar;
import jPlus.io.file.DirUtils;
import jPlusLibs.com.edu.sphinx.SphinxUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.File;

public class DiscordEP {
    public static void main(Kernel kernel, char indicator, String token) {

        discordVoiceListener(kernel);

        try {
            JDABuilder.createDefault(token)
                    .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .enableCache(CacheFlag.ACTIVITY)
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.listening(
                            String.format(SystemController.ACTIVITY_RAW, indicator)))
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .enableCache(CacheFlag.VOICE_STATE)
                    .addEventListeners(new DiscordTextListener(kernel))
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    protected static void discordVoiceListener(Kernel kernel) {
        final String grammarPath = DirUtils.fromUserDir("repos" + File.separatorChar + "system");
        DirUtils.make(grammarPath);
        KernelGrammar.write(kernel, grammarPath);
        SphinxUtils.conf.setGrammarPath("file:" + grammarPath);

//        final DiscordVoiceListener discordVoiceOut = new DiscordVoiceListener(kernel);
//        kernel.getSyncFunctions().put("listen", discordVoiceOut::listenToVoiceChannel);
        kernel.getSyncFunctions().put("listen", (api, args) -> api.println("Sorry, this feature is not yet working quite right..."));
        final IFuncController systemController = kernel.getController(SystemController.class);
        if (systemController != null) systemController.menu().add("listen");
    }
}
