package jPlus.io.out;

import jPlus.io.security.Access;

public interface IAPIWrapper extends IIOWrapper {
    Access access();

    String username();
}
