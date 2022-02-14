package com.mk.tv.kernel;

import com.mk.tv.kernel.generic.ICommandController;
import com.mk.tv.kernel.presses.PressController;
import com.mk.tv.kernel.mixes.MixController;
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

    protected final Config config;
    protected final List<ICommandController> controllers = new ArrayList<>();

    //***************************************************************//

    protected final Map<String, Receivable2<APIWrapper, String[]>> commandFunctionMap = new LinkedHashMap<>();
    protected final List<String> menu = new ArrayList<>();
    protected final Map<Character, List<String>> indicatorMenuMap = new LinkedHashMap<>();

    public Kernel(Config config) {
        this.config = config;

        final PressController press = new PressController(config);
        controllers.add(press);
        controllers.add(new ScriptController(config));

        final MixController mix = new MixController(config);
        controllers.add(mix);

        controllers.add(new SystemController(config));

        mix.synchronicityReceivers.add(press::setSynchronous);
    }

    public void init() {
        prepareCommandMap();
        prepareMenu();
    }

    protected void prepareCommandMap() {
        commandFunctionMap.put("help", this::menuResponse);
        for (ICommandController controller : controllers) controller.read(this.commandFunctionMap);
    }

    protected void prepareMenu() {
        Collections.addAll(menu, "help", "press", "script", "mix", "system");
        for (ICommandController controller : controllers)
            indicatorMenuMap.put(controller.indicator(), controller.menu());
    }

    //***************************************************************//

    public void parse(APIWrapper api) {
        String message = api.in();
        if (message.charAt(0) == config.commandIndicator) {
            message = message.substring(1);
        } else if (api.access().value() < Access.PRIVATE.value()) return;

        final String[] parsedM = message.split(" ");
        interpret(api, parsedM);
    }

    protected void interpret(APIWrapper api, String[] parsedM) {

        final Receivable2<APIWrapper, String[]> command =
                findCommand(parsedM);

        processCommand(api, command, parsedM);
    }

    protected Receivable2<APIWrapper, String[]> findCommand(String[] parsedM) {
        Integer intIndicator = IntUtils.parseInteger(parsedM[0]);
        if (intIndicator != null) parsedM[0] = menu.get(boundsMin(intIndicator, 0));

        final List<String> menuList = indicatorMenuMap.get(parsedM[0].charAt(0));
        if (menuList != null) {
            intIndicator = IntUtils.parseInteger(parsedM[0].substring(1));
            if (intIndicator != null && intIndicator < menuList.size())
                parsedM[0] = menuList.get(intIndicator);
        }

        return commandFunctionMap.get(parsedM[0]);
    }

    protected void processCommand(APIWrapper api, Receivable2<APIWrapper, String[]> command, String[] parsedM) {
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
        final String border = "?-";
        final String format = "  %d  " + border + "        %s        ";

        api.print(sep() + ConsoleUtils.encaseInBanner(
                menu, border, (item, i) -> String.format(format, i, item)));
    }
}
