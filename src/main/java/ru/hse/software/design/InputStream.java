package ru.hse.software.design;

import java.io.IOException;
import java.io.PipedInputStream;
import java.nio.charset.StandardCharsets;

public class InputStream implements IOStream {
    private final PipedInputStream inputStream;

    public InputStream(PipedInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String readAsString() throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public byte[] readAsBytesArray() throws IOException {
        return inputStream.readAllBytes();
    }
}
