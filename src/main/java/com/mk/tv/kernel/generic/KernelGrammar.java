package com.mk.tv.kernel.generic;

import com.mk.tv.kernel.Kernel;
import jPlus.io.file.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class KernelGrammar {

    public static void write(Kernel kernel, String path) {
        final Collection<String> functions = new ArrayList<>();
        functions.addAll(kernel.getSyncFunctions().keySet());
        functions.addAll(kernel.getAsyncFunctions().keySet());
        KernelGrammar.write(new String[]{"kay bee", "kernel bot"}, functions.toArray(new String[0]), path);
    }

    public static void write(String[] indicators, String[] funcNames, String path) {
        final String sep = System.lineSeparator();
        final String data = header() +
                publicField("distractions",
                        "entry | envy | scab | crab | scrap | tap | mister | bill | tell | firm | fuse | pass | crypt | express | impress | fix | big | wax | max | start | sin | fill | bath | curve | zap | agriculture | horror | perfect | " +
                                "yes | yeah | maybe | no | nope | never | ever | bigger | rhyme | time | timeout | believe | earnest | not | test"
                ) + sep +
                publicField("distractionFuncs", funcNames) + sep +
                publicField("indicator", indicators) + sep +
                publicField("func", funcNames, "<indicator>{1} ", "{1}");
        FileUtils.write(data, path + File.separatorChar + "grammar.gram");
    }

    public static String header() {
        final String sep = System.lineSeparator();
        final String sepsep = sep + sep;
        return "#JSGF V1.0;" + sepsep +
                "/**" + sep +
                " * JSGF Grammar" + sep +
                " */" + sepsep +
                "grammar grammar;" + sep;
    }

    public static String publicField(String name, String values) {
        return publicField(name, values, "", "");
    }

    public static String publicField(String name, String[] values) {
        return publicField(name, values, "", "");
    }

    public static String publicField(String name, String[] values, String prefix, String suffix) {
        return publicField(name, String.join(" | ", values), prefix, suffix);
    }

    public static String publicField(String name, String value, String prefix, String suffix) {
        final String sep = System.lineSeparator();
        final String sepsep = sep + sep;
        return sepsep +
                "public <" +
                name +
                ">  = " +
                prefix +
                '(' + sepsep +
                value + sepsep +
                ')' +
                suffix + ';';
    }
}
