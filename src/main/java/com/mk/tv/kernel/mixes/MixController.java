package com.mk.tv.kernel.mixes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mk.tv.kernel.generic.ICommandController;
import com.mk.tv.kernel.generic.JacksonRepo;
import com.mk.tv.kernel.system.Config;
import jPlus.io.APIWrapper;
import jPlus.lang.callback.Receivable2;
import jPlus.util.io.ConsoleUtils;
import jPlus.util.lang.IntUtils;
import jPlus.util.map.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jPlus.util.io.ConsoleIOUtils.validateString;
import static jPlus.util.io.ConsoleUtils.sep;

public class MixController implements ICommandController {

    private Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap;

    private final JacksonRepo<String> repo = new JacksonRepo<>("repos/mixes.txt", new TypeReference<>() {
    }, MapUtils::newLinkedInstance);

    private final Config config;

    private final List<String> menu = new ArrayList<>();

    private static String COMMAND_NOT_FOUND = "%$1s not found";

    public MixController(Config config) {
        this.config = config;
    }

    @Override
    public void readCommands(Map<String, Receivable2<APIWrapper, String[]>> commandFuncMap) {
        this.commandFuncMap = commandFuncMap;
        commandFuncMap.put("mix", this::processCommand);

        final Receivable2<APIWrapper, String[]> command = this::processRepoCommand;
        for (String commandName : repo.map.keySet()) commandFuncMap.put(commandName, command);
        menu.addAll(repo.map.keySet());
    }

    private void processRepoCommand(APIWrapper api, String[] args) {
        final String code = repo.get(args[0]);
        processCommand(api, code);
    }

    @Override
    public char indicator() {
        return 'm';
    }

    public void processCommand(APIWrapper api, String[] args) {
        if (validateString(args, 1) && processCommand(api, args[1])) return;
        menuResponse(api, args);
    }

    public boolean processCommand(APIWrapper api, String code) {
        if (code.length() == 0) return false;

        new Thread(() -> {
            try {
                final String[] commandNames = code.split("\\+");
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
        }).start();

        return true;
    }

    @Override
    public void menuResponse(APIWrapper api, String[] args) {
        final String sep = sep();
        final String prefix = sep + "MIX PRESETS" + sep;

        final String format = " %c%d  " + config.border + "        %s --  %s        ";
        final String[] menuFormatted = new String[menu.size()];
        for (int i = 0; i < menuFormatted.length; i++) {
            final String item = menu.get(i);
            menuFormatted[i] = String.format(format, indicator(), i, item, repo.map.get(item));
        }
        final String body = ConsoleUtils.encaseInBanner(menuFormatted, config.border);

        final String suffix = sep + config.displayLiteralCommand("mix scriptName+w(1000)+hotkeyName") + "will run script, wait 1s and press hotkey!"; //+

        api.print(prefix + body + suffix);
    }

    @Override
    public List<String> menu() {
        return menu;
    }
}
