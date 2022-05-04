package com.mk.tv.io.spring;

import com.mk.tv.io.generic.IAPIWrapper;
import jPlus.io.Access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RESTAPIWrapper implements IAPIWrapper {

    private final String in;
    private final List<String> out = new ArrayList<>();
    private final Runnable onFinish;
    private final String userName;

    public RESTAPIWrapper(String in) {
        this(in, () -> {
        });
    }

    public RESTAPIWrapper(String in, Runnable onFinish) {
        this(in, null, onFinish);
    }

    public RESTAPIWrapper(String in, String userName, Runnable onFinish) {
        this.in = in;
        this.userName = userName == null ? "admin" : userName;
        this.onFinish = onFinish;
    }

    @Override
    public Access access() {
        return Access.PRIVATE;
    }

    @Override
    public String username() {
        return this.userName;
    }

    @Override
    public String in() {
        return this.in;
    }

    @Override
    public void print(String s) {
        final String[] split = s.split(System.lineSeparator());
        Collections.addAll(out, split);
    }

    @Override
    public void onFinish() {
        if (onFinish != null) this.onFinish.run();
    }

    @Override
    public void send(String endpoint, String message){
        println(message);
    }

    @Override
    public void println(String s) {
        print(s);
    }

    @Override
    public void printLink(String url) {
        println("<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>");
    }

    public String[] getOut() {
        return out.toArray(new String[0]);
    }
}
