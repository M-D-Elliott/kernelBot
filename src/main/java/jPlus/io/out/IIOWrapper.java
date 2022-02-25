package jPlus.io.out;

import jPlus.lang.callback.Receivable1;

import java.io.File;

public interface IIOWrapper {
    String in();

    Receivable1<String> out();

    void print(String s);

    void println(String s);

    void printLink(String url);

    void send(File f);

    void setStatus(String status);
}
