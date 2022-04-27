package com.mk.tv.kernel.controllers;

import com.mk.tv.kernel.generic.Config;
import com.mk.tv.io.generic.IAPIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleUtils.sep;


public abstract class FuncController implements IFuncController {
    public final List<String> menu = new ArrayList<>();
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

    @Override
    public List<String> menu() {
        return menu;
    }

    //***************************************************************//

    protected void process(IAPIWrapper api, String[] args) {
        menuResponse(api, args);
    }

    protected void menuResponse(IAPIWrapper api, String[] args) {
        final String format = " %c%d  " + menuBorder() + "        %s%s        ";
        final String[] menuFormatted = new String[menu.size()];
        for (int i = 0; i < menuFormatted.length; i++) {
            final String item = menu.get(i);
            menuFormatted[i] = String.format(format, indicator(), i, item, funcDesc(item));
        }
        final String body = ConsoleUtils.encaseInBanner(menuFormatted, menuBorder());

        final String sep = sep();
        api.print(sep + menuPrefix() + sep + body + sep + menuSuffix());
    }

    protected String menuBorder() {
        return config.system.getMenuBorder();
    }

    //***************************************************************//

    protected abstract String menuPrefix();

    protected abstract String menuSuffix();

    protected abstract String funcDesc(String item);
}
