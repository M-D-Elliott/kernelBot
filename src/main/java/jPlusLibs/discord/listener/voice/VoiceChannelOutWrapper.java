package jPlusLibs.discord.listener.voice;

import jPlus.io.out.IAPIWrapper;
import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable1;
import jPlusLibs.discord.DiscordConsoleUtils;
import jPlusLibs.discord.DiscordIOUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.io.File;

public class VoiceChannelOutWrapper implements IAPIWrapper {

    private final User user;
    private final MessageChannel reportingChannel;
    private final String voiceCommand;

    public VoiceChannelOutWrapper(User user,
                                  String voiceCommand) {
        this.user = user;
        this.reportingChannel = user.openPrivateChannel().complete();
        this.voiceCommand = voiceCommand;
    }

    @Override
    public Access access() {
        return Access.PRIVATE;
    }

    @Override
    public String username() {
        return user.getName();
    }

    @Override
    public Object payload() {
        return reportingChannel;
    }

    @Override
    public String in() {
        return voiceCommand;
    }

    @Override
    public Receivable1<String> out() {
        return System.out::println;
    }

    @Override
    public void print(String s) {
        DiscordConsoleUtils.print(reportingChannel, s);
    }

    @Override
    public void println(String s) {
        DiscordConsoleUtils.println(reportingChannel, s);
    }

    @Override
    public void printLink(String url) {
        DiscordConsoleUtils.printLink(reportingChannel, url);
    }

    @Override
    public void send(File f) {
        DiscordIOUtils.sendFile(reportingChannel, f);
    }

    @Override
    public void setStatus(String status) {

    }
}
