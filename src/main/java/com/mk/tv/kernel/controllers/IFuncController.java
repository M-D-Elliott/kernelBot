package com.mk.tv.kernel.controllers;

import com.mk.tv.io.generic.IAPIWrapper;
import jPlus.lang.callback.Receivable2;

import java.util.List;
import java.util.Map;

public interface IFuncController {

    List<String> menu();

    void read(
            Map<String, Receivable2<IAPIWrapper, String[]>> sync,
            Map<String, Receivable2<IAPIWrapper, String[]>> async);

    char indicator();

    String menuName();

}
