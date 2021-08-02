package be.twofold.fcop.handler;

import java.io.*;

public interface FileHandler {

    String getExtension();

    void process(byte[] content, OutputStream out) throws IOException;

}
