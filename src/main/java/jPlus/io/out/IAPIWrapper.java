package jPlus.io.out;

import jPlus.io.security.Access;
import jPlus.io.out.IIOWrapper;

public interface IAPIWrapper extends IIOWrapper {
    Access access();
    String username();
}
