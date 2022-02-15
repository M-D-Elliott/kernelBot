package jPlus.io.out;

import jPlus.lang.callback.Receivable1;

import java.io.PrintStream;

public class PrintStreamWrapper implements IIOWrapper {
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
    public void setStatus(String actString) {

    }

    @Override
    public Receivable1<String> out() {
        return System.out::println;
    }
}
