package com.mk.tv.kernel.generic;

import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;

import java.util.List;
import java.util.Map;

public interface ICommandController {
    void read(
            Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap, Map<String, Receivable2<APIWrapper, String[]>> asyncFunctionMap);

    char indicator();

    List<String> menu();

    String entryPointName();
}
