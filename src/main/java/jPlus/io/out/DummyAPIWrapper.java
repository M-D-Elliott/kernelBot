package jPlus.io.out;

import jPlus.io.security.Access;
import jPlus.lang.callback.Receivable1;

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
    public void printUnf(String s) {
        System.out.print(s);
    }

    @Override
    public void printLink(String url) {
        print(url);
    }

    @Override
    public void setStatus(String actString) {
    }

    @Override
    public Receivable1<String> out() {
        return System.out::println;
    }
}