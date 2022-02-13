package com.mk.tv.kernel.generic;

import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;

import java.util.Collection;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.NOT_YET_IMPLEMENTED;

public abstract class RepoCommandController extends CommandController implements IRepoCommandController {

    protected static final String COMMAND_NOT_FOUND = "%1$s not found";

    //***************************************************************//

    public RepoCommandController(Config config) {
        super(config);
    }

    protected abstract String entryPointName();

    protected abstract Collection<String> commandNames();

    //***************************************************************//

    @Override
    public void readCommands(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        final String epName = entryPointName();
        commandFuncMap.put("add" + epName, this::addCommand);
        commandFuncMap.put(epName, this::processCommand);

        final Receivable2<APIWrapper, String[]> repoCommand = this::processRepoCommand;
        final Collection<String> commandNames = commandNames();
        for (String commandName : commandNames) commandFuncMap.put(commandName, repoCommand);
        menu.addAll(commandNames);
    }

    @Override
    public void addCommand(APIWrapper api, String[] args) {
        api.print(NOT_YET_IMPLEMENTED);
    }
}
