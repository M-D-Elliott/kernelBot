package com.mk.tv.auth.config;

public class Globals {

    public static final int MIN_PASS_LENGTH = 6;
    public static final String PASSWORD_RESET_HELP = String.format(
            "Passwords must be %1$d characters long", MIN_PASS_LENGTH)
            + System.lineSeparator() +
            "Type without quotes: '%1$schangepass currPass newPass newPassMustMatch'";
    public static final String CHANGE_PASS_SUCCESS = "Password changed!";
    public static final String REGISTER_HELP = String.format(
            "Passwords must be %1$d characters long", MIN_PASS_LENGTH)
            + System.lineSeparator() +
            "Type without quotes: '%1$sregister myPass newUsername newUserPass newUserPassMustMatch'";
    public static final String REGISTER_SUCCESS = "Registration for %1$s successful!";
}
