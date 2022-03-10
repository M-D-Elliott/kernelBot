package com.mk.tv.auth.botusers;

import com.mk.tv.auth.AuthConfig;
import com.mk.tv.kernel.generic.FuncController;
import jPlus.io.file.FileUtils;
import jPlus.io.in.IAPIWrapper;
import jPlus.io.out.Access;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateString;

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
        sync.put("whisper", this::whisper);
        sync.put("changepass", this::changePassword);
        sync.put("setwelcome", this::setWelcome);
        sync.put("register", this::register);

        Collections.addAll(menu, "spill", "whisper", "changepass", "setwelcome", "register");
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

    private void whisper(IAPIWrapper api, String[] args) {
        if (validateString(args, 2)) {
            api.send(args[1], String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
        }
    }

    protected void register(IAPIWrapper api, String[] args) {
        if (checkPassword(api, args)) {
            if (!(validateString(args, 4) && service.register(args[2], args[3], args[4], api.out())))
                api.print(String.format(BotUserService.REGISTER_HELP, config.system.commandIndicator));
        } else wrongPass(api);
    }

    protected void changePassword(IAPIWrapper api, String[] args) {
        if (args.length >= 4) changePassword(api, args[1], args[2], args[3]);
        else api.print(String.format(BotUserService.PASSWORD_RESET_HELP, config.system.commandIndicator));
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
        final String secrets = String.join(System.lineSeparator(), FileUtils.read("repos/secrets.txt"));
        if (validateString(args, 1)) api.send(args[1], secrets);
        else api.send(api.username(), secrets);
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
            api.print(System.lineSeparator() + ConsoleUtils.encaseInBanner(userConfig.publicChannelWarning, "#"));
    }
}
