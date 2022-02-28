package jPlusLibs.discord.listener.voice;

import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable1;
import jPlusLibs.com.edu.sphinx.SphinxUtils;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DiscordVoiceStreamHandler implements AudioReceiveHandler {

    protected final Collection<Receivable1<IAPIWrapper>> recipients;
    protected final Map<String, VoiceSession> voiceSessions = new HashMap<>();

    public DiscordVoiceStreamHandler(Collection<Receivable1<IAPIWrapper>> recipients) {
        this.recipients = recipients;
        SphinxUtils.conf.setSampleRate((int) AudioReceiveHandler.OUTPUT_FORMAT.getSampleRate());
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

        if (session.isFull()) session.end();
        else session.waitAndEnd();

    }

    @NotNull
    protected VoiceSession getVoiceSession(User user, String userName) {
        VoiceSession ret = voiceSessions.get(userName);
        if (ret == null) {
            ret = new VoiceSession(user);
            voiceSessions.put(userName, ret);
            ret.setOnEnd((api) -> {
                recipients.forEach(r -> r.receive(api));
            });
        }
        return ret;
    }
}
