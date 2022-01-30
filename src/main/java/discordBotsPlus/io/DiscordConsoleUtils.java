package discordBotsPlus.io;

import jPlus.util.lang.StringUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DiscordConsoleUtils {
    private static final String printFormat = "`%1$s`";

    public static void print(MessageReceivedEvent e, String message) {
        message = StringUtils.isNullWhiteSpaceOrEmpty(message) ? " " : message;
        e.getChannel().sendMessage(String.format(printFormat, message)).queue();
    }
}
