package com.mk.tv.kernel.generic;

import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IFuncController {
    List<String> menu = new ArrayList<>();

    void read(
            Map<String, Receivable2<IAPIWrapper, String[]>> sync,
            Map<String, Receivable2<IAPIWrapper, String[]>> async);

    char indicator();

    String menuName();
}
