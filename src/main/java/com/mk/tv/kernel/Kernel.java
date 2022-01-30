package com.mk.tv.kernel;

import com.mk.tv.kernel.hotkeys.HotkeyController;
import com.mk.tv.kernel.scripts.ScriptController;
import com.mk.tv.kernel.system.Config;
import com.mk.tv.kernel.system.SystemController;
import jPlus.io.APIWrapper;
import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.lang.IntUtils;

import java.util.*;

import static jPlus.util.io.ConsoleUtils.sep;
import static jPlus.util.lang.IntUtils.boundsMin;

public class Kernel {

    public final Config config;
    private final HotkeyController hotkeyController;
    private final ScriptController scriptController;
    private final SystemController systemController;

    //***************************************************************//

    protected final Map<String, Receivable2<APIWrapper, String[]>> commandFunctionMap = new LinkedHashMap<>();
    protected final List<String> menu = new ArrayList<>();
    protected final Map<Character, List<String>> indicatorMenuMap = new LinkedHashMap<>();

    public Kernel(Config config) {
        this.config = config;
        hotkeyController = new HotkeyController(config);
        scriptController = new ScriptController(config);
        systemController = new SystemController(config);
    }

    public void init() {
        prepareCommandMap();
        prepareMenu();
    }

    protected void prepareCommandMap() {
        commandFunctionMap.put("help", this::menuResponse);

        hotkeyController.readCommands(this.commandFunctionMap);
        scriptController.readCommands(this.commandFunctionMap);
        systemController.readCommands(this.commandFunctionMap);
    }

    protected void prepareMenu() {
        Collections.addAll(menu, "help", "press", "script", "system");
        indicatorMenuMap.put(hotkeyController.indicator, hotkeyController.menu);
        indicatorMenuMap.put(scriptController.indicator, scriptController.menu);
        indicatorMenuMap.put(systemController.indicator, systemController.menu);
    }

    //***************************************************************//

    public void interpret(APIWrapper api) {
        String message = api.in();
        if (message.charAt(0) == config.commandIndicator) {
            message = message.substring(1);
        } else if (api.access().value() <= Access.PRIVATE.value()) return;

        final String[] parsedM = message.split(" ");

        Integer intIndicator = IntUtils.parseInteger(parsedM[0]);
        if (intIndicator != null) parsedM[0] = menu.get(boundsMin(intIndicator, 0));

        final List<String> menuList = indicatorMenuMap.get(parsedM[0].charAt(0));
        if (menuList != null) {
            intIndicator = IntUtils.parseInteger(parsedM[0].substring(1));
            if (intIndicator != null && --intIndicator < menuList.size())
                parsedM[0] = menuList.get(intIndicator);
        }

        final Receivable2<APIWrapper, String[]> command = commandFunctionMap.get(parsedM[0]);

        if (command == null) {
            System.out.println(api.username() + " -- unknown");
            noCommandFoundResponse(api);
        } else {
            System.out.println(api.username() + " -- " + parsedM[0]);
            command.receive(api, parsedM);
        }
    }

    //***************************************************************//

    protected void noCommandFoundResponse(APIWrapper api) {
        api.print("No command found...");
    }

    protected void menuResponse(APIWrapper api, String[] args) {
        final String format = "  %d  " + config.border + "        %s        ";

        api.print(sep() + ConsoleUtils.encaseInBanner(
                menu, config.border, (item, i) -> String.format(format, i, item)));
    }
}
