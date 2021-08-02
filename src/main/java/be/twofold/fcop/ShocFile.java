package be.twofold.fcop;

import be.twofold.fcop.iff.*;

import java.util.*;

public final class ShocFile {

    private final int sequence;
    private final IffFourCC fourCC;
    private final byte[] content;
    private final String filename;

    public ShocFile(int sequence, IffFourCC fourCC, byte[] content, String filename) {
        this.sequence = sequence;
        this.fourCC = fourCC;
        this.content = content;
        this.filename = filename;
    }

    public int getSequence() {
        return sequence;
    }

    public IffFourCC getFourCC() {
        return fourCC;
    }

    public byte[] getContent() {
        return content;
    }

    public Optional<String> getFilename() {
        return Optional.ofNullable(filename);
    }

    @Override
    public String toString() {
        return "ShocFile(" +
            "sequence=" + sequence + ", " +
            "fourCC=" + fourCC + ", " +
            "size=" + content.length + ", " +
            "filename=" + filename +
            ")";
    }

}
