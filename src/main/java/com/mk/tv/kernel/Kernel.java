package com.mk.tv.kernel;

import com.mk.tv.kernel.generic.IFuncController;
import com.mk.tv.kernel.mixes.MixController;
import com.mk.tv.kernel.presses.PressController;
import com.mk.tv.kernel.scripts.ScriptController;
import com.mk.tv.kernel.system.SystemController;
import jPlus.io.out.DummyAPIWrapper;
import jPlus.io.out.IAPIWrapper;
import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable1;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.lang.IntUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleUtils.sep;
import static jPlus.util.lang.IntUtils.boundsMin;

public class Kernel implements Receivable1<IAPIWrapper> {

    protected final Config config;
    protected final List<IFuncController> controllers = new ArrayList<>();
    protected final List<String> menu = new ArrayList<>();
    protected final Map<Character, List<String>> indicatorMenuMap = new LinkedHashMap<>();

    protected final Map<String, Receivable2<IAPIWrapper, String[]>> syncFunctions = new LinkedHashMap<>();
    protected final Map<String, Receivable2<IAPIWrapper, String[]>> asyncFunctions = new LinkedHashMap<>();
    protected final BusyCommand asyncThread = new BusyCommand();

    //***************************************************************//

    public Kernel(Config config) {
        this.config = config;

        controllers.add(new PressController(config));
        controllers.add(new ScriptController(config));
        controllers.add(new MixController(config));

        controllers.add(new SystemController(config));
    }

    public void init() {
        prepareFuncMap();
        prepareMenu();
        callStartupFunc();
    }

    protected void prepareFuncMap() {
        syncFunctions.put("help", this::menuResponse);
        syncFunctions.put("term", (api, args) -> {
            asyncThread.terminate();
        });

        for (IFuncController c : controllers)
            c.read(this.syncFunctions, this.asyncFunctions);
    }

    protected void prepareMenu() {
        menu.add("help");
        for (IFuncController c : controllers) {
            menu.add(c.menuName());
            indicatorMenuMap.put(c.indicator(), c.menu());
        }
    }

    private void callStartupFunc() {
        final String[] parsedM = config.system.startup.split(" ");
        final IAPIWrapper dummy = new DummyAPIWrapper();
        thread(dummy, parsedM);
    }

    //***************************************************************//

    @Override
    public void receive(IAPIWrapper api) {
        String message = api.in();
        if (message.charAt(0) == config.system.commandIndicator) {
            message = message.substring(1);
        } else if (api.access().value() < Access.PRIVATE.value()) return;

        interpret(api, message.split(" "));
    }

    protected void interpret(IAPIWrapper api, String[] message) {
        thread(api, parse(message));
    }

    protected String[] parse(String[] parsedM) {
        Integer intIndicator = IntUtils.parseInteger(parsedM[0]);
        if (intIndicator != null) parsedM[0] = menu.get(boundsMin(intIndicator, 0));

        final List<String> menuList = indicatorMenuMap.get(parsedM[0].charAt(0));
        if (menuList != null) {
            intIndicator = IntUtils.parseInteger(parsedM[0].substring(1));
            if (intIndicator != null && intIndicator < menuList.size())
                parsedM[0] = menuList.get(intIndicator);
        }

        return parsedM;
    }

    protected void thread(IAPIWrapper api, String[] parsedM) {
        final String funcName = parsedM[0];
        final Receivable2<IAPIWrapper, String[]> syncF = syncFunctions.get(funcName);
        if (syncF != null) receiveFunc(api, parsedM, syncF);

        final Receivable2<IAPIWrapper, String[]> asyncF = asyncFunctions.get(funcName);
        if (asyncF != null) {
            if (asyncThread.isDormant()) {
                asyncThread.busyMessage = String.format(busyMessageUnf(), funcName, api.username());
                asyncThread.body = () -> receiveValidFunc(api, parsedM, asyncF);
            } else api.print(asyncThread.busyMessage);
            asyncThread.run();
        }
    }

    protected void receiveFunc(IAPIWrapper api, String[] parsedM, Receivable2<IAPIWrapper, String[]> func) {
        if (func == null) {
            System.out.println(api.username() + " -- unknown");
            noCommandFoundResponse(api);
        } else receiveValidFunc(api, parsedM, func);
    }

    protected void receiveValidFunc(IAPIWrapper api, String[] parsedM, Receivable2<IAPIWrapper, String[]> func) {
        System.out.println(api.username() + " -- " + parsedM[0]);
        func.receive(api, parsedM);
    }

    protected void noCommandFoundResponse(IAPIWrapper api) {
        api.print("No command found...");
    }

    //***************************************************************//

    protected void menuResponse(IAPIWrapper api, String[] args) {
        final String border = "?-";
        final String format = "  %d  " + border + "        %s        ";

        api.print(sep() + ConsoleUtils.encaseInBanner(
                menu, border, (item, i) -> String.format(format, i, item)));
    }

    //***************************************************************//

    protected static String busyMessageUnf() {
        final String sep = sep();
        return "I am performing %1$s for %2$s" + sep +
                "Scripts, presses, or mixes are async. I can only run 1 async task at a time. " + sep +
                "I can still perform synchronous tasks such as help or changepass!";
    }
}
