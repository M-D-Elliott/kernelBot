package com.mk.tv.auth.botusers;

import com.mk.tv.auth.config.AuthConfig;
import com.mk.tv.kernel.generic.CommandController;
import jPlus.io.APIWrapper;
import jPlus.io.file.FileUtils;
import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.ConsoleUtils.sep;

public class BotUserController extends CommandController {

    protected final AuthConfig authConfig;
    public final BotUserService service = new BotUserService();

    public BotUserController(AuthConfig config) {
        super(config);
        this.authConfig = config;
    }

    //***************************************************************//

    @Override
    public void read(Map<String, Receivable2<APIWrapper, String[]>> sync,
                     Map<String, Receivable2<APIWrapper, String[]>> async) {
        super.read(sync, async);

        sync.put("spill", this::spill);
        sync.put("changepass", this::changePassword);
        sync.put("setwelcome", this::setWelcome);
        sync.put("register", this::register);

        Collections.addAll(menu, "spill", "changepass", "setwelcome", "register");
    }

    //***************************************************************//

    @Override
    public char indicator() {
        return 'u';
    }

    @Override
    public String entryPointName() {
        return "user";
    }

    @Override
    protected String menuPrefix() {
        return "USERS";
    }

    @Override
    protected String menuSuffix() {
        return "";
    }

    @Override
    protected String commandDesc(String item) {
        return "";
    }

    //***************************************************************//

    protected void register(APIWrapper api, String[] args) {
        if (checkPassword(api, args)) {
            if (!(validateString(args, 4) && service.register(args[2], args[3], args[4], api.out())))
                api.print(String.format(BotUserService.REGISTER_HELP, config.commandIndicator));
        } else wrongPass(api);
    }

    protected void changePassword(APIWrapper api, String[] args) {
        if (args.length >= 4) changePassword(api, args[1], args[2], args[3]);
        else api.print(String.format(BotUserService.PASSWORD_RESET_HELP, config.commandIndicator));
    }

    protected void changePassword(APIWrapper api, String oldPass, String newPass, String newPass2) {
        warnPublicChannel(api);
        service.changePassword(api.username(), oldPass, newPass, newPass2, api.out(), indicator());
    }

    protected void setWelcome(APIWrapper api, String[] args) {
        final String welcome = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        service.setWelcome(api.username(), welcome);
        api.print("Welcome set!");
    }

    protected void spill(APIWrapper api, String[] args) {
        if (api.access() == Access.PRIVATE) {
            if (authConfig.securityLevel == Access.PROTECTED && !checkPassword(api, args)) {
                wrongPass(api);
                return;
            }
        } else {
            api.print(authConfig.onlySecureChannelWarning);
            return;
        }

        final StringBuilder building = new StringBuilder();
        final String sep = sep();
        FileUtils.read("repos/secrets.txt").forEach(item -> building.append(item).append(sep));
        api.print(building.toString());
    }

    //***************************************************************//

    public boolean authenticate(APIWrapper api) {
        return service.authenticate(api.username());
    }

    public boolean authenticateSession(APIWrapper api) {
        return service.authenticateSession(api.username(), api.out());
    }

    public boolean initiateSession(APIWrapper api, String pass) {
        return service.initiateSession(api.username(), pass, authConfig.sessionDurationS());
    }

    public boolean checkPassword(APIWrapper api, String[] args) {
        if (validateString(args, 1))
            return service.checkPassword(api.username(), args[1]);
        else return false;
    }

    public void wrongPass(APIWrapper api) {
        api.print(authConfig.rejectUserMessage);
    }

    protected void warnPublicChannel(APIWrapper api) {
        if (api.access().value() < Access.PRIVATE.value())
            api.print(sep() + ConsoleUtils.encaseInBanner(authConfig.publicChannelWarning, "#"));
    }
}
