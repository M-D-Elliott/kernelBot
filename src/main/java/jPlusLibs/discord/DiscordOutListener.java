package jPlusLibs.discord;

import jPlus.async.command.ThreadCommand;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable1;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiscordOutListener extends ListenerAdapter {

    private List<Receivable1<APIWrapper>> recipients = new ArrayList<>();

    public DiscordOutListener(Collection<Receivable1<APIWrapper>> recipients) {
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
        final APIWrapper wrapper = new ChannelOutWrapper(e);
        for (Receivable1<APIWrapper> recipient : recipients) recipient.receive(wrapper);
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        System.out.println("Shutting down...");
        ThreadCommand.terminateAllAndWait();
    }
}
