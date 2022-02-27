package jPlusLibs.discord.listener.text;

import jPlus.io.out.IAPIWrapper;
import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable1;
import jPlusLibs.discord.DiscordConsoleUtils;
import jPlusLibs.discord.DiscordIOUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;

public class TextChannelOutWrapper implements IAPIWrapper {
    private final MessageReceivedEvent e;
    private final Access access;

    private final Receivable1<String> out = this::print;

    public TextChannelOutWrapper(MessageReceivedEvent e) {
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
        DiscordConsoleUtils.println(e, s);
    }

    @Override
    public void printLink(String url) {
        DiscordConsoleUtils.printLink(e, url);
    }

    @Override
    public void send(File f) {
        DiscordIOUtils.sendFile(e, f);
    }

    @Override
    public void setStatus(String status) {
        final Activity activity = Activity.listening(status);
        e.getJDA().getPresence().setActivity(activity);
    }

    @Override
    public Receivable1<String> out() {
        return out;
    }

    @Override
    public Access access() {
        return access;
    }

    @Override
    public String username() {
        return e.getAuthor().getName();
    }

    @Override
    public Object payload() {
        return e;
    }
}
