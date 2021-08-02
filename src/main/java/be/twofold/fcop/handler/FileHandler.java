package be.twofold.fcop.handler;

import be.twofold.fcop.iff.*;

import java.io.*;

public interface FileHandler {

    IffFourCC getFourCC();

    String getExtension();

    void process(byte[] content, OutputStream out) throws IOException;

}
