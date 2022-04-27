package com.mk.tv.auth.botusers;

import jPlusLibs.com.fasterxml.jackson.JacksonRepo;
import jPlus.lang.callback.Receivable1;

public class BotUserService {
    protected final JacksonRepo<BotUser> repo = new BotUserRepo();

    //***************************************************************//

    public boolean checkPassword(String username, String password) {
        return checkPassword(getBotUser(username), password);
    }

    public boolean checkPassword(BotUser botUser, String password) {
        return botUser.password.equals(password);
    }

    public boolean authenticate(String username) {
        return getBotUser(username) != null;
    }

    public boolean authenticateSession(String username, Receivable1<String> messageOut) {
        final BotUser user = getBotUser(username);
        final boolean userExists = user != null;

        if (userExists) return user.getSession().active();
        else messageOut.receive("User must be registered first.");

        return false;
    }

    public boolean initiateSession(String username, String pass, long dur) {
        final BotUser user = getBotUser(username);
        if (user == null || !user.password.equals(pass)) return false;

        user.session = new Session(dur);
        repo.save();
        return true;
    }

    public void changePassword(String username, String oldPass, String newPass, String newPass2, Receivable1<String> messageOut, char ind) {
        if (changePassword(getBotUser(username), oldPass, newPass, newPass2)) {
            repo.save();
            messageOut.receive(CHANGE_PASS_SUCCESS);
        } else messageOut.receive(String.format(PASSWORD_RESET_HELP, ind));
    }

    private boolean changePassword(BotUser botUser, String oldPass, String newPass, String newPass2) {
        if (newPass.length() >= MIN_PASS_LENGTH
                && checkPassword(botUser, oldPass) && newPass.equals(newPass2)) {
            botUser.password = newPass;

            return true;
        }
        return false;
    }

    public boolean register(String username, String pass, String pass2, Receivable1<String> messageOut) {
        if (username.length() >= 4) {
            final BotUser newUser = new BotUser(username);
            if (changePassword(newUser, newUser.password, pass, pass2)) {
                repo.add(newUser);
                messageOut.receive(String.format(REGISTER_SUCCESS, username));
                return true;
            }
        }

        return false;
    }

    public void setWelcome(String username, String message) {
        getBotUser(username).welcome = message;
        repo.save();
    }

    //***************************************************************//

    public BotUser getBotUser(String username) {
        return repo.get(username);
    }

    public String getWelcome(String username) {
        final BotUser user = getBotUser(username);
        return user == null ? null : user.welcome;
    }

    //***************************************************************//

    public static final int MIN_PASS_LENGTH = 6;
    public static final String REGISTER_HELP = String.format(
            "Passwords must be %1$d characters long", MIN_PASS_LENGTH)
            + System.lineSeparator() +
            "Type without quotes: '%1$sregister myPass newUsername newUserPass newUserPassMustMatch'";
    public static final String PASSWORD_RESET_HELP = String.format(
            "Passwords must be %1$d characters long", MIN_PASS_LENGTH)
            + System.lineSeparator() +
            "Type without quotes: '%1$schangepass currPass newPass newPassMustMatch'";
    public static final String CHANGE_PASS_SUCCESS = "Password changed!";
    public static final String REGISTER_SUCCESS = "Registration for %1$s successful!";
}
