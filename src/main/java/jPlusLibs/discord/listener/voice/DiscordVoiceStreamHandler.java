package jPlusLibs.discord.listener.voice;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static jPlus.util.io.ConsoleUtils.encaseInBanner;
import static jPlusLibs.discord.listener.voice.DiscordAudioUtils.audioToTextBliss;

public class DiscordVoiceStreamHandler implements AudioReceiveHandler {

    private final int segmentDuration = (int) TimeUnit.of(ChronoUnit.SECONDS).toMillis(3);
    private final int maxSegmentLength = segmentDuration / 20;
    private static final int BYTES_PER_SEGMENT = 3840;
    private final byte[] building = new byte[maxSegmentLength * BYTES_PER_SEGMENT];
    private int segmentIndex = 0;
    private int audioFileCount = 0;

    @Override
    public boolean canReceiveUser() {
        return segmentIndex < maxSegmentLength;
    }

    @Override
    public void handleUserAudio(UserAudio userAudio) {

        byte[] data = userAudio.getAudioData(1.0f);

        DiscordAudioUtils.copyBytes(data, building, BYTES_PER_SEGMENT * segmentIndex++);

        if (!canReceiveUser()) {
            System.out.println(encaseInBanner("R E S U L T  " + audioFileCount++));

            System.out.println(audioToTextBliss(building));

            segmentIndex = 0;
        }
    }

}
