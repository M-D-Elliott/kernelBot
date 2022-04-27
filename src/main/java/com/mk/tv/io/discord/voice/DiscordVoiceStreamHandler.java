package com.mk.tv.io.discord.voice;

import jPlus.async.Threads;
import jPlus.async.command.LoopThreadCommand;
import com.mk.tv.io.generic.IAPIWrapper;
import com.mk.tv.io.generic.IClientResponse;
import jPlus.io.Resolution;
import jPlus.lang.callback.Retrievable1;
import jPlusLibs.com.edu.sphinx.SphinxUtils;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DiscordVoiceStreamHandler implements AudioReceiveHandler {

    protected final Retrievable1<IClientResponse, IAPIWrapper> recipient;
    protected final Map<String, DiscordVoiceSession> voiceSessions = new HashMap<>();

    public DiscordVoiceStreamHandler(Retrievable1<IClientResponse, IAPIWrapper> recipient) {
        this.recipient = recipient;
        SphinxUtils.conf.setSampleRate((int) AudioReceiveHandler.OUTPUT_FORMAT.getSampleRate());
        startSnippingCommand();
    }

    @Override
    public boolean canReceiveUser() {
        return true;
    }

    @Override
    public void handleUserAudio(UserAudio userAudio) {

        final User user = userAudio.getUser();
        final String userName = user.getName();

        final DiscordVoiceSession session = getVoiceSession(user, userName);

        session.store(userAudio.getAudioData(1.0f));

        if (session.check()) {
            final String voiceCommand = session.result();
            System.out.println(voiceCommand);
            final IAPIWrapper api = new DiscordVoiceIOWrapper(user, voiceCommand);
            final IClientResponse response = recipient.retrieve(api);

            if (response.resolution() == Resolution.SUCCESS || session.isFull()) {
                System.out.println("Success end");
                session.end();
            }
        }
    }

    @NotNull
    protected DiscordVoiceSession getVoiceSession(User user, String userName) {
        DiscordVoiceSession ret = voiceSessions.get(userName);
        if (ret == null) {
            ret = new DiscordVoiceSession(user);
            voiceSessions.put(userName, ret);
        }
        return ret;
    }

    private void startSnippingCommand() {
        new LoopThreadCommand() {
            @Override
            protected boolean condition() {
                return true;
            }

            @Override
            protected void loopBody() {
                long nextExpr = 100;
                for (DiscordVoiceSession session : voiceSessions.values()) {
                    if (session.isActive()) {
                        final long expiresIn = session.expiration - System.currentTimeMillis();
                        if (expiresIn <= 0) {
                            System.out.println("expiration");
                            session.end();
                        }
                        else nextExpr = Math.min(expiresIn, nextExpr);
                    }
                }
                Threads.sleepBliss((int) Math.max(nextExpr, 20));
            }
        }.run();
    }

}
