package com.mk.tv.kernel.generic;

import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleUtils.sep;


public abstract class CommandController implements ICommandController {
    protected final List<String> menu = new ArrayList<>();
    protected Config config;

    public CommandController(Config config) {
        this.config = config;
    }

    //***************************************************************//

    @Override
    public void read(Map<String, Receivable2<APIWrapper, String[]>> sync,
                     Map<String, Receivable2<APIWrapper, String[]>> async) {
        sync.put(entryPointName(), this::process);
    }

    //***************************************************************//

    protected void process(APIWrapper api, String[] args) {
        menuResponse(api, args);
    }

    protected void menuResponse(APIWrapper api, String[] args) {
        final String format = " %c%d  " + config.menuBorder + "        %s%s        ";
        final String[] menuFormatted = new String[menu.size()];
        for (int i = 0; i < menuFormatted.length; i++) {
            final String item = menu.get(i);
            menuFormatted[i] = String.format(format, indicator(), i, item, commandDesc(item));
        }
        final String body = ConsoleUtils.encaseInBanner(menuFormatted, config.menuBorder);

        final String sep = sep();
        api.print(sep + menuPrefix() + sep + body + sep + menuSuffix());
    }

    //***************************************************************//

    @Override
    public List<String> menu() {
        return menu;
    }

    protected abstract String menuPrefix();

    protected abstract String menuSuffix();

    protected abstract String commandDesc(String item);
}
