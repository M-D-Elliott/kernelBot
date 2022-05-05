package com.mk.tv.io.spring;

import com.mk.tv.auth.AuthKernel;
import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.io.spring.controllers.IOController;
import com.mk.tv.io.spring.sec.CustomIPAuth;
import jPlus.io.file.DirUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Collections;

@SpringBootApplication
public class SpringEP {
    public static void main(AuthKernel kernel, String port) {

        final File hostsFile = new File(DirUtils.fromUserDir("repos/.hosts"));

        if (!hostsFile.exists()) {
            System.err.println(noHostsError());
            return;
        }

        SpringApplication app = new SpringApplication(SpringEP.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", port));
        app.setHeadless(false);
        app.run();

        IOController.instance.setKernel(kernel);

        CustomIPAuth.instance.addScrubbedIPs(hostsFile);

        final BotUserController userController = kernel.getBotUserController();
        if (userController != null)
            CustomIPAuth.instance.setAuthenticator(userController::initiateSession);
    }

    @NotNull
    protected static String noHostsError() {
        return ".hosts file required for web interface." + System.lineSeparator() +
                "1. Use editor like notepad++ to save file without filename as .hosts." + System.lineSeparator() +
                "2. Set one IP location per line, obtained when a user tries to log in." + System.lineSeparator() +
                "e.g. 102.94.23.12" + System.lineSeparator() +
                "3. IP location ranges can be specified, but best to only do this for local." + System.lineSeparator() +
                "e.g. 192.168.1.0/24" + System.lineSeparator() +
                "4. Web IP Locations (from your friends) automatically include all, no need to use /." + System.lineSeparator() +
                "e.g. 102.94.23.12";
    }
}
