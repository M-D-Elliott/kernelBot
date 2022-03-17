package jPlusLibs.discord;

import jPlus.io.file.FileUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.io.File;

import static jPlusLibs.discord.DiscordConsoleUtils.printUnf;

public class DiscordIOUtils {
    public static void sendFile(GenericMessageEvent e, File f) {
        sendFile(e.getChannel(), f);
    }

    public static void sendFile(MessageChannel channel, File f) {
        if (f.exists())
            try {
                channel.sendMessage(FileUtils.simpleName(f)).addFile(f).queue();
            } catch (InsufficientPermissionException ignored) {
                printUnf(channel, "I require permission... MESSAGE_EMBED_LINKS");
            }
        else printUnf(channel, "File does not exist...");
    }

    public static void whisper(MessageReceivedEvent e, String meesage) {
        whisper(e.getAuthor(), meesage);
    }

    public static void whisper(User user, String message) {
        final MessageChannel channel = user.openPrivateChannel().complete();
        DiscordConsoleUtils.print(channel, message);
    }

    public static void whisper(MessageReceivedEvent e, String username, String message) {
        if (e.getChannel() instanceof PrivateChannel) {
            DiscordConsoleUtils.print(e, "Only in guild channel...");
            return;
        }
        final Thread th = new Thread(() -> {
            final User user = findFirstMember(e.getGuild(), username).getUser();
            whisper(user, message);
        });
        th.setDaemon(true);
        th.start();
    }

    //***************************************************************//

    private static Member findFirstMember(Guild guild, String username) {
        final int len = username.length();
        return guild.findMembers(member -> {
            final String currUsername = member.getUser().getName();
            return len == currUsername.length() && username.equals(currUsername);
        }).get().get(0);
    }
}
