package jPlus.lang.callback;

public interface Retrievable3<RET, REC1, REC2, REC3>  {
    RET retrieve(REC1 r1, REC2 r2, REC3 r3);
}
