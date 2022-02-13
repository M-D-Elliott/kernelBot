package com.mk.tv.kernel.generic;

import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;

import java.util.Collection;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.NOT_YET_IMPLEMENTED;

public abstract class RepoCommandController extends CommandController {

    protected static final String COMMAND_NOT_FOUND = "%1$s not found";

    //***************************************************************//

    public RepoCommandController(Config config) {
        super(config);
    }

    protected abstract Collection<String> commandNames();

    //***************************************************************//

    protected abstract void processRepoCommand(APIWrapper api, String[] args);

    @Override
    public void readCommands(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        commandFuncMap.put("add" + entryPointName(), this::addCommand);
        super.readCommands(commandFuncMap);

        final Receivable2<APIWrapper, String[]> repoCommand = this::processRepoCommand;
        final Collection<String> commandNames = commandNames();
        for (String commandName : commandNames) commandFuncMap.put(commandName, repoCommand);
        menu.addAll(commandNames);
    }

    protected void addCommand(APIWrapper api, String[] args) {
        api.print(NOT_YET_IMPLEMENTED);
    }
}
