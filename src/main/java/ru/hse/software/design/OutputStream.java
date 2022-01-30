package ru.hse.software.design;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

public class OutputStream implements IOStream {
    private final PipedOutputStream outputStream;

    public OutputStream(PipedOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeAsString(String output) throws IOException {
        outputStream.write(output.getBytes(StandardCharsets.UTF_8));
    }

    public void writeAsBytesArray(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }
}
