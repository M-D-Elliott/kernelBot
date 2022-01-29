package com.mk.tv.auth;

import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.auth.config.Globals;
import com.mk.tv.auth.config.AuthConfig;
import com.mk.tv.kernel.Kernel;
import jPlus.io.APIWrapper;

import java.util.Collections;

public class AuthKernel extends Kernel {

    private final BotUserController userController;

    public AuthKernel(AuthConfig config) {
        super(config);
        userController = new BotUserController(config);
    }

    @Override
    protected void prepareCommandMap() {
        super.prepareCommandMap();
        userController.readCommands(commandFunctionMap);
    }

    @Override
    protected void prepareMenu() {
        super.prepareMenu();
        menu.addAll(3, Collections.singletonList("user"));
        indicatorMenuMap.put(userController.indicator, userController.menu);
    }

    //***************************************************************//

    @Override
    protected void noCommandFoundResponse(APIWrapper api) {
        api.print(userController.getBotUser(api.username()).welcome);
    }

    @Override
    public void interpret(APIWrapper api) {
        if (userController.getBotUser(api.username()) == null) {
            if (api.access().value() == 3) api.print(Globals.NOT_USER);
            return;
        }

        super.interpret(api);
    }
}
