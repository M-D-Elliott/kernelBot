package com.mk.tv.auth;

import com.mk.tv.auth.botusers.BotUser;
import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.kernel.Kernel;
import jPlus.io.out.IAPIWrapper;
import jPlus.util.io.ConsoleIOUtils;

import java.util.HashSet;
import java.util.Set;

public class AuthKernel extends Kernel {

    private final BotUserController userController;
    private final AuthConfig authConfig;
    //protected Set<BotUser> busyUsers = new HashSet<>();

    public AuthKernel(AuthConfig config) {
        super(config);
        authConfig = config;
        userController = new BotUserController(config);
        controllers.add(3, userController);
    }

    //***************************************************************//

    @Override
    protected void noCommandFoundResponse(IAPIWrapper api) {
        api.print(userController.service.getWelcome(api.username()));
    }

    @Override
    protected void interpret(IAPIWrapper api, String[] parsedM) {
        switch (authConfig.user.securityLevel) {
            case PUBLIC:
                break;
            case PROTECTED:
                if (!userController.authenticate(api)) {
                    api.print(authConfig.user.rejectUserMessage);
                    return;
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
                } else api.print("You are not signed in. " + config.system.commandIndicator + "signin mypass");
            default:
                return;
        }

        super.interpret(api, parsedM);
    }
}
