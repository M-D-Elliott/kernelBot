package com.mk.tv.kernel.controllers.system;

import com.mk.tv.kernel.generic.Config;
import com.mk.tv.kernel.controllers.FuncController;
import jPlus.io.file.DirUtils;
import jPlus.io.file.FileUtils;
import com.mk.tv.io.generic.IAPIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.JarUtils;

import java.util.Collections;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateChar;
import static jPlus.util.io.ConsoleUtils.sep;

public class SystemController extends FuncController {

    public SystemController(Config config) {
        super(config);
    }

    //***************************************************************//

    @Override
    public void read(Map<String, Receivable2<IAPIWrapper, String[]>> sync,
                     Map<String, Receivable2<IAPIWrapper, String[]>> async) {
        super.read(sync, async);

        sync.put("indicator", this::changeIndicator);
        sync.put("version", this::printVersion);
        sync.put("splash", this::printSplash);
        Collections.addAll(menu, "indicator", "version", "splash");
    }

    //***************************************************************//

    @Override
    public String menuName() {
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
    protected String funcDesc(String item) {
        return "";
    }

    //***************************************************************//

    public void changeIndicator(IAPIWrapper api, String[] args) {
        if (validateChar(args, 1)) {
            config.system.commandIndicator = args[1].charAt(0);
            config.store();

            final String actString = String.format(ACTIVITY_RAW, config.system.commandIndicator);
            api.setStatus(actString);
        } else
            api.print(String.format(CHANGE_INDICATOR_HELP, config.system.commandIndicator));
    }

    public void printVersion(IAPIWrapper api, String[] args) {
        String version = JarUtils.version();
        api.print(version);
    }

    public void printSplash(IAPIWrapper api, String[] args) {
        final String sep = sep();
        final String splash = String.join(sep, FileUtils.read(DirUtils.fromUserDir("repos/splash.txt")));
        api.println(sep + splash);
        api.printLink(INSTALL_URL);
    }

    //***************************************************************//

    public static final String ACTIVITY_RAW = "%sfunction [args]";
    public static final String CHANGE_INDICATOR_HELP = "to change indicator to ! type: '%sindicator !'";
    public static final String INSTALL_URL = "https://github.com/M-D-Elliott/kernelBot/";
}
