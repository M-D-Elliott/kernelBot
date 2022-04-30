package com.mk.tv.io.console;

import com.mk.tv.io.generic.IAPIWrapper;
import com.mk.tv.io.generic.IIOWrapper;
import jPlus.io.Priority;
import jPlus.io.Access;
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
    public void printLink(String url) {
        println(url);
    }

    @Override
    public void send(File f) {
    }

    @Override
    public IIOWrapper setPriority(Priority priority) {
        return this;
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

    @Override
    public Object payload() {
        return in;
    }

    @Override
    public void send(String endpoint, String message) {

    }
}
