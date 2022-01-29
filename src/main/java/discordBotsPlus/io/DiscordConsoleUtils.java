package discordBotsPlus.io;

import jPlus.lang.callback.Retrievable2;
import jPlus.util.lang.StringUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;

import static jPlus.util.io.ConsoleUtils.encaseInBanner;
import static jPlus.util.io.ConsoleUtils.sep;

public class DiscordConsoleUtils {
    private static final String printFormat = "`%1$s`";

    public static void print(MessageReceivedEvent e, String message) {
        message = StringUtils.isNullWhiteSpaceOrEmpty(message) ? " " : message;
        e.getChannel().sendMessage(String.format(printFormat, message)).queue();
    }

    public static void printMenu(MessageReceivedEvent e,
                                 Collection<String> items,
                                 Retrievable2<String, String, Integer> itemFormatter) {
        printMenu(e, items, itemFormatter, new StringBuilder());
    }

    public static void printMenu(MessageReceivedEvent e,
                                 Collection<String> items,
                                 Retrievable2<String, String, Integer> itemFormatter,
                                 StringBuilder builder) {
        int index = 1;
        for (String item : items) builder.append(itemFormatter.retrieve(item, index++));
        print(e, builder.toString());
    }

    public static void printMenu(MessageReceivedEvent e,
                                 Collection<String> items,
                                 Retrievable2<String, String, Integer> itemFormatter,
                                 StringBuilder builder,
                                 char border) {
        int index = 1;
        for (String item : items) builder.append(itemFormatter.retrieve(item, index++));
        print(e, sep() + encaseInBanner(builder.toString(), border));
    }

    public static void printMenu(MessageReceivedEvent e,
                                 Collection<String> items,
                                 Retrievable2<String, String, Integer> itemFormatter,
                                 char border) {
        print(e, sep() + encaseInBanner(items, border, itemFormatter));
    }
}
