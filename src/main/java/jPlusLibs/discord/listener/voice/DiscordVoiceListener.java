package jPlusLibs.discord.listener.voice;

import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable1;
import jPlusLibs.discord.listener.RecipientListener;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DiscordVoiceListener extends RecipientListener {

    private Map<String, ? extends IVoiceUser> lastSpeechTime = new HashMap<>();

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
        final Message message = event.getMessage();
        final User author = message.getAuthor();
        final String content = message.getContentRaw();

        if (author.isBot() || !event.isFromGuild()) return;

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

        final DiscordVoiceStreamHandler handler = new DiscordVoiceStreamHandler();

        final AudioManager audioManager = channel.getGuild().getAudioManager();
        audioManager.setReceivingHandler(handler);
        audioManager.openAudioConnection(channel);
    }
}
