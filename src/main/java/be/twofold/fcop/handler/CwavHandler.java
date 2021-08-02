package be.twofold.fcop.handler;

import java.io.*;

public final class CwavHandler implements FileHandler {

    @Override
    public String getExtension() {
        return "wav";
    }

    @Override
    public void process(byte[] content, OutputStream out) throws IOException {
        out.write(content);
    }

}
