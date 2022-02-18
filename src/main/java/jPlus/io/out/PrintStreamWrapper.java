package jPlus.io.out;

import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable1;

import java.io.File;
import java.io.PrintStream;

public class PrintStreamWrapper implements IAPIWrapper {
    private final PrintStream ps;
    private String in = "";

    public PrintStreamWrapper(PrintStream ps) {
        this.ps = ps;
    }

    //***************************************************************//

    @Override
    public String in() {
        return in;
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
    public void printUnf(String s) {
        print(s);
    }

    @Override
    public void printLink(String url) {
        print(url);
    }

    @Override
    public void sendFile(File f) {
    }

    @Override
    public void setStatus(String actString) {

    }

    @Override
    public Receivable1<String> out() {
        return System.out::println;
    }

    //***************************************************************//

    public void setIn(String in) {
        this.in = in;
    }

    @Override
    public Access access() {
        return Access.PRIVATE;
    }

    @Override
    public String username() {
        return "admin";
    }
}
