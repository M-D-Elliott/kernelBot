package jPlusLibs.discord.listener.text;

import jPlus.async.command.ThreadCommand;
import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable1;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiscordTextListener extends ListenerAdapter {

    protected final List<Receivable1<IAPIWrapper>> recipients = new ArrayList<>();

    public DiscordTextListener(Collection<Receivable1<IAPIWrapper>> recipients) {
        this.recipients.addAll(recipients);
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
        final IAPIWrapper wrapper = new TextChannelOutWrapper(e);
        for (Receivable1<IAPIWrapper> recipient : recipients) recipient.receive(wrapper);
    }
}
