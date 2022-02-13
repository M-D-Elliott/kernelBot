package com.mk.tv.kernel.generic;

import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;

import java.util.List;
import java.util.Map;

public interface ICommandController {
    void readCommands(
            Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap);

    char indicator();

    List<String> menu();
}
