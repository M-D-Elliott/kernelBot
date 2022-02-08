package com.mk.tv.auth;

import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.auth.config.AuthConfig;
import com.mk.tv.kernel.Kernel;
import jPlus.io.APIWrapper;
import jPlus.util.io.ConsoleIOUtils;

public class AuthKernel extends Kernel {

    private final BotUserController userController;
    private final AuthConfig authConfig;

    public AuthKernel(AuthConfig config) {
        super(config);
        authConfig = config;
        userController = new BotUserController(config);
        controllers.add(userController);
    }

    @Override
    public void init(){
        super.init();
        menu.add(4, "user");
    }

    //***************************************************************//

    @Override
    protected void noCommandFoundResponse(APIWrapper api) {
        api.print(userController.getBotUser(api.username()).welcome);
    }

    @Override
    protected void interpret(APIWrapper api, String[] parsedM) {
        switch (authConfig.securityLevel) {
            case PUBLIC:
                break;
            case PROTECTED:
                if (!userController.authenticate(api)) {
                    api.print(authConfig.rejectUserMessage);
                    return;
                }
                break;
            case PRIVATE:
                if (userController.authenticateWPass(api)) break;
                if (ConsoleIOUtils.validateString(parsedM, 0)
                        && parsedM[0].equals("signin")
                        && ConsoleIOUtils.validateString(parsedM, 1)) {
                    if (userController.initiateSession(api, parsedM[1])) {
                        api.print("Sign in successful!");
                        break;
                    } else api.print("Wrong password!");
                } else api.print("You are not signed in. " + config.commandIndicator + "signin mypass");
            default:
                return;
        }

        super.interpret(api, parsedM);
    }
}
