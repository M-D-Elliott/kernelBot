package com.mk.tv.auth;

import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.kernel.Kernel;
import com.mk.tv.io.generic.IAPIWrapper;
import jPlus.io.Priority;
import com.mk.tv.io.generic.DummyClientResponse;
import com.mk.tv.io.generic.IClientResponse;
import jPlus.util.io.ConsoleIOUtils;

public class AuthKernel extends Kernel {

    private final BotUserController userController;
    private final AuthConfig authConfig;

    public AuthKernel(AuthConfig config) {
        super(config);
        authConfig = config;
        userController = new BotUserController(config);
        controllers.add(3, userController);
    }

    //***************************************************************//

    @Override
    protected void invalidFuncReceive(IAPIWrapper api) {
        final String welcome = userController.service.getWelcome(api.username());
        if (welcome == null) super.invalidFuncReceive(api);
        else api.setPriority(Priority.LOW).println(welcome);
    }

    @Override
    protected IClientResponse interpret(IAPIWrapper api, String[] parsedM) {
        switch (authConfig.user.securityLevel) {
            case PUBLIC:
                break;
            case PROTECTED:
                if (!userController.authenticate(api)) {
                    api.print(authConfig.user.rejectUserMessage);
                    return new DummyClientResponse();
                }
                break;
            case PRIVATE:
                if (userController.authenticateSession(api)) break;
                if (ConsoleIOUtils.validateString(parsedM, 0)
                        && parsedM[0].equals("signin")
                        && ConsoleIOUtils.validateString(parsedM, 1)) {
                    if (userController.initiateSession(api, parsedM[1])) {
                        api.print("Sign in successful!");
                        break;
                    } else userController.wrongPass(api);
                } else
                    api.setPriority(Priority.LOW).print("You are not signed in. " + config.system.commandIndicator + "signin mypass");
            default:
                return new DummyClientResponse();
        }

        return super.interpret(api, parsedM);
    }
}
