package jPlusLibs.discord.listener.voice;

import jPlus.async.Threads;
import jPlus.async.command.LoopThreadCommand;
import jPlus.io.in.IAPIWrapper;
import jPlus.io.out.IClientResponse;
import jPlus.io.out.Resolution;
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
    protected final Map<String, VoiceSession> voiceSessions = new HashMap<>();

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

        final VoiceSession session = getVoiceSession(user, userName);

        session.store(userAudio.getAudioData(1.0f));

        if (session.check()) {
            final String voiceCommand = session.result();
            System.out.println(voiceCommand);
            final IAPIWrapper api = new VoiceChannelOutWrapper(user, voiceCommand);
            final IClientResponse response = recipient.retrieve(api);

            if (response.resolution() == Resolution.SUCCESS || session.isFull()) {
                System.out.println("Success end");
                session.end();
            }
        }
    }

    @NotNull
    protected VoiceSession getVoiceSession(User user, String userName) {
        VoiceSession ret = voiceSessions.get(userName);
        if (ret == null) {
            ret = new VoiceSession(user);
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
                for (VoiceSession session : voiceSessions.values()) {
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
