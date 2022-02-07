package com.mk.tv.kernel.scripts;

import com.mk.tv.kernel.generic.IRepoCommandController;
import com.mk.tv.kernel.system.Config;
import com.mk.tv.utils.ApacheFileUtils;
import jPlus.io.APIWrapper;
import jPlus.io.file.FileUtils;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.io.RuntimeUtils;

import java.io.File;
import java.util.*;

import static jPlus.util.io.ConsoleIOUtils.NOT_YET_IMPLEMENTED;
import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.ConsoleUtils.sep;

public class ScriptController implements IRepoCommandController {
    public static final String SUCCESSFUL_SCRIPT = "--script--%s--%s!";

    public final Map<String, String> pathMap = new HashMap<>();

    private final List<String> menu = new ArrayList<>();
    public final char indicator = 's';

    private final Config config;

    public ScriptController(Config config) {
        this.config = config;
    }

    @Override
    public void readCommands(
            Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        commandFuncMap.put("addScript", this::addCommand);
        commandFuncMap.put("script", this::processCommand);

        final Collection<File> scriptFiles = ApacheFileUtils.getRecursiveFiles(
                new File(System.getProperty("user.dir") + File.separator + "myscripts"),
                config.formats);
        scriptFiles.removeIf(File::isHidden);

        final Receivable2<APIWrapper, String[]> scriptCommand = this::processRepoCommand;

        for (File file : scriptFiles) {
            final String simpleName = FileUtils.simpleName(file);
            commandFuncMap.put(simpleName, scriptCommand);
            pathMap.put(simpleName, file.getAbsolutePath());
        }

        menu.addAll(pathMap.keySet());
    }

    @Override
    public char indicator() {
        return indicator;
    }

    @Override
    public String getMenuItem(int i) {
        return menu.get(i);

    }

    public void processCommand(APIWrapper api, String[] args) {
        if (validateString(args, 1) && processCommand(args[1])) return;
        menuResponse(api, args);
    }

    public boolean processCommand(String commandBody) {
        return RuntimeUtils.batch(commandBody);
    }

    @Override
    public void menuResponse(APIWrapper api, String[] args) {
        final String sep = sep();

        final String prefix = sep + "SCRIPT PRESETS" + sep;
        final String suffix = sep + config.displayLiteralCommand("addscript scriptName scriptCode") + " -- adds a new script preset!";

        final String format = " %c%d  " + config.border + "        %s        ";
        api.print(prefix + ConsoleUtils.encaseInBanner(
                menu, config.border, (item, i) -> String.format(format, indicator, i, item)) + suffix);
    }

    @Override
    public List<String> menu() {
        return menu;
    }

    @Override
    public void processRepoCommand(APIWrapper api, String[] args) {
        final String commandPath = pathMap.get(args[0]);
        final boolean success = processCommand(commandPath);
        api.print(String.format(SUCCESSFUL_SCRIPT, args[0],
                success ? "success" : "failed"));
    }

    @Override
    public void addCommand(APIWrapper api, String[] args) {
        api.print(NOT_YET_IMPLEMENTED);
    }
}
