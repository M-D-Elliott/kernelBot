package com.mk.tv.io.discord.text;

import com.mk.tv.io.generic.IAPIWrapper;
import com.mk.tv.io.generic.IClientResponse;
import jPlus.lang.callback.Retrievable1;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordTextListener extends ListenerAdapter {

    protected final  Retrievable1<IClientResponse, IAPIWrapper> recipient;

    public DiscordTextListener(Retrievable1<IClientResponse, IAPIWrapper> recipient) {
        this.recipient = recipient;
    }

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        super.onReady(e);
        System.out.println(e.getJDA().getSelfUser().getName() + " bot online.");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) onUserMessageReceived(e);
    }

    public void onUserMessageReceived(MessageReceivedEvent e) {
        final IAPIWrapper wrapper = new DiscordTextIOWrapper(e);
        recipient.retrieve(wrapper);
    }
}
