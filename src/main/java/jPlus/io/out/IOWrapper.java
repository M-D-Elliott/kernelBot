package jPlus.io.out;

import jPlus.lang.callback.Retrievable2;

import java.util.Collection;

public interface IOWrapper {
    String in();

    void print(String s);

    void println(String s);

    void setStatus(String actString);
}
