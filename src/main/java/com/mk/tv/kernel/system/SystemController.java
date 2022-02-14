package com.mk.tv.kernel.system;

import com.mk.tv.kernel.generic.CommandController;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.JarUtils;

import java.util.Collections;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateChar;

public class SystemController extends CommandController {

    public SystemController(Config config) {
        super(config);
    }

    //***************************************************************//

    @Override
    public void read(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        super.read(commandFuncMap);

        commandFuncMap.put("indicator", this::changeIndicator);
        commandFuncMap.put("version", this::speakVersion);
        Collections.addAll(menu, "indicator", "version");
    }

    //***************************************************************//

    @Override
    protected String entryPointName() {
        return "system";
    }

    @Override
    public char indicator() {
        return 'y';
    }

    @Override
    protected String menuPrefix() {
        return "System";
    }

    @Override
    protected String menuSuffix() {
        return "";
    }

    @Override
    protected String commandDesc(String item) {
        return "";
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

    //***************************************************************//

    public static final String ACTIVITY_RAW = "%scommand arg1 arg2";
    public static final String CHANGE_INDICATOR_HELP = "to change indicator to ! type: '%sindicator !'";
}
