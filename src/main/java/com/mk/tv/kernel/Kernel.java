package com.mk.tv.kernel;

import com.mk.tv.kernel.generic.BusyCommand;
import com.mk.tv.kernel.generic.Config;
import com.mk.tv.kernel.generic.IFuncController;
import com.mk.tv.kernel.mixes.MixController;
import com.mk.tv.kernel.presses.PressController;
import com.mk.tv.kernel.scripts.ScriptController;
import com.mk.tv.kernel.system.SystemController;
import com.mk.tv.kernel.tools.ToolsController;
import jPlus.io.in.DummyAPIWrapper;
import jPlus.io.in.IAPIWrapper;
import jPlus.io.in.Priority;
import jPlus.io.out.Access;
import jPlus.io.out.DummyClientResponse;
import jPlus.io.out.IClientResponse;
import jPlus.lang.callback.Receivable2;
import jPlus.lang.callback.Retrievable1;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.lang.IntUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        controllers.add(new PressController(config));
        controllers.add(new ScriptController(config));
        controllers.add(new MixController(config));
        controllers.add(new ToolsController(config));

        controllers.add(new SystemController(config));
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

        if(parsedM[0].length() > 0){
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
            if (asyncThread.isDormant()) {
                asyncThread.busyMessage = String.format(busyMessageUnf(), funcName, api.username());
                asyncThread.body = () -> asyncF.receive(api, parsedM);
                logUserNameAndFunc(api, parsedM[0]);
                asyncThread.setOnEnd(() ->{
                    api.println("Async func complete.");
                });
            } else busyResp(api);
            asyncThread.run();
            return DummyClientResponse.success();
        }

        System.out.println(api.username() + " -- unknown");
        noFuncFoundResp(api);
        return new DummyClientResponse();
    }

    //***************************************************************//

    protected void busyResp(IAPIWrapper api) {
        api.print(asyncThread.busyMessage);
    }

    protected void validFuncReceive(IAPIWrapper api, String[] parsedM, Receivable2<IAPIWrapper, String[]> func) {
        logUserNameAndFunc(api, parsedM[0]);
        func.receive(api, parsedM);
    }

    protected void logUserNameAndFunc(IAPIWrapper api, String name) {
        System.out.println(api.username() + " -- " + name);
    }

    protected void noFuncFoundResp(IAPIWrapper api) {
        api.setPriority(Priority.LOW).print("No func found...");
    }

    //***************************************************************//

    protected void menuResponse(IAPIWrapper api, String[] args) {
        final String border = "?-";
        final String format = "  %d  " + border + "        %s        ";
        final String sep = System.lineSeparator();

        api.print(sep + ConsoleUtils.encaseInBanner(
                menu, border, (item, i) -> String.format(format, i, item))
                + sep);
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
}
