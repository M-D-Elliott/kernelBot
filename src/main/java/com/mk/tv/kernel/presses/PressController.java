package com.mk.tv.kernel.presses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mk.tv.kernel.generic.JacksonRepo;
import com.mk.tv.kernel.generic.RepoCommandController;
import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable1;
import jPlus.util.awt.KeyEvents;
import jPlus.util.awt.RobotUtils;

import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedHashMap;

import static jPlus.util.io.ConsoleIOUtils.validateString;

public class PressController extends RepoCommandController {

    protected final JacksonRepo<String> repo = new JacksonRepo<>("repos/presses.txt", new TypeReference<>() {
    }, PressController::newHotkeyMap);

    private Receivable1<int[][]> pressReceiver = RobotUtils::pressAsync;

    public PressController(Config config) {
        super(config);
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
    protected void processCommand(APIWrapper api, String[] args) {
        if (config.allowFreePress && validateString(args, 1) && processCommand(args[1])) return;
        super.processCommand(api, args);
    }

    protected boolean processCommand(String commandBody) {
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
    protected void processRepoCommand(APIWrapper api, String[] args) {
        final String presetContent = repo.get(args[0]);
        processCommand(presetContent);
    }

    //***************************************************************//

    @Override
    public char indicator() {
        return 'p';
    }

    @Override
    protected String entryPointName() {
        return "press";
    }

    @Override
    protected Collection<String> commandNames() {
        return repo.map.keySet();
    }

    @Override
    protected String commandDesc(String item) {
        return  " --  " + repo.map.get(item);
    }

    @Override
    protected String menuPrefix() {
        return "PRESS PRESETS";
    }

    @Override
    protected String menuSuffix() {
        return config.displayLiteralCommand("press ctrl+f1") + "will press ctrl and f1 on the host comp!";
    }

    public void setSynchronous(Boolean b) {
        pressReceiver = b ? RobotUtils::press : RobotUtils::pressAsync;
    }
}
