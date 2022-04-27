package jPlus.io.in;

import jPlus.io.out.Access;

public interface IAPIWrapper extends IIOWrapper {
    Access access();

    String username();

    default Object payload(){
        return "";
    }

    void send(String endpoint, String message);
}
