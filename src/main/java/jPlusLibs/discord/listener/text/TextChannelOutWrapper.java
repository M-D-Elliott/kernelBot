package jPlusLibs.discord.listener.text;

import jPlus.io.in.IAPIWrapper;
import jPlus.io.in.IIOWrapper;
import jPlus.io.in.Priority;
import jPlus.io.out.Access;
import jPlus.lang.callback.Receivable1;
import jPlusLibs.discord.DiscordConsoleUtils;
import jPlusLibs.discord.DiscordIOUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
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
    public IIOWrapper setPriority(Priority priority) {
        return this;
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

    @Override
    public void send(String endpoint, String message) {
        DiscordIOUtils.whisper(e, endpoint, message);
    }
}
