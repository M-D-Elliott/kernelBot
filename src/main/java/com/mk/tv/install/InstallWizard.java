package com.mk.tv.install;

import com.mk.tv.auth.AuthConfig;
import com.mk.tv.auth.botusers.BotUser;
import com.mk.tv.auth.botusers.BotUserRepo;
import com.mk.tv.kernel.controllers.system.IOConfig;
import com.mk.tv.kernel.generic.Config;
import jPlus.io.file.DirUtils;
import jPlus.lang.Runnables;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.io.JarUtils;
import jPlus.util.io.RuntimeUtils;
import jPlus.util.map.MapUtils;
import jPlusLibs.com.fasterxml.jackson.JacksonUtils;

import java.util.ArrayList;
import java.util.List;

import static jPlus.util.io.ConsoleIOUtils.confirm;
import static jPlus.util.io.ConsoleIOUtils.request;
import static jPlus.util.io.ConsoleUtils.encase;
import static jPlus.util.io.ConsoleUtils.sep;

public class InstallWizard extends Install {

    private final String header = SPLASH + sep() +
            "v" + JarUtils.version() + sep() +
            "----INSTALL WIZARD----" + sep() + sep();

    private final List<String> completeTasks = new ArrayList<>();

    public InstallWizard(boolean usingJRE) {
        super(usingJRE);
    }

    public void run() {
        System.out.println(header);

        System.out.println("For this install I will ask you to complete tasks elsewhere, but");
        System.out.println("do not close the prompt. Return here after each task!" + sep() + sep());

        task1();
        task2();
        task3();
        task4();
        task5();
        task6();
        task7();

        super.run();
    }

    private void taskConfirm(String title, Runnable body) {
        task(title, body, () -> {
            request("Ready for next step?");
        });
    }

    private void task(String title, Runnable body) {
        task(title, body, Runnables.blank);
    }

    private void task(String title, Runnable body, Runnable afterBody) {
        System.out.println();
        System.out.println();
        System.out.println(encase("Task " + (completeTasks.size() + 1), "-") + " " + title);
        System.out.println();
        body.run();
        afterBody.run();

        completeTasks.add(title);
        ConsoleUtils.clsBliss();
        System.out.println(header);

        int index = 1;
        for (String task : completeTasks) System.out.println(encase("Task " + index++, "+") + " " + task);
    }

    private void task1() {
        taskConfirm("Sign into the Discord Developer Portal.", () -> {
            confirm("May I open your browser?", () -> {
                RuntimeUtils.web(discordDevPortalURL);
            });
            System.out.println(discordDevPortalURL);
        });
    }

    private void task2() {
        taskConfirm("Create a new Discord application and name it whatever you like.", () -> {
            System.out.println("--It is a blue button in the top right of the Developer portal.");
        });
    }

    private void task3() {
        taskConfirm("Register new Discord Application as a Discord Bot", () -> {
            System.out.println("--With your new application selected (task 2) click the bot tab on the left.");
            System.out.println("--Then click add bot on the right.");
        });
    }

    private void task4() {
        taskConfirm("Enter your token into Kernel Bot", () -> {
            System.out.println("***DO NOT SHARE TOKEN PUBLICLY***" + sep());
            final String token = request("--Click copy token and paste it here");
            final AuthConfig config = JacksonUtils.readAndUpdateBliss(DirUtils.fromUserDir(Config.CONFIG_PATH),
                    AuthConfig.class, AuthConfig::newInstance);
            final IOConfig discordConf = config.system.ioConfigs.get(Config.DISCORD_IO_CONFIG_NAME);
            discordConf.key = token;
            discordConf.isActive = true;
            config.store();
            System.out.println("I have produced config.txt, edit as needed.");
        });
    }

    private void task5() {
        taskConfirm("Add your new bot to your discord server", () -> {
            System.out.println("--On the left side of the portal click OAuth2");
            System.out.println("--Click the sub-tab called URL Generator");
            System.out.println("--Select 'bot' as the scope and choose copy.");
            System.out.println("--Paste it into your browser, and select your server!");
            System.out.println("(Note your account must have access to that server)");
        });
    }

    private void task6() {
        taskConfirm("Give your bot permissions", () -> {
            System.out.println("--On the left side of the portal click Bot");
            System.out.println("--Under 'Privileged Gateway Intents' turn on 'presence intents' and 'server member intents'");
            System.out.println("--Select 'Administrator' under permissions");
            System.out.println("--Click save changes popup button!");
        });
    }

    private void task7() {
        task("Whitelist your username for kernel bot!", () -> {
            final String username = request("What is your Discord username?");
            final BotUser firstUser = new BotUser(username);
            final BotUserRepo repo = new BotUserRepo(MapUtils::newLinkedInstance);
            repo.add(firstUser);
        });

        System.out.println();
        System.out.println("I have produced repos/users.txt");
        System.out.println();
        System.out.println("Installation complete!");
    }

    //***************************************************************//

    private static final String discordDevPortalURL = "https://discord.com/developers/applications";

    private static final String SPLASH = "888                                         888 888888b.            888    \n" +
            "888                                         888 888  \"88b           888    \n" +
            "888                                         888 888  .88P           888    \n" +
            "888  888  .d88b.  888d888 88888b.   .d88b.  888 8888888K.   .d88b.  888888 \n" +
            "888 .88P d8P  Y8b 888P\"   888 \"88b d8P  Y8b 888 888  \"Y88b d88\"\"88b 888    \n" +
            "888888K  88888888 888     888  888 88888888 888 888    888 888  888 888    \n" +
            "888 \"88b Y8b.     888     888  888 Y8b.     888 888   d88P Y88..88P Y88b.  \n" +
            "888  888  \"Y8888  888     888  888  \"Y8888  888 8888888P\"   \"Y88P\"   \"Y888 ";
}
