package be.twofold.fcop.chunk;

import be.twofold.fcop.iff.*;
import be.twofold.fcop.util.*;

import java.nio.charset.*;

public final class SwvrChunk extends Chunk {

    private final String filename;

    private SwvrChunk(String filename) {
        this.filename = filename;
    }

    public static SwvrChunk parse(byte[] content) {
        int end = ArrayUtils.indexOf(content, (byte) 0, 12, content.length);
        String filename = new String(content, 12, end - 12, StandardCharsets.ISO_8859_1);
        return new SwvrChunk(filename.trim());
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public IffFourCC getType() {
        return IffFourCC.SWVR;
    }

}
