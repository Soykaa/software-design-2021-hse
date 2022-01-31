package ru.hse.software.design;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

public class InputStream implements IOStream {
    private final PipedInputStream inputStream;

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

    public String readAsString() throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public byte[] readAsBytesArray() throws IOException {
        return inputStream.readAllBytes();
    }

    public void close() throws IOException {
        inputStream.close();
    }
}
