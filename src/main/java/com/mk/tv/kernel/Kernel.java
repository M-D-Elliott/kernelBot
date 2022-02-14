package com.mk.tv.kernel;

import com.mk.tv.kernel.generic.ICommandController;
import com.mk.tv.kernel.mixes.MixController;
import com.mk.tv.kernel.presses.PressController;
import com.mk.tv.kernel.scripts.ScriptController;
import com.mk.tv.kernel.system.Config;
import com.mk.tv.kernel.system.SystemController;
import jPlus.io.APIWrapper;
import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.lang.IntUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleUtils.sep;
import static jPlus.util.io.JarUtils.version;
import static jPlus.util.lang.IntUtils.boundsMin;

public class Kernel {

    protected final Config config;
    protected final List<ICommandController> controllers = new ArrayList<>();

    //***************************************************************//

    protected final Map<String, Receivable2<APIWrapper, String[]>> syncFunctions = new LinkedHashMap<>();
    protected final Map<String, Receivable2<APIWrapper, String[]>> asyncFunctions = new LinkedHashMap<>();
    protected Thread asyncThread = null;
    protected String busyMessage = "";
    // protected Set<BotUser> busyUsers = new HashSet<>();

    protected final List<String> menu = new ArrayList<>();
    protected final Map<Character, List<String>> indicatorMenuMap = new LinkedHashMap<>();

    public Kernel(Config config) {
        this.config = config;

        controllers.add(new PressController(config));
        controllers.add(new ScriptController(config));
        controllers.add(new MixController(config));

        controllers.add(new SystemController(config));
    }

    public void init() {
        prepareCommandMap();
        prepareMenu();
    }

    protected void prepareCommandMap() {
        syncFunctions.put("help", this::menuResponse);

        controllers.forEach(c -> {
            c.read(this.syncFunctions, this.asyncFunctions);
        });
    }

    protected void prepareMenu() {
        menu.add("help");
        controllers.forEach(c -> {
            menu.add(c.entryPointName());
            indicatorMenuMap.put(c.indicator(), c.menu());
        });
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

        Integer intIndicator = IntUtils.parseInteger(parsedM[0]);
        if (intIndicator != null) parsedM[0] = menu.get(boundsMin(intIndicator, 0));

        final List<String> menuList = indicatorMenuMap.get(parsedM[0].charAt(0));
        if (menuList != null) {
            intIndicator = IntUtils.parseInteger(parsedM[0].substring(1));
            if (intIndicator != null && intIndicator < menuList.size())
                parsedM[0] = menuList.get(intIndicator);
        }

        final String commandN = parsedM[0];
        final Receivable2<APIWrapper, String[]> syncF = syncFunctions.get(commandN);
        if (syncF != null) receiveCommand(api, parsedM, syncF);

        final Receivable2<APIWrapper, String[]> asyncF = asyncFunctions.get(commandN);
        if (asyncF != null)
            if (asyncThread == null) {
                asyncThread = new Thread(() -> {
                    receiveCommand(api, parsedM, asyncF);
                    asyncThread = null;
                });
                busyMessage = String.format(busyMessageUnf(), commandN, api.username());
                asyncThread.start();
            } else api.print(busyMessage);
    }

    private void receiveCommand(APIWrapper api, String[] parsedM, Receivable2<APIWrapper, String[]> command) {
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

    //***************************************************************//

    protected final String busyMessageUnf() {
        final String sep = sep();
        return "I am performing %1$s for %2$s" + sep +
                "Scripts, presses, or mixes are async. I can only run 1 async task at a time. " + sep +
                "I can still perform synchronous tasks such as help or changepass!";
    }
}
