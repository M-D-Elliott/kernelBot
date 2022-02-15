package com.mk.tv.kernel.mixes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mk.tv.kernel.Config;
import com.mk.tv.kernel.generic.FuncController;
import com.mk.tv.kernel.generic.FuncService;
import jPlus.io.out.IAPIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.lang.IntUtils;
import jPlus.util.map.MapUtils;
import jPlusLibs.generic.IRepo;
import jPlusLibs.jackson.JacksonRepo;

import java.util.Map;

import static com.mk.tv.kernel.generic.FuncService.FUNC_NOT_FOUND;
import static jPlus.util.io.ConsoleIOUtils.validateString;

public class MixController extends FuncController {

    protected final FuncService<String> service;

    private Map<String, Receivable2<IAPIWrapper, String[]>> commandFuncMap;

    public MixController(Config config) {
        super(config);

        final IRepo<String> repo = new JacksonRepo<>("repos/mixes.txt", new TypeReference<>() {
        }, MapUtils::newLinkedInstance);
        service = new FuncService<>(repo) {
            @Override
            protected void process(IAPIWrapper api, String[] args) {
                final String code = repo.get(args[0]);
                MixController.this.process(api, code);
            }
        };
    }

    //***************************************************************//

    @Override
    public void read(Map<String, Receivable2<IAPIWrapper, String[]>> sync,
                     Map<String, Receivable2<IAPIWrapper, String[]>> async) {
        this.commandFuncMap = async;
        super.read(async, sync);
        service.read(async, menuName(), menu);
    }

    @Override
    protected void process(IAPIWrapper api, String[] args) {
        if (config.mix.allowFreeMix && validateString(args, 1) && process(api, args[1])) return;
        super.process(api, args);
    }

    protected boolean process(IAPIWrapper api, String code) {
        if (code.length() == 0) return false;
        iterateCommands(api, code);

        return true;
    }

    //***************************************************************//

    private void iterateCommands(IAPIWrapper api, String code) {
        try {
            final String[] commandNames = code.split(config.nextDelimiterS());
            for (String commandName : commandNames) {
                final Receivable2<IAPIWrapper, String[]> func = commandFuncMap.get(commandName);
                if (func != null) func.receive(api, new String[]{commandName});
                else if (commandName.charAt(0) == 'w') {
                    final String wvs = commandName.substring(2, commandName.length() - 1);
                    if (IntUtils.canBeParsedAsInt(wvs)) {
                        final int waitValue = Integer.parseInt(wvs);
                        Thread.sleep(waitValue);
                    }
                } else {
                    api.print(String.format(FUNC_NOT_FOUND, commandName));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //***************************************************************//

    @Override
    public char indicator() {
        return 'm';
    }

    @Override
    public String menuName() {
        return "mix";
    }

    @Override
    protected String funcDesc(String item) {
        return " --  " + service.repo.get(item);
    }

    @Override
    protected String menuPrefix() {
        return "MIX PRESETS";
    }

    @Override
    protected String menuSuffix() {
        return config.displayLiteralCommand("mix scriptName+w(1000)+hotkeyName") + "will run script, wait 1s and press hotkey!";
    }
}
