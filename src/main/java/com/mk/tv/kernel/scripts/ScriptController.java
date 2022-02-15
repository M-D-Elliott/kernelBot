package com.mk.tv.kernel.scripts;

import com.mk.tv.kernel.Config;
import com.mk.tv.kernel.generic.FuncController;
import com.mk.tv.kernel.generic.FuncService;
import jPlus.io.file.FileUtils;
import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlusLibs.apache.commons.ApacheFileUtils;
import jPlusLibs.generic.IRepo;
import jPlusLibs.generic.MapRepoS;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.RuntimeUtils.cmdWaitStringArr;
import static jPlus.util.io.RuntimeUtils.execWaitBliss;

public class ScriptController extends FuncController {

    protected final FuncService<String> service;

    public ScriptController(Config config) {
        super(config);

        final IRepo<String> repo = new MapRepoS<>(pathMap()) {
        };
        service = new FuncService<>(repo) {
            @Override
            protected void process(IAPIWrapper api, String[] args) {
                final String commandPath = repo.get(args[0]);
                ScriptController.this.process(commandPath);
            }
        };
    }

    protected Map<String, String> pathMap() {
        return ApacheFileUtils.getRecursiveFiles(
                new File(System.getProperty("user.dir") + File.separator + "myscripts"),
                config.script.formats)
                .stream()
                .filter(f -> !f.isHidden())
                .collect(Collectors.toMap(FileUtils::simpleName, File::getAbsolutePath));
    }

    @Override
    public void read(Map<String, Receivable2<IAPIWrapper, String[]>> sync,
                     Map<String, Receivable2<IAPIWrapper, String[]>> async) {
        super.read(async, sync);
        service.read(async, menuName(), menu);
    }

    //***************************************************************//

    @Override
    protected void process(IAPIWrapper api, String[] args) {
        if (validateString(args, 1)) return;
        super.process(api, args);
    }

    protected void process(String commandBody) {
        execWaitBliss(cmdWaitStringArr(commandBody));
    }

    //***************************************************************//

    @Override
    public String menuName() {
        return "script";
    }

    @Override
    public char indicator() {
        return 's';
    }

    @Override
    protected String funcDesc(String item) {
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
