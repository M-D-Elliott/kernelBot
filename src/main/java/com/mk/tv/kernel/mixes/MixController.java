package com.mk.tv.kernel.mixes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mk.tv.kernel.generic.JacksonRepo;
import com.mk.tv.kernel.generic.RepoCommandController;
import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable1;
import jPlus.lang.callback.Receivable2;
import jPlus.util.lang.IntUtils;
import jPlus.util.map.MapUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateString;

public class MixController extends RepoCommandController {

    private final JacksonRepo<String> repo = new JacksonRepo<>("repos/mixes.txt", new TypeReference<>() {
    }, MapUtils::newLinkedInstance);

    private Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap;
    private Receivable2<APIWrapper, String> iterateCommandsReceiver = this::iterateCommandsAsync;
    public Collection<Receivable1<Boolean>> synchronicityReceivers = new ArrayList<>();

    public MixController(Config config) {
        super(config);
        synchronicityReceivers.add(this::setSynchronous);
    }

    //***************************************************************//

    @Override
    public void readCommands(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        this.commandFuncMap = commandFuncMap;
        super.readCommands(commandFuncMap);
    }

    @Override
    protected void processCommand(APIWrapper api, String[] args) {
        if (config.allowFreeMix && validateString(args, 1) && processCommand(api, args[1])) return;
        super.processCommand(api, args);
    }

    protected boolean processCommand(APIWrapper api, String code) {
        if (code.length() == 0) return false;
        iterateCommandsReceiver.receive(api, code);

        return true;
    }

    @Override
    protected void processRepoCommand(APIWrapper api, String[] args) {
        final String code = repo.get(args[0]);
        processCommand(api, code);
    }

    //***************************************************************//

    private void iterateCommandsAsync(APIWrapper api, String code) {
        new Thread(() -> {
            for (Receivable1<Boolean> rec : synchronicityReceivers) rec.receive(true);
            iterateCommands(api, code);
            for (Receivable1<Boolean> rec : synchronicityReceivers) rec.receive(false);
        }).start();
    }

    private void iterateCommands(APIWrapper api, String code) {
        try {
            final String[] commandNames = code.split(config.nextDelimiterS());
            for (String commandName : commandNames) {
                final Receivable2<APIWrapper, String[]> func = commandFuncMap.get(commandName);
                if (func != null) func.receive(api, new String[]{commandName});
                else if (commandName.charAt(0) == 'w') {
                    final String wvs = commandName.substring(2, commandName.length() - 1);
                    if (IntUtils.canBeParsedAsInt(wvs)) {
                        final int waitValue = Integer.parseInt(wvs);
                        Thread.sleep(waitValue);
                    }
                } else {
                    api.print(String.format(COMMAND_NOT_FOUND, commandName));
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
    protected String entryPointName() {
        return "mix";
    }

    @Override
    protected String commandDesc(String item) {
        return " --  " + repo.map.get(item);
    }

    @Override
    protected Collection<String> commandNames() {
        return repo.map.keySet();
    }

    @Override
    protected String menuPrefix() {
        return "MIX PRESETS";
    }

    @Override
    protected String menuSuffix() {
        return config.displayLiteralCommand("mix scriptName+w(1000)+hotkeyName") + "will run script, wait 1s and press hotkey!";
    }

    public void setSynchronous(Boolean b) {
        iterateCommandsReceiver = b ? this::iterateCommands : this::iterateCommandsAsync;
    }
}
