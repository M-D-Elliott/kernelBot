package jPlusLibs.discord;

import jPlus.util.lang.StringUtils;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.jetbrains.annotations.NotNull;

import static jPlus.util.io.ConsoleUtils.encase;

public class DiscordConsoleUtils {

    //TODO bother discord until they make simple link embeds that can be in public channels too.
    // 13 parameters? Jee wiz!
    //private static final String ALIASED_LINK_UNF = "[%1$s](%2$s)";

    //***************************************************************//

    public static void printUnf(GenericMessageEvent e, String message) {
        e.getChannel().sendMessage(message).queue();
    }

    public static void print(GenericMessageEvent e, String message) {
        printUnf(e, monospaced(message));
    }

    public static void println(GenericMessageEvent e, String message) {
        print(e, message + System.lineSeparator());
    }

    public static void printLink(GenericMessageEvent e, String url) {
        printUnf(e, "<" + url + ">");
    }

    //***************************************************************//

    @NotNull
    public static String monospaced(String message) {
        message = StringUtils.isNullWhiteSpaceOrEmpty(message) ? "` `" : encase(message, '`');
        return message;
    }
}
