package jPlus.io.out;

import jPlus.io.security.Access;
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
    public Object payload() {
        return null;
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
        print(url);
    }

    @Override
    public void send(File f) {
    }

    @Override
    public void setStatus(String status) {
    }

    @Override
    public Receivable1<String> out() {
        return System.out::println;
    }
}
