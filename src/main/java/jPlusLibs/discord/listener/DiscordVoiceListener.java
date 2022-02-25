package jPlusLibs.discord.listener;

import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable1;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static jPlus.util.io.ConsoleUtils.encaseInBanner;
import static jPlusLibs.discord.listener.DiscordAudioUtils.audioToTextBliss;

public class DiscordVoiceListener extends RecipientListener {

    private Map<String, Integer> lastSpeech = new HashMap<>();

    public DiscordVoiceListener(Collection<Receivable1<IAPIWrapper>> recipients) {
        super(recipients);
        disableLogs();
    }

    private void disableLogs() {
        String conFile = System.getProperty("java.util.logging.config.file");
        if (conFile == null)
            System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        User author = message.getAuthor();
        String content = message.getContentRaw();

        if (author.isBot()) return;

        if (!event.isFromGuild()) return;

        if (content.equals("!echo")) onEchoCommand(event);
    }

    private void onEchoCommand(MessageReceivedEvent event) {

        final Member member = event.getMember();
        if (member != null) {
            final GuildVoiceState voiceState = member.getVoiceState();
            if (voiceState != null) {
                final VoiceChannel channel = voiceState.getChannel();
                if (channel != null) {
                    connectTo(channel);
                    onConnecting(channel, event.getChannel());
                } else {
                    onUnknownChannel(event.getChannel());
                }
            }
        }
    }

    private void onConnecting(VoiceChannel channel, MessageChannel messageChannel) {
        messageChannel.sendMessage("Connecting to " + channel.getName()).queue();
    }

    private void onUnknownChannel(MessageChannel channel) {
        channel.sendMessage("Unknown channel").queue();
    }

    private void connectTo(VoiceChannel channel) {
        Guild guild = channel.getGuild();

        AudioManager audioManager = guild.getAudioManager();

        EchoHandler handler = new EchoHandler();

        audioManager.setReceivingHandler(handler);

        audioManager.openAudioConnection(channel);
    }


    public static class EchoHandler implements AudioReceiveHandler {

        private final int segmentDuration = (int) TimeUnit.of(ChronoUnit.SECONDS).toMillis(3);
        private final int maxSegmentLength = segmentDuration / 20;
        private static final int BYTES_PER_SEGMENT = 3840;
        private final byte[] building = new byte[maxSegmentLength * BYTES_PER_SEGMENT];
        private int segmentIndex = 0;
        private int audioFileCount = 0;

//        private final File waveFile = new File("repos/audiotests/test.wav");

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

//                final File waveFile = new File("repos/audiotests/test" + (audioFileCount++) + ".wav");

//                saveAudioBliss(waveFile, building);
//                System.out.println(audioToTextBliss(waveFile));

                segmentIndex = 0;
            }
        }

    }
}
