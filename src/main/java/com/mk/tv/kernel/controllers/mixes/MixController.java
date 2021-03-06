package com.mk.tv.kernel.controllers.mixes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mk.tv.io.generic.IAPIWrapper;
import com.mk.tv.kernel.controllers.FuncController;
import com.mk.tv.kernel.controllers.FuncService;
import com.mk.tv.kernel.generic.Config;
import jPlus.lang.callback.Receivable2;
import jPlus.util.lang.IntUtils;
import jPlus.util.map.MapUtils;
import jPlusLibs.com.fasterxml.jackson.JacksonRepo;
import jPlusLibs.generic.IRepo;

import java.util.Map;

import static com.mk.tv.kernel.controllers.FuncService.FUNC_NOT_FOUND;
import static jPlus.util.io.ConsoleIOUtils.validateString;

public class MixController extends FuncController {

    protected final FuncService<String> service;

    private Map<String, Receivable2<IAPIWrapper, String[]>> sync;
    private Map<String, Receivable2<IAPIWrapper, String[]>> async;

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
        this.sync = sync;
        this.async = async;
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

        final String[] funcNames = code.split(config.system.nextDelimiterS());
        try {
            for (String funcName : funcNames) {

                Receivable2<IAPIWrapper, String[]> func = async.get(funcName);
                if (func != null) func.receive(api, new String[]{funcName});
                else if (funcName.charAt(0) == 'w') {
                    final String wvs = funcName.substring(2, funcName.length() - 1);
                    if (IntUtils.canBeParsedAsInt(wvs)) {
                        final int wait = Integer.parseInt(wvs);
                        Thread.sleep(wait);
                    }
                } else {
                    func = sync.get(funcName);
                    if (func != null) func.receive(api, new String[]{funcName});
                    else api.print(String.format(FUNC_NOT_FOUND, funcName));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        return config.system.displayLiteralCommand("mix scriptName+w(1000)+hotkeyName") + "will run script, wait 1s and press hotkey!";
    }
}
