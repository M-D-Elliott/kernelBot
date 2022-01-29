package jPlus.io.out;

import jPlus.lang.callback.Retrievable2;

import java.io.PrintStream;
import java.util.Collection;

public class PrintStreamWrapper implements IOWrapper {
    private final PrintStream ps;

    public PrintStreamWrapper(PrintStream ps) {
        this.ps = ps;
    }


    @Override
    public String in() {
        return null;
    }

    @Override
    public void print(String s) {
        ps.print(s);
    }

    @Override
    public void println(String s) {
        ps.println(s);
    }

    @Override
    public void printMenu(Collection<String> items, Retrievable2<String, String, Integer> itemFormatter, StringBuilder builder) {

    }

    @Override
    public void printMenu(Collection<String> items, Retrievable2<String, String, Integer> itemFormatter, StringBuilder builder, char border) {

    }

    @Override
    public void setStatus(String actString) {

    }
}
