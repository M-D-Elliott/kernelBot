package com.mk.tv.auth.botusers;

import com.mk.tv.auth.config.AuthConfig;
import com.mk.tv.auth.config.Globals;
import com.mk.tv.kernel.generic.ICommandController;
import com.mk.tv.kernel.generic.JacksonRepo;
import jPlus.io.APIWrapper;
import jPlus.io.file.FileUtils;
import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;

import java.util.*;

import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.ConsoleUtils.sep;

public class BotUserController implements ICommandController {
    private final JacksonRepo<BotUser> repo = new BotUserRepo();

    private final AuthConfig config;

    private final List<String> menu = new ArrayList<>();
    private final char indicator = 'u';

    public BotUserController(AuthConfig config) {
        this.config = config;
    }

    //***************************************************************//

    @Override
    public void readCommands(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        commandFuncMap.put("user", this::processCommand);

        commandFuncMap.put("spill", this::spill);
        commandFuncMap.put("changepass", this::changePassword);
        commandFuncMap.put("setwelcome", this::setWelcome);
        commandFuncMap.put("register", this::register);

        Collections.addAll(menu, "spill", "changepass", "setwelcome", "register");
    }

    @Override
    public char indicator() {
        return indicator;
    }

    public void processCommand(APIWrapper api, String[] args) {
        menuResponse(api, args);
    }

    @Override
    public void menuResponse(APIWrapper api, String[] args) {
        final String format = " %c%d  " + config.border + "        %s        ";

        api.print(sep() + ConsoleUtils.encaseInBanner(
                menu, config.border, (item, i) -> String.format(format, this.indicator, i, item)));
    }

    @Override
    public List<String> menu() {
        return menu;
    }

    //***************************************************************//

    public void register(APIWrapper api, String[] args) {
        if (checkPassword(api, args)) {
            if (args.length >= 5 && register(api, args[2], args[3], args[4]))
                return;
        } else wrongPass(api);
        api.print(String.format(Globals.REGISTER_HELP, config.commandIndicator));
    }

    private boolean register(APIWrapper api, String username, String pass, String pass2) {
        if (username.length() >= 4) {
            final BotUser newUser = new BotUser(username);
            if (changePassword(api, newUser, "", pass, pass2)) {
                repo.add(newUser);
                api.print(String.format(Globals.REGISTER_SUCCESS, username));
                return true;
            }
        }

        return false;
    }

    public void changePassword(APIWrapper api, String[] args) {
        if (args.length >= 4) changePassword(api, args[1], args[2], args[3]);
        else api.print(String.format(Globals.PASSWORD_RESET_HELP, config.commandIndicator));
    }

    private void changePassword(APIWrapper api, String oldPass, String newPass, String newPass2) {
        changePassword(api, getBotUser(api.username()), oldPass, newPass, newPass2);
    }

    private boolean changePassword(APIWrapper api, BotUser botUser, String oldPass, String newPass, String newPass2) {
        warnPublicChannel(api);

        if (newPass.length() >= Globals.MIN_PASS_LENGTH
                && checkPassword(botUser, oldPass) && newPass.equals(newPass2)) {
            botUser.password = newPass;
            repo.save();
            api.print(Globals.CHANGE_PASS_SUCCESS);
            return true;
        } else api.print(String.format(Globals.PASSWORD_RESET_HELP, config.commandIndicator));
        return false;
    }

    public void setWelcome(APIWrapper api, String[] args) {
        getBotUser(api.username()).welcome = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        repo.save();
        api.print("Welcome set!");
    }

    private void spill(APIWrapper api, String[] args) {
        if (api.access() == Access.PRIVATE) {
            if (config.securityLevel == Access.PROTECTED && !checkPassword(api, args)) {
                wrongPass(api);
                return;
            }
        } else {
            api.print(config.onlySecureChannelWarning);
            return;
        }

        final StringBuilder building = new StringBuilder();
        final String sep = sep();
        FileUtils.read("repos/secrets.txt").forEach(item -> building.append(item).append(sep));
        api.print(building.toString());

    }

    //***************************************************************//

    public boolean authenticate(APIWrapper api) {
        return getBotUser(api.username()) != null;
    }


    public boolean authenticateWPass(APIWrapper api) {
        final BotUser user = getBotUser(api.username());
        final boolean userExists = user != null;

        if (userExists) return user.getSession().active();
        else api.print("User must be registered first.");

        return false;
    }

    public boolean initiateSession(APIWrapper api, String pass) {
        final BotUser user = getBotUser(api.username());
        if (user == null || !user.password.equals(pass)) return false;

        user.session = new Session(config.sessionDurationS());
        repo.save();
        return true;
    }

    public boolean checkPassword(APIWrapper api, String[] args) {
        if (validateString(args, 1))
            return checkPassword(api, args[1]);
        else return false;
    }

    private boolean checkPassword(APIWrapper api, String password) {
        return checkPassword(getBotUser(api.username()), password);
    }

    public void wrongPass(APIWrapper api) {
        api.print(config.rejectUserMessage);
    }

    private boolean checkPassword(BotUser botUser, String password) {
        return botUser.password.equals(password);
    }

    private void warnPublicChannel(APIWrapper api) {
        if (api.access().value() < 3)
            api.print(ConsoleUtils.encaseInBanner(config.publicChannelWarning, "#"));
    }

    public BotUser getBotUser(String username) {
        return repo.get(username);
    }
}
