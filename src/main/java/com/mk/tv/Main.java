package com.mk.tv;

import com.mk.tv.entryPoints.Install;
import com.mk.tv.entryPoints.InstallWizard;
import com.mk.tv.entryPoints.PreInstall;
import com.mk.tv.entryPoints.Run;
import jPlus.io.file.DirUtils;
import jPlus.util.io.ConsoleIOUtils;
import jPlusLibs.com.edu.sphinx.SphinxUtils;

import java.io.File;
import java.nio.file.Paths;

import static jPlusLibs.com.edu.sphinx.SphinxUtils.disableSphinxLogs;

public class Main {
    public static void main(String[] args) {
//        findEntry(args);
        sphinxtest();
    }

    protected static void findEntry(String[] args) {
        if (ConsoleIOUtils.validateChar(args, 0)) {
            final char entry = args[0].toLowerCase().charAt(0);
            switch (entry) {
                case 'e':
                    new Run().run();
                    break;
                case 'w':
                    new InstallWizard(isArg2J(args)).run();
                    break;
                case 'i':
                    new Install(isArg2J(args)).run();
                    break;
                default:
                    new PreInstall().run();
                    break;
            }
        } else new PreInstall().run();
    }

    private static boolean isArg2J(String[] args) {
        return ConsoleIOUtils.validateChar(args, 1) &&
                args[1].toLowerCase().charAt(0) == 'j';
    }

    protected static void sphinxtest() {
        disableSphinxLogs();
        SphinxUtils.conf.setSampleRate(16000);
//        final String uri = Paths.get(DirUtils.fromUserDir("grammar.gram")).toUri().toString();
//        System.out.println(uri);
//        SphinxUtils.conf.setGrammarPath(DirUtils.fromUserDir("grammar.gram"));
        System.out.println(SphinxUtils.audioToTextBliss(new File(DirUtils.fromUserDir("repos/audiotests/kb.wav"))));
        System.out.println(SphinxUtils.audioToTextBliss(new File(DirUtils.fromUserDir("repos/audiotests/kbcap.wav"))));
        System.out.println(SphinxUtils.audioToTextBliss(new File(DirUtils.fromUserDir("repos/audiotests/kbhelp.wav"))));

        System.out.println(SphinxUtils.audioToTextBliss(new File(DirUtils.fromUserDir("repos/audiotests/kernelbot.wav"))));
        System.out.println(SphinxUtils.audioToTextBliss(new File(DirUtils.fromUserDir("repos/audiotests/kernelbotcap.wav"))));
        System.out.println(SphinxUtils.audioToTextBliss(new File(DirUtils.fromUserDir("repos/audiotests/kernelbothelp.wav"))));

        System.out.println(SphinxUtils.audioToTextBliss(new File(DirUtils.fromUserDir("repos/audiotests/cap.wav"))));
        System.out.println(SphinxUtils.audioToTextBliss(new File(DirUtils.fromUserDir("repos/audiotests/help.wav"))));
    }

}
