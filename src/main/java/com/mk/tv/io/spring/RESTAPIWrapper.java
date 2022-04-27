package com.mk.tv.io.spring;

import com.mk.tv.io.generic.IAPIWrapper;
import jPlus.io.Access;
import jPlus.lang.callback.Receivable1;

import java.io.File;

public class RESTAPIWrapper implements IAPIWrapper {

    public final String in;
    public String out = "";

    public RESTAPIWrapper(String in) {
        this.in = in;
    }

    @Override
    public Access access() {
        return null;
    }

    @Override
    public String username() {
        return null;
    }

    @Override
    public String in() {
        return this.in;
    }

    @Override
    public Receivable1<String> out() {
        return null;
    }

    @Override
    public void print(String s) {

    }

    @Override
    public void println(String s) {

    }

    @Override
    public void printLink(String url) {

    }
}
