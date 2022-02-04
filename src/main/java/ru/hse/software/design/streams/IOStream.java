package ru.hse.software.design.streams;

import java.io.IOException;

/**
 * Interface with a single 'close' method throwing an IOException.
 **/
public interface IOStream {
    /**
     * Closes the stream.
     *
     * @throws IOException thrown in case of closing error
     **/
    void close() throws IOException;
}
