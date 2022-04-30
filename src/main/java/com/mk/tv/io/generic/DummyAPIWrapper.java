package com.mk.tv.io.generic;

import jPlus.io.Priority;
import jPlus.io.Access;
import jPlus.lang.callback.Receivable1;

import java.io.File;

public class DummyAPIWrapper implements IAPIWrapper {
    @Override
    public Access access() {
        return Access.PRIVATE;
    }

    @Override
    public String username() {
        return "dummy";
    }

    @Override
    public void send(String endpoint, String message) {
    }

    @Override
    public String in() {
        return "";
    }

    @Override
    public void print(String s) {
        System.out.print(s);
    }

    @Override
    public void println(String s) {
        System.out.println(s);
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
}
