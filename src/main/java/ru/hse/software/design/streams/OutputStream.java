package ru.hse.software.design.streams;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Class which closes input and creates (and stores) output stream, implements IOStream interface.
 * Contains two PipedInputStream objects as a private fields.
 * Also contains several public methods.
 **/
public class OutputStream implements IOStream {
    private final PipedOutputStream outputStream;
    private final PipedInputStream inputStream;

    /**
     * Creates outputStream from the given inputStream and closes that inputStream.
     * Also catches exceptions and throws RuntimeException in case of creation error.
     *
     * @param inputStream input stream
     **/
    public OutputStream(PipedInputStream inputStream) {
        this.inputStream = inputStream;
        try {
            outputStream = new PipedOutputStream(inputStream);
        } catch (IOException e) {
            try {
                inputStream.close();
            } catch (IOException e1) {
                e.addSuppressed(e1);
            }
            throw new RuntimeException("OutputStream::Exception while creating output stream", e);
        }
    }

    /**
     * Writes string as bytes into the output stream.
     *
     * @param output string to output
     * @throws IOException thrown in case of writing error
     **/
    public void writeAsString(String output) throws IOException {
        outputStream.write(output.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Writes bytes into the output stream.
     *
     * @param bytes bytes to output
     * @throws IOException thrown in case of writing error
     **/
    public void writeAsBytesArray(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    /**
     * Closes the output stream
     *
     * @throws IOException thrown in case of closing error
     **/
    public void close() throws IOException {
        outputStream.close();
    }
}
