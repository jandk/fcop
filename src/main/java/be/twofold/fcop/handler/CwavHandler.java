package be.twofold.fcop.handler;

import be.twofold.fcop.iff.*;

import java.io.*;

public final class CwavHandler implements FileHandler {

    @Override
    public IffFourCC getFourCC() {
        return IffFourCC.Cwav;
    }

    @Override
    public String getExtension() {
        return "wav";
    }

    @Override
    public void process(byte[] content, OutputStream out) throws IOException {
        out.write(content);
    }

}
