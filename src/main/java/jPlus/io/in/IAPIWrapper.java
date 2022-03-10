package jPlus.io.in;

import jPlus.io.out.Access;

public interface IAPIWrapper extends IIOWrapper {
    Access access();

    String username();

    Object payload();

    void send(String endpoint, String message);
}
