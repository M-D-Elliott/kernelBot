package com.mk.tv.auth.botusers;

import com.mk.tv.auth.AuthConfig;
import com.mk.tv.kernel.generic.FuncController;
import jPlus.io.file.FileUtils;
import jPlus.io.out.IAPIWrapper;
import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.ConsoleUtils.sep;

public class BotUserController extends FuncController {

    protected final UserConfig userConfig;
    public final BotUserService service = new BotUserService();

    public BotUserController(AuthConfig config) {
        super(config);
        this.userConfig = config.user;
    }

    //***************************************************************//

    @Override
    public void read(Map<String, Receivable2<IAPIWrapper, String[]>> sync,
                     Map<String, Receivable2<IAPIWrapper, String[]>> async) {
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
    public String menuName() {
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
    protected String funcDesc(String item) {
        return "";
    }

    //***************************************************************//

    protected void register(IAPIWrapper api, String[] args) {
        if (checkPassword(api, args)) {
            if (!(validateString(args, 4) && service.register(args[2], args[3], args[4], api.out())))
                api.print(String.format(BotUserService.REGISTER_HELP, config.commandIndicator));
        } else wrongPass(api);
    }

    protected void changePassword(IAPIWrapper api, String[] args) {
        if (args.length >= 4) changePassword(api, args[1], args[2], args[3]);
        else api.print(String.format(BotUserService.PASSWORD_RESET_HELP, config.commandIndicator));
    }

    protected void changePassword(IAPIWrapper api, String oldPass, String newPass, String newPass2) {
        warnPublicChannel(api);
        service.changePassword(api.username(), oldPass, newPass, newPass2, api.out(), indicator());
    }

    protected void setWelcome(IAPIWrapper api, String[] args) {
        final String welcome = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        service.setWelcome(api.username(), welcome);
        api.print("Welcome set!");
    }

    protected void spill(IAPIWrapper api, String[] args) {
        if (api.access() == Access.PRIVATE) {
            if (userConfig.securityLevel == Access.PROTECTED && !checkPassword(api, args)) {
                wrongPass(api);
                return;
            }
        } else {
            api.print(userConfig.onlySecureChannelWarning);
            return;
        }

        final StringBuilder building = new StringBuilder();
        final String sep = sep();
        FileUtils.read("repos/secrets.txt").forEach(item -> building.append(item).append(sep));
        api.print(building.toString());
    }

    //***************************************************************//

    public boolean authenticate(IAPIWrapper api) {
        return service.authenticate(api.username());
    }

    public boolean authenticateSession(IAPIWrapper api) {
        return service.authenticateSession(api.username(), api.out());
    }

    public boolean initiateSession(IAPIWrapper api, String pass) {
        return service.initiateSession(api.username(), pass, userConfig.sessionDurationS());
    }

    public boolean checkPassword(IAPIWrapper api, String[] args) {
        if (validateString(args, 1))
            return service.checkPassword(api.username(), args[1]);
        else return false;
    }

    public void wrongPass(IAPIWrapper api) {
        api.print(userConfig.rejectUserMessage);
    }

    protected void warnPublicChannel(IAPIWrapper api) {
        if (api.access().value() < Access.PRIVATE.value())
            api.print(sep() + ConsoleUtils.encaseInBanner(userConfig.publicChannelWarning, "#"));
    }
}
