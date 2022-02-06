package ru.hse.software.design.streams;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Class which closes output and creates (and stores) input stream, implements IOStream interface.
 * Contains PipedInputStream object as a private field.
 * Also contains several public methods.
 **/
public class InputStream implements IOStream {
    private final PipedInputStream inputStream;

    /**
     * Creates inputStream from the given outputStream and closes that outputStream.
     * Also catches exceptions and throws RuntimeException in case of creation error.
     *
     * @param outputStream output stream
     **/
    public InputStream(PipedOutputStream outputStream) {
        try {
            inputStream = new PipedInputStream(outputStream);
        } catch (IOException e) {
            try {
                outputStream.close();
            } catch (IOException e1) {
                e.addSuppressed(e1);
            }
            throw new RuntimeException("InputStream::Exception while creating input stream", e);
        }
    }

    /**
     * Reads bytes from the input stream as string.
     *
     * @return Read bytes as string
     * @throws IOException thrown in case of reading error
     **/
    public String readAsString() throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Reads bytes from the input stream.
     *
     * @return Read bytes
     * @throws IOException thrown in case of reading error
     **/
    public byte[] readAsBytesArray() throws IOException {
        return inputStream.readAllBytes();
    }

    /**
     * Closes the input stream.
     *
     * @throws IOException thrown in case of closing error
     **/
    public void close() throws IOException {
        inputStream.close();
    }
}
