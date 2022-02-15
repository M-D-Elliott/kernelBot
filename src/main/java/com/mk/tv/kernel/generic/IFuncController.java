package com.mk.tv.kernel.generic;

import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable2;

import java.util.List;
import java.util.Map;

public interface IFuncController {
    void read(
            Map<String, Receivable2<IAPIWrapper, String[]>> commandFuncMap, Map<String, Receivable2<IAPIWrapper, String[]>> asyncFunctionMap);

    char indicator();

    List<String> menu();

    String menuName();
}
