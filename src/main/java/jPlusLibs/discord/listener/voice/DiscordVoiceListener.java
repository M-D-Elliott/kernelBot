package jPlusLibs.discord.listener.voice;

import jPlus.io.in.IAPIWrapper;
import jPlus.io.out.IClientResponse;
import jPlus.lang.callback.Receivable1;
import jPlus.lang.callback.Retrievable1;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Collection;

import static jPlusLibs.com.edu.sphinx.SphinxUtils.disableSphinxLogs;

public class DiscordVoiceListener {

    private final DiscordVoiceStreamHandler handler;

    public DiscordVoiceListener(Retrievable1<IClientResponse, IAPIWrapper> recipient) {
        disableSphinxLogs();
        handler = new DiscordVoiceStreamHandler(recipient);
    }

    //***************************************************************//

    public void listenToVoiceChannel(IAPIWrapper api, String[] args) {
        final Object payload = api.payload();
        if (payload instanceof MessageReceivedEvent) {
            final MessageReceivedEvent e = (MessageReceivedEvent) payload;
            final Member member = e.getMember();
            if (member != null) {
                final GuildVoiceState voiceState = member.getVoiceState();
                if (voiceState != null) {
                    final VoiceChannel channel = voiceState.getChannel();
                    if (channel != null) {
                        connectTo(channel);
                        onConnecting(channel, e.getChannel());
                    } else onUnknownChannel(e.getChannel());
                }
            }
        }
    }

    private void connectTo(VoiceChannel channel) {
        final AudioManager audioManager = channel.getGuild().getAudioManager();

        audioManager.setReceivingHandler(handler);
        audioManager.openAudioConnection(channel);
    }

    private void onConnecting(VoiceChannel channel, MessageChannel messageChannel) {
        messageChannel.sendMessage("Connecting to " + channel.getName()).queue();
    }

    private void onUnknownChannel(MessageChannel channel) {
        channel.sendMessage("Unknown channel").queue();
    }
}
