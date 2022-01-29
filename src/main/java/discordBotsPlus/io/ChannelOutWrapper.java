package discordBotsPlus.io;

import jPlus.io.APIWrapper;
import jPlus.io.security.Access;
import jPlus.lang.callback.Retrievable2;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;

public class ChannelOutWrapper implements APIWrapper {
    private final MessageReceivedEvent e;
    private final Access access;

    public ChannelOutWrapper(MessageReceivedEvent e) {
        this.e = e;
        access = (e.getChannel() instanceof PrivateChannel)
                ? Access.PRIVATE : Access.PUBLIC;
    }

    @Override
    public String in() {
        return e.getMessage().getContentRaw();
    }

    @Override
    public void print(String s) {
        DiscordConsoleUtils.print(e, s);
    }

    @Override
    public void println(String s) {
        DiscordConsoleUtils.print(e, s);
    }

    @Override
    public void printMenu(Collection<String> items,
                          Retrievable2<String, String, Integer> itemFormatter,
                          StringBuilder builder) {
        DiscordConsoleUtils.printMenu(e, items, itemFormatter, builder);
    }

    @Override
    public void printMenu(Collection<String> items, Retrievable2<String, String, Integer> itemFormatter, StringBuilder builder, char border) {
        DiscordConsoleUtils.printMenu(e, items, itemFormatter, builder, border);
    }

    @Override
    public void setStatus(String status) {
        final Activity listeningOnIndicator = Activity.listening(status);
        e.getJDA().getPresence().setActivity(listeningOnIndicator);
    }

    @Override
    public Access access() {
        return access;
    }

    @Override
    public String username() {
        return e.getAuthor().getName();
    }
}
