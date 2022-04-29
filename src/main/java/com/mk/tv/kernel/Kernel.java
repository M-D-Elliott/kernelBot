package com.mk.tv.kernel;

import com.mk.tv.io.generic.DummyAPIWrapper;
import com.mk.tv.io.generic.DummyClientResponse;
import com.mk.tv.io.generic.IAPIWrapper;
import com.mk.tv.io.generic.IClientResponse;
import com.mk.tv.kernel.controllers.IFuncController;
import com.mk.tv.kernel.controllers.mixes.MixController;
import com.mk.tv.kernel.controllers.presses.PressController;
import com.mk.tv.kernel.controllers.scripts.ScriptController;
import com.mk.tv.kernel.controllers.system.SystemController;
import com.mk.tv.kernel.controllers.tools.ToolsController;
import com.mk.tv.kernel.generic.BusyCommand;
import com.mk.tv.kernel.generic.Config;
import jPlus.io.Access;
import jPlus.io.Priority;
import jPlus.lang.callback.Receivable2;
import jPlus.lang.callback.Retrievable1;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.lang.IntUtils;

import java.util.*;

import static jPlus.util.lang.IntUtils.boundsMin;

public class Kernel implements Retrievable1<IClientResponse, IAPIWrapper> {

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

        Collections.addAll(controllers,
                new PressController(config),
                new ScriptController(config),
                new MixController(config),
                new ToolsController(config),
                new SystemController(config));
    }

    public void init() {
        prepareFuncMap();
        prepareMenu();
        callStartupFunc();
    }

    protected void prepareFuncMap() {
        syncFunctions.put("help", this::menuResponse);
        syncFunctions.put("term", (api, args) -> asyncThread.terminate());

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
    public final IClientResponse retrieve(IAPIWrapper api) {
        String message = api.in();
        final int len = message.length();
        if (len > 1 && message.charAt(0) == config.system.commandIndicator) {
            message = message.substring(1);
        } else if (api.access().value() < Access.PRIVATE.value() || len < 1) return new DummyClientResponse();

        return interpret(api, message.split(" "));
    }

    protected IClientResponse interpret(IAPIWrapper api, String[] message) {
        return thread(api, parse(message));
    }

    protected String[] parse(String[] parsedM) {
        Integer intIndicator = IntUtils.parseIntBliss(parsedM[0]);
        if (intIndicator != null) parsedM[0] = menu.get(boundsMin(intIndicator, 0));

        if (parsedM[0].length() > 0) {
            final List<String> menuList = indicatorMenuMap.get(parsedM[0].charAt(0));
            if (menuList != null) {
                intIndicator = IntUtils.parseIntBliss(parsedM[0].substring(1));
                if (intIndicator != null && intIndicator < menuList.size())
                    parsedM[0] = menuList.get(intIndicator);
            }
        }

        return parsedM;
    }

    protected IClientResponse thread(IAPIWrapper api, String[] parsedM) {
        final String funcName = parsedM[0];
        final Receivable2<IAPIWrapper, String[]> syncF = syncFunctions.get(funcName);
        if (syncF != null) {
            validFuncReceive(api, parsedM, syncF);
            return DummyClientResponse.success();
        }

        final Receivable2<IAPIWrapper, String[]> asyncF = asyncFunctions.get(funcName);
        if (asyncF != null) {
            validAsyncFuncReceive(api, parsedM, funcName, asyncF);
            return DummyClientResponse.success();
        }

        invalidFuncReceive(api);
        return new DummyClientResponse();
    }

    private void validAsyncFuncReceive(IAPIWrapper api, String[] parsedM, String funcName, Receivable2<IAPIWrapper, String[]> asyncF) {
        if (asyncThread.isDormant()) {
            asyncThread.busyMessage = String.format(busyMessageUnf(), funcName, api.username());
            asyncThread.body = () -> asyncF.receive(api, parsedM);
            logUserNameAndFunc(api, parsedM[0]);
            asyncThread.setOnEnd(() -> {
                api.println("Async func complete.");
                api.onFinish();
            });
        } else busyResp(api);
        asyncThread.run();
    }

    //***************************************************************//

    protected void busyResp(IAPIWrapper api) {
        api.print(asyncThread.busyMessage);
    }

    protected void validFuncReceive(IAPIWrapper api, String[] parsedM, Receivable2<IAPIWrapper, String[]> func) {
        logUserNameAndFunc(api, parsedM[0]);
        func.receive(api, parsedM);
        api.onFinish();
    }

    protected void logUserNameAndFunc(IAPIWrapper api, String name) {
        System.out.println(api.username() + " -- " + name);
    }

    protected void invalidFuncReceive(IAPIWrapper api) {
        System.out.println(api.username() + " -- unknown");
        api.setPriority(Priority.LOW).print("No func found...");
        api.onFinish();
    }

    //***************************************************************//

    protected void menuResponse(IAPIWrapper api, String[] args) {
        final String border = "?-";
        final String format = "  %d  " + border + "        %s        ";
        final String sep = System.lineSeparator();

        api.print(sep +
                ConsoleUtils.encaseInBanner(menu, border, (item, i) -> String.format(format, i, item)) +
                sep);
    }

    //***************************************************************//

    protected static String busyMessageUnf() {
        return "I am performing %1$s for %2$s";
    }

    public Map<String, Receivable2<IAPIWrapper, String[]>> getSyncFunctions() {
        return syncFunctions;
    }

    public Map<String, Receivable2<IAPIWrapper, String[]>> getAsyncFunctions() {
        return asyncFunctions;
    }

    public <T extends IFuncController> T getController(Class<T> klazz) {
        for (IFuncController c : controllers) if (klazz.isInstance(c)) return klazz.cast(c);
        return null;
    }

    public Map<String, List<String>> functionNamesByController() {
        final Map<String, List<String>> ret = new LinkedHashMap<>();
        for (IFuncController controller : controllers) {
            ret.put(controller.menuName(), controller.menu());
        }

        return ret;
    }
}
