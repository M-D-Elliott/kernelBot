package com.mk.tv.auth.config;

public class Globals {
    public static final String ONLY_SECURE = "Hey I won't do that for just anybody...";
    public static final String WARN_PUBLIC_CHANNEL = " This is a public channel, btw... ";

    public static final String NOT_YET_IMPLEMENTED = "Not yet implemented!";

    public static final int MIN_PASS_LENGTH = 6;
    public static final String PASSWORD_RESET_HELP = String.format(
            "Passwords must be %d characters long", MIN_PASS_LENGTH)
            + System.lineSeparator() +
            "Type without quotes: '%schangepass currPass newPass newPassMustMatch'";
    public static final String CHANGE_PASS_SUCCESS = "Password changed!";
    public static final String REGISTER_HELP = String.format(
            "Passwords must be %d characters long", MIN_PASS_LENGTH)
            + System.lineSeparator() +
            "Type without quotes: '%sregister myPass newUsername newUserPass newUserPassMustMatch'";
    public static final String REGISTER_SUCCESS = "Registration for %s successful!";
}
