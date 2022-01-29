package jPlus.io;

import jPlus.io.security.Access;
import jPlus.io.out.IOWrapper;

public interface APIWrapper extends IOWrapper {
    Access access();
    String username();
}
