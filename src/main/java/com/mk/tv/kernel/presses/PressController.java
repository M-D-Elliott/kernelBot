package com.mk.tv.kernel.presses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mk.tv.kernel.generic.IRepoCommandController;
import com.mk.tv.kernel.generic.JacksonRepo;
import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable1;
import jPlus.lang.callback.Receivable2;
import jPlus.util.awt.KeyEvents;
import jPlus.util.awt.RobotUtils;
import jPlus.util.io.ConsoleUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.ConsoleUtils.sep;

public class PressController implements IRepoCommandController {

    private final Config config;
    private final List<String> menu = new ArrayList<>();

    private final JacksonRepo<String> repo = new JacksonRepo<>("repos/presses.txt", new TypeReference<>() {
    }, PressController::newHotkeyMap);

    private Receivable1<int[][]> pressReceiver = RobotUtils::pressAsync;

    public PressController(Config config) {
        this.config = config;
        KeyEvents.ADD_DEL = "\\" + config.addDelimiter;
        KeyEvents.NEXT_DEL = "\\" + config.nextDelimiter;
    }

    //***************************************************************//

    private static LinkedHashMap<String, String> newHotkeyMap() {
        final LinkedHashMap<String, String> ret = new LinkedHashMap<>();
        final String preset = "a+b";
        ret.put("ab", preset);
        return ret;
    }

    //***************************************************************//

    @Override
    public void readCommands(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        commandFuncMap.put("addpress", this::addCommand);
        commandFuncMap.put("press", this::processCommand);

        final Receivable2<APIWrapper, String[]> presetCommand = this::processRepoCommand;
        for (String presetName : repo.map.keySet()) commandFuncMap.put(presetName, presetCommand);
        menu.addAll(repo.map.keySet());
    }

    @Override
    public char indicator() {
        return 'p';
    }

    public void processCommand(APIWrapper api, String[] args) {
        if (config.allowFreePress && validateString(args, 1) && processCommand(args[1])) return;
        menuResponse(api, args);
    }

    public boolean processCommand(String commandBody) {
        try {
            final int[][] keyEvents = KeyEvents.parseGroup2D(commandBody);

            pressReceiver.receive(keyEvents);

            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void menuResponse(APIWrapper api, String[] args) {
        final String sep = sep();

        final String prefix = sep + "PRESS PRESETS" + sep;

        final String format = " %c%d  " + config.menuBorder + "        %s --  %s        ";
        final String[] menuFormatted = new String[menu.size()];
        for (int i = 0; i < menuFormatted.length; i++) {
            final String item = menu.get(i);
            menuFormatted[i] = String.format(format, indicator(), i, item, repo.map.get(item));
        }
        final String body = ConsoleUtils.encaseInBanner(menuFormatted, config.menuBorder);

        final String suffix = sep + config.displayLiteralCommand("press ctrl+f1") + "will press ctrl and f1 on the host comp!"; //+

        api.print(prefix + body + suffix);
    }

    @Override
    public List<String> menu() {
        return menu;
    }

    @Override
    public void processRepoCommand(APIWrapper api, String[] args) {
        final String presetContent = repo.get(args[0]);
        processCommand(presetContent);
    }

    @Override
    public void addCommand(APIWrapper api, String[] args) {
    }

    public void setSynchronous(Boolean b) {
        pressReceiver = b ? RobotUtils::press : RobotUtils::pressAsync;
    }
}
