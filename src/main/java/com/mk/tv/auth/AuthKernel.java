package com.mk.tv.auth;

import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.auth.config.AuthConfig;
import com.mk.tv.auth.config.Globals;
import com.mk.tv.kernel.Kernel;
import jPlus.io.APIWrapper;

public class AuthKernel extends Kernel {

    private final BotUserController userController;

    public AuthKernel(AuthConfig config) {
        super(config);
        userController = new BotUserController(config);
        controllers.add(userController);
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
