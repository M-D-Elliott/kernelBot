package com.mk.tv.auth;

import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.auth.config.AuthConfig;
import com.mk.tv.kernel.Kernel;
import jPlus.io.APIWrapper;
import jPlus.io.security.Access;
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
    public void parse(APIWrapper api) {
        switch (authConfig.securityLevel) {
            case PUBLIC:
                break;
            case PROTECTED:
                if (!userController.authenticateUser(api)) {
                    api.print(authConfig.rejectUserMessage);
                    return;
                }
                break;
            case PRIVATE:
                if (userController.authenticateUser(api, true)) break;
                String message = api.in();
                if (message.charAt(0) == config.commandIndicator) {
                    message = message.substring(1);
                } else if (api.access().value() <= Access.PRIVATE.value()) return;

                final String[] parsedM = message.split(" ");
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

        super.parse(api);
    }
}
