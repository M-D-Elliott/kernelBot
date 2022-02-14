package com.mk.tv.kernel.scripts;

import com.mk.tv.kernel.generic.CommandController;
import com.mk.tv.kernel.generic.CommandService;
import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.io.file.FileUtils;
import jPlus.lang.callback.Receivable2;
import jPlusLibs.apache.commons.ApacheFileUtils;
import jPlusLibs.generic.IRepo;
import jPlusLibs.generic.MapRepoS;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.RuntimeUtils.cmdStartString;
import static jPlus.util.io.RuntimeUtils.execBliss;

public class ScriptController extends CommandController {

    protected final CommandService<String> service;

    public ScriptController(Config config) {
        super(config);

        final IRepo<String> repo = new MapRepoS<>(pathMap()) {
        };
        service = new CommandService<>(repo) {
            @Override
            protected void process(APIWrapper api, String[] args) {
                final String commandPath = repo.get(args[0]);
                final boolean success = ScriptController.this.process(commandPath);
                api.print(String.format(SUCCESSFUL_SCRIPT, args[0],
                        success ? "success" : "failed"));
            }
        };
    }

    protected Map<String, String> pathMap() {
        return ApacheFileUtils.getRecursiveFiles(
                new File(System.getProperty("user.dir") + File.separator + "myscripts"),
                config.scriptFormats)
                .stream()
                .filter(f -> !f.isHidden())
                .collect(Collectors.toMap(FileUtils::simpleName, File::getAbsolutePath));
    }

    @Override
    public void read(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        super.read(commandFuncMap);
        service.read(commandFuncMap, entryPointName(), menu);
    }

    //***************************************************************//

    @Override
    protected void process(APIWrapper api, String[] args) {
        if (validateString(args, 1) && process(args[1])) return;
        super.process(api, args);
    }

    protected boolean process(String commandBody) {
        return execBliss(cmdStartString(commandBody));
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
