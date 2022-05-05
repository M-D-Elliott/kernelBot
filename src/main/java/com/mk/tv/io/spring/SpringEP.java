package com.mk.tv.io.spring;

import com.mk.tv.auth.AuthKernel;
import com.mk.tv.auth.botusers.BotUserController;
import com.mk.tv.io.spring.controllers.IOController;
import com.mk.tv.io.spring.sec.CustomIPAuth;
import jPlus.io.file.DirUtils;
import jPlus.io.file.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringEP {
    public static void main(AuthKernel kernel, String port) {

        final File hostsFile = new File(DirUtils.fromUserDir("repos/.hosts"));

        if (!hostsFile.exists()) {
            System.err.println(".hosts required for web intf.");
            return;
        }

        SpringApplication app = new SpringApplication(SpringEP.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", port));
        app.setHeadless(false);
        app.run();

        IOController.instance.setKernel(kernel);

        final String regexMatcher = "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}[/]?(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)?";
        final Pattern pattern = Pattern.compile(regexMatcher);
        final List<String> scrubbedWhiteList =
                FileUtils.read(hostsFile).stream()
                        .filter(string -> pattern.matcher(string).matches())
                        .collect(Collectors.toList());

        final Set<String> whitelist = CustomIPAuth.instance.whiteList();
        whitelist.addAll(scrubbedWhiteList);
        whitelist.add("0:0:0:0:0:0:0:1");

        final BotUserController userController = kernel.getBotUserController();
        if (userController != null)
            CustomIPAuth.instance.setAuthenticator(userController::initiateSession);
    }
}
