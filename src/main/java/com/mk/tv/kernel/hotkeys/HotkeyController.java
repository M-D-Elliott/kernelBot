package com.mk.tv.kernel.hotkeys;

import com.mk.tv.kernel.generic.IRepoCommandController;
import com.mk.tv.kernel.generic.JacksonRepo;
import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.awt.KeyEvents;
import jPlus.util.awt.RobotUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.ConsoleUtils.sep;

public class HotkeyController implements IRepoCommandController {
    private final JacksonRepo<Hotkey> repo = new JacksonRepo<>("repos/hotkeys.txt",
            Hotkey.typeRef(), HotkeyController::newHotkeyMap);

    private final Config config;

    public final List<String> menu = new ArrayList<>();
    public final char indicator = 'p';

    public HotkeyController(Config config) {
        this.config = config;
    }

    //***************************************************************//

    private static LinkedHashMap<String, Hotkey> newHotkeyMap() {
        final LinkedHashMap<String, Hotkey> ret = new LinkedHashMap<>();
        final Hotkey hotkey = new Hotkey("a+b");
        ret.put("ab", hotkey);
        return ret;
    }

    //***************************************************************//

    @Override
    public void readCommands(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        commandFuncMap.put("addpress", this::addCommand);
        commandFuncMap.put("press", this::processCommand);

        final Receivable2<APIWrapper, String[]> hotkeyCommand = this::processRepoCommand;

        for (String hotkeyName : repo.map.keySet()) {
            commandFuncMap.put(hotkeyName, hotkeyCommand);
        }

        menu.addAll(repo.map.keySet());
    }

    @Override
    public String getMenuItem(int i) {
        return menu.get(i);
    }

    @Override
    public void processCommand(APIWrapper api, String[] args) {
        if (validateString(args, 1) && processCommand(args[1])) return;
        menuResponse(api, args);
    }

    @Override
    public boolean processCommand(String commandBody) {
        try {
            final int[] keyEvents = KeyEvents.parseGroup(commandBody);
            RobotUtils.press(keyEvents);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void menuResponse(APIWrapper api, String[] args) {
        final String prefix = sep() + "PRESS PRESETS" + sep() +
                config.displayLiteralCommand("press ctrl+f1") + "will press ctrl and f1 on the host comp!" + sep() +
                config.displayLiteralCommand("addpress hotkeyName hotkeyCode") + " -- adds a new hotkey preset!"
                + sep() + sep();
        final String format = "%c%d. %s %s" + sep();
        api.printMenu(menu,
                (item, i) -> String.format(format, indicator, i, item, repo.map.get(item).code),
                new StringBuilder(prefix)
        );
    }

    @Override
    public void processRepoCommand(APIWrapper api, String[] args) {
        final Hotkey hotkey = repo.get(args[0]);
        processCommand(hotkey.code);
    }

    @Override
    public void addCommand(APIWrapper api, String[] args) {
    }
}
