package jPlusLibs.discord.listener;

import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable1;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecipientListener extends ListenerAdapter {
    protected final List<Receivable1<IAPIWrapper>> recipients = new ArrayList<>();

    public RecipientListener(Collection<Receivable1<IAPIWrapper>> recipients) {
        this.recipients.addAll(recipients);
    }
}
