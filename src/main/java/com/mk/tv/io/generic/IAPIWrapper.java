package com.mk.tv.io.generic;

import jPlus.io.Access;

public interface IAPIWrapper extends IIOWrapper {
    Access access();

    String username();

    default Object payload(){
        return "";
    }

    default void send(String endpoint, String message){}
}
