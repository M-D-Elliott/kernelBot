package com.mk.tv.kernel.generic;

import com.mk.tv.kernel.Config;
import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleUtils.sep;


public abstract class FuncController implements IFuncController {
    protected final List<String> menu = new ArrayList<>();
    protected Config config;

    public FuncController(Config config) {
        this.config = config;
    }

    //***************************************************************//

    @Override
    public void read(Map<String, Receivable2<IAPIWrapper, String[]>> sync,
                     Map<String, Receivable2<IAPIWrapper, String[]>> async) {
        sync.put(menuName(), this::process);
    }

    //***************************************************************//

    protected void process(IAPIWrapper api, String[] args) {
        menuResponse(api, args);
    }

    protected void menuResponse(IAPIWrapper api, String[] args) {
        final String format = " %c%d  " + config.system.getMenuBorder() + "        %s%s        ";
        final String[] menuFormatted = new String[menu.size()];
        for (int i = 0; i < menuFormatted.length; i++) {
            final String item = menu.get(i);
            menuFormatted[i] = String.format(format, indicator(), i, item, funcDesc(item));
        }
        final String body = ConsoleUtils.encaseInBanner(menuFormatted, config.system.getMenuBorder());

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

    protected abstract String funcDesc(String item);
}