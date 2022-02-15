package jPlus.io.out;

import jPlus.lang.callback.Receivable1;

public interface IIOWrapper {
    String in();

    void print(String s);

    void println(String s);

    void setStatus(String actString);

    Receivable1<String> out();
}
