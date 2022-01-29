package jPlus.io.out;

import jPlus.lang.callback.Retrievable2;

import java.util.Collection;

public interface IOWrapper {
    String in();

    void print(String s);

    void println(String s);

    void printMenu(Collection<String> items,
                   Retrievable2<String, String, Integer> itemFormatter,
                   StringBuilder builder);

    void printMenu(Collection<String> items,
                   Retrievable2<String, String, Integer> itemFormatter,
                   StringBuilder builder,
                   char border);

    void setStatus(String actString);
}
