package jPlus.lang.callback;

public interface Retrievable2<RET, REC1, REC2> {
    RET retrieve(REC1 r1, REC2 r2);
}
