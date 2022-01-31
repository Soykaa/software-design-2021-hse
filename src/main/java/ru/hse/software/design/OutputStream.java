package ru.hse.software.design;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

public class OutputStream implements IOStream {
    private final PipedOutputStream outputStream;
    private final PipedInputStream inputStream;

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

    public void writeAsString(String output) throws IOException {
        outputStream.write(output.getBytes(StandardCharsets.UTF_8));
    }

    public void writeAsBytesArray(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    public void close() throws IOException {
        outputStream.close();
    }
}
