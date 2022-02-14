package com.mk.tv.kernel.presses;

import com.mk.tv.kernel.generic.CommandController;
import com.mk.tv.kernel.generic.CommandService;
import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable1;
import jPlus.lang.callback.Receivable2;
import jPlus.util.awt.KeyEvents;
import jPlus.util.awt.RobotUtils;
import jPlusLibs.generic.IRepo;

import java.text.ParseException;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateString;

public class PressController extends CommandController {

    protected final CommandService<String> service;

    public PressController(Config config) {
        super(config);
        KeyEvents.ADD_DEL = "\\" + config.addDelimiter;
        KeyEvents.NEXT_DEL = "\\" + config.nextDelimiter;

        final IRepo<String> repo = new PressRepo();
        service = new CommandService<>(repo) {
            @Override
            protected void process(APIWrapper api, String[] args) {
                final String presetContent = repo.get(args[0]);
                PressController.this.process(presetContent);
            }
        };
    }

    @Override
    public void read(Map<String, Receivable2<APIWrapper, String[]>> sync,
                     Map<String, Receivable2<APIWrapper, String[]>> async) {
        super.read(async, sync);
        service.read(async, entryPointName(), menu);
    }

    //***************************************************************//

    @Override
    protected void process(APIWrapper api, String[] args) {
        if (config.allowFreePress && validateString(args, 1) && process(args[1])) return;
        super.process(api, args);
    }

    protected boolean process(String commandBody) {
        try {
            final int[][] keyEvents = KeyEvents.parseGroup2D(commandBody);
            RobotUtils.press(keyEvents);

            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    //***************************************************************//

    @Override
    public char indicator() {
        return 'p';
    }

    @Override
    public String entryPointName() {
        return "press";
    }

    @Override
    protected String commandDesc(String item) {
        return " --  " + service.repo.get(item);
    }

    @Override
    protected String menuPrefix() {
        return "PRESS PRESETS";
    }

    @Override
    protected String menuSuffix() {
        return config.displayLiteralCommand("press ctrl+f1") + "will press ctrl and f1 on the host comp!";
    }
}
