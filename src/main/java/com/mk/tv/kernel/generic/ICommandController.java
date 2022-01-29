package com.mk.tv.kernel.generic;

import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;

import java.util.Map;

public interface ICommandController {
    void readCommands(
            Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap);

    String getMenuItem(int i);

    void processCommand(APIWrapper api, String[] args);

    boolean processCommand(String commandBody);

    void menuResponse(APIWrapper api, String[] args);
}
