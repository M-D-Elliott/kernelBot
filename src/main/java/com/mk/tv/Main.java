package com.mk.tv;

import com.mk.tv.entryPoints.Install;
import com.mk.tv.entryPoints.InstallWizard;
import com.mk.tv.entryPoints.PreInstall;
import com.mk.tv.entryPoints.Run;
import jPlus.util.io.ConsoleIOUtils;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        if (ConsoleIOUtils.validateChar(args, 0)) {
            final char entry = args[0].toLowerCase().charAt(0);
            switch (entry) {
                case 'e':
                    new Run().run();
                    break;
                case 'w':
                    new InstallWizard(isArg2J(args)).run();
                    break;
                case 'i':
                    new Install(isArg2J(args)).run();
                    break;
                default:
                    new PreInstall().run();
                    break;
            }
        } else new PreInstall().run();
    }

    private static boolean isArg2J(String[] args) {
        return ConsoleIOUtils.validateChar(args, 1) &&
                args[1].toLowerCase().charAt(0) == 'j';
    }
}
