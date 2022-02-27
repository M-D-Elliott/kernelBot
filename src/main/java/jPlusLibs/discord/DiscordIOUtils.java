package jPlusLibs.discord;

import jPlus.io.file.FileUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
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
}
