package jPlusLibs.discord.listener.voice;

import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable1;
import jPlusLibs.com.edu.sphinx.SphinxUtils;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static jPlus.util.io.ConsoleUtils.encaseInBanner;
import static jPlusLibs.com.edu.sphinx.SphinxUtils.audioToTextBliss;

public class DiscordVoiceStreamHandler implements AudioReceiveHandler {

    protected final Collection<Receivable1<IAPIWrapper>> recipients;

    protected final Map<String, VoiceSession> voiceSessions = new HashMap<>();

    protected int receivedCount = 0;

    private long timeSinceLast = System.currentTimeMillis();

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

        final String userName = userAudio.getUser().getName();

        VoiceSession session = voiceSessions.get(userName);
        if (session == null) {
            session = new VoiceSession();
            voiceSessions.put(userName, session);
        }
        session.store(userAudio.getAudioData(1.0f));

        if (session.isFull()) {
            System.out.println(encaseInBanner("R E S U L T  " + receivedCount++));
            final String voiceCommand = audioToTextBliss(session.read(), AudioReceiveHandler.OUTPUT_FORMAT);
            System.out.println(voiceCommand);
            final IAPIWrapper api = new VoiceChannelOutWrapper(userAudio.getUser(), voiceCommand);
            recipients.forEach(r -> r.receive(api));

            final long currTime = System.currentTimeMillis();
            System.out.println(currTime - timeSinceLast);
            timeSinceLast = currTime;
        }
    }
}
