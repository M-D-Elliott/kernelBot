package com.mk.tv.io.generic;

import jPlus.io.Priority;
import jPlus.lang.callback.Receivable1;

import java.io.File;

public interface IIOWrapper {
    String in();

    default Receivable1<String> out() {
        return this::print;
    }

    void print(String s);

    void println(String s);

    void printLink(String url);

    default void send(File f) {
    }

    default void onFinish() {}

    default IIOWrapper setPriority(Priority priority) {
        return this;
    }

    default IIOWrapper setStatus(String status) {
        return this;
    }
}
