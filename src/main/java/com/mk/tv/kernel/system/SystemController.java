package com.mk.tv.kernel.system;

import com.mk.tv.kernel.generic.ICommandController;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.io.JarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateChar;
import static jPlus.util.io.ConsoleUtils.sep;

public class SystemController implements ICommandController {

    public static final String ACTIVITY_RAW = "%scommand arg1 arg2";
    public static final String CHANGE_INDICATOR_HELP = "to change indicator to ! type: '%sindicator !'";
    private final Config config;

    public final List<String> menu = new ArrayList<>();
    public final char indicator = 'y';

    public SystemController(Config config) {
        this.config = config;
    }

    //***************************************************************//

    @Override
    public void readCommands(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        commandFuncMap.put("system", this::processCommand);

        commandFuncMap.put("indicator", this::changeIndicator);
        commandFuncMap.put("version", this::speakVersion);
        Collections.addAll(menu, "indicator", "version");
    }

    @Override
    public String getMenuItem(int i) {
        return menu.get(i);
    }

    @Override
    public void processCommand(APIWrapper api, String[] args) {
        menuResponse(api, args);
    }

    @Override
    public boolean processCommand(String commandBody) {
        return false;
    }

    @Override
    public void menuResponse(APIWrapper api, String[] args) {
        final String prefix = sep() + "System" + sep();
        final String format = " %c%d  " + config.border + "        %s        ";
        api.print(prefix + ConsoleUtils.encaseInBanner(
                menu, config.border, (item, i) -> String.format(format, indicator, i, item)));
    }

    //***************************************************************//

    public void changeIndicator(APIWrapper api, String[] args) {
        if (validateChar(args, 1)) {
            config.commandIndicator = args[1].charAt(0);
            config.store();

            final String actString = String.format(ACTIVITY_RAW, config.commandIndicator);
            api.setStatus(actString);
        } else
            api.print(String.format(CHANGE_INDICATOR_HELP, config.commandIndicator));
    }

    public void speakVersion(APIWrapper api, String[] args) {
        String version = JarUtils.version();
        api.print(version == null ? "DEVELOPMENT" : version);
    }
}
