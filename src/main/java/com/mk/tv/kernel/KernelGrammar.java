package com.mk.tv.kernel;

import jPlus.io.file.DirUtils;
import jPlus.io.file.FileUtils;

public class KernelGrammar {

    public static void write(String[] indicators, String[] funcNames) {
        final String sep = System.lineSeparator();
        final String data = header() +
                publicField("indicator", indicators) + sep +
                publicField("func", funcNames, "<indicator>{1} ", "{1}");
        FileUtils.write(data, DirUtils.fromUserDir("grammar.gram"));
    }

    private static String header() {
        final String sep = System.lineSeparator();
        final String sepsep = sep + sep;
        return "#JSGF V1.0;" + sepsep +
                "/**" + sep +
                " * JSGF Grammar" + sep +
                " */" + sepsep +
                "grammar grammar;" + sep;
    }

    private static String publicField(String name, String[] values) {
        return publicField(name, values, "", "");
    }

    private static String publicField(String name, String[] values, String prefix, String suffix) {
        final String sep = System.lineSeparator();
        final String sepsep = sep + sep;
        return sepsep +
                "public <" +
                name +
                ">  = " +
                prefix +
                '(' + sepsep +
                String.join(" | ", values) + sepsep +
                ')' +
                suffix + ';';
    }
}
