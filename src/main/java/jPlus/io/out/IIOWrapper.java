package jPlus.io.out;

import jPlus.lang.callback.Receivable1;

import java.io.File;

public interface IIOWrapper {
    String in();

    void print(String s);

    void println(String s);

    void printUnf(String s);

    void printLink(String url);

    void sendFile(File f);

    void setStatus(String actString);

    Receivable1<String> out();
}
