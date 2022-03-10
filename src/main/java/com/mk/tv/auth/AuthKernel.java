package com.mk.tv.auth;

import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.kernel.Kernel;
import jPlus.io.in.IAPIWrapper;
import jPlus.io.in.Priority;
import jPlus.io.out.DummyClientResponse;
import jPlus.io.out.IClientResponse;
import jPlus.util.io.ConsoleIOUtils;

public class AuthKernel extends Kernel {

    private final BotUserController userController;
    private final AuthConfig authConfig;
//    protected Set<BotUser> busyUsers = new HashSet<>();

    public AuthKernel(AuthConfig config) {
        super(config);
        authConfig = config;
        userController = new BotUserController(config);
        controllers.add(3, userController);
    }

    //***************************************************************//

    @Override
    protected void noFuncFoundResp(IAPIWrapper api) {
        final String welcome = userController.service.getWelcome(api.username());
        if (welcome == null) super.noFuncFoundResp(api);
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

    //***************************************************************//


//    @Override
//    protected void busyResp(IAPIWrapper api) {
//        super.busyResp(api);
//        busyUsers.add(userController.service.getBotUser(api.username()));
//    }
}
