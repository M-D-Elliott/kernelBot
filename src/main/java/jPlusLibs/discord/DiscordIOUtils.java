package jPlusLibs.discord;

import jPlus.io.file.FileUtils;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.io.File;

import static jPlusLibs.discord.DiscordConsoleUtils.printUnf;

public class DiscordIOUtils {
    public static void sendFile(GenericMessageEvent e, File f) {
        if (f.exists())
            try {
                e.getChannel().sendMessage(FileUtils.simpleName(f)).addFile(f).queue();
            } catch (InsufficientPermissionException ignored) {
                printUnf(e, "I require permission... MESSAGE_EMBED_LINKS");
            }
        else printUnf(e, "File does not exist...");
    }
}
