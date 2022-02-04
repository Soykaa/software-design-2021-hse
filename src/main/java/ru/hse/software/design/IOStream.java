package ru.hse.software.design;

import java.io.IOException;

/**
 * Interface with a single 'close' method throwing an IOException.
 **/
public interface IOStream {
    void close() throws IOException;
}
