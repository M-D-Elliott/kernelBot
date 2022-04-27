package com.mk.tv.kernel.controllers;

import com.mk.tv.io.generic.IAPIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlusLibs.generic.IRepo;

import java.util.Collection;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.NOT_YET_IMPLEMENTED;

public abstract class FuncService<DATA_TYPE> {

    public final IRepo<DATA_TYPE> repo;

    public static final String FUNC_NOT_FOUND = "%1$s not found";

    public FuncService(IRepo<DATA_TYPE> repo) {
        this.repo = repo;
    }

    //***************************************************************//

    protected abstract void process(IAPIWrapper api, String[] args);

    public void read(Map<String, Receivable2<IAPIWrapper, String[]>> funcMap,
                     String epName, Collection<String> menu) {
        funcMap.put("add" + epName, this::add);

        final Receivable2<IAPIWrapper, String[]> repoCommand = this::process;
        final Collection<String> commandNames = repo.keys();
        for (String commandName : commandNames) funcMap.put(commandName, repoCommand);
        menu.addAll(commandNames);
    }

    protected void add(IAPIWrapper api, String[] args) {
        api.print(NOT_YET_IMPLEMENTED);
    }
}
