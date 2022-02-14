package com.mk.tv.kernel.scripts;

import com.mk.tv.kernel.generic.RepoCommandController;
import com.mk.tv.kernel.system.Config;
import jPlusLibs.apache.commons.ApacheFileUtils;
import jPlus.io.APIWrapper;
import jPlus.io.file.FileUtils;
import jPlus.util.io.RuntimeUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static jPlus.util.io.ConsoleIOUtils.validateString;

public class ScriptController extends RepoCommandController {

    public final Map<String, String> pathMap = new HashMap<>();

    public ScriptController(Config config) {
        super(config);
    }

    //***************************************************************//

    @Override
    protected void processCommand(APIWrapper api, String[] args) {
        if (validateString(args, 1) && processCommand(args[1])) return;
        super.processCommand(api, args);
    }

    private boolean processCommand(String commandBody) {
        return RuntimeUtils.batch(commandBody);
    }

    @Override
    protected void processRepoCommand(APIWrapper api, String[] args) {
        final String commandPath = pathMap.get(args[0]);
        final boolean success = processCommand(commandPath);
        api.print(String.format(SUCCESSFUL_SCRIPT, args[0],
                success ? "success" : "failed"));
    }

    //***************************************************************//

    @Override
    protected String entryPointName() {
        return "script";
    }

    @Override
    public char indicator() {
        return 's';
    }

    @Override
    protected Collection<String> commandNames() {
        return ApacheFileUtils.getRecursiveFiles(
                new File(System.getProperty("user.dir") + File.separator + "myscripts"),
                config.scriptFormats)
                .stream()
                .filter(f -> !f.isHidden())
                .map(f -> {
                    final String simpleName = FileUtils.simpleName(f);
                    pathMap.put(simpleName, f.getAbsolutePath());
                    return simpleName;
                }).collect(Collectors.toList());
    }

    @Override
    protected String commandDesc(String item) {
        return "";
    }

    @Override
    protected String menuPrefix() {
        return "SCRIPT PRESETS";
    }

    @Override
    protected String menuSuffix() {
        return config.displayLiteralCommand("addscript scriptName scriptCode") + " -- adds a new script preset!";
    }

    //***************************************************************//

    public static final String SUCCESSFUL_SCRIPT = "--script--%s--%s!";
}
