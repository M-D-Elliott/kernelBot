package com.mk.tv.kernel.generic;

import jPlus.io.APIWrapper;

public interface IRepoCommandController extends ICommandController {
    void processRepoCommand(APIWrapper api, String[] args);

    void addCommand(APIWrapper api, String[] args);
}
