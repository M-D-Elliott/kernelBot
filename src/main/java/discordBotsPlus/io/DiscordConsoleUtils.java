package discordBotsPlus.io;

import jPlus.util.lang.StringUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static jPlus.util.io.ConsoleUtils.encase;

public class DiscordConsoleUtils {
    public static void print(MessageReceivedEvent e, String message) {
        message = StringUtils.isNullWhiteSpaceOrEmpty(message) ? "` `" : encase(message, '`');
        e.getChannel().sendMessage(message).queue();
    }
}
