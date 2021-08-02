package be.twofold.fcop.iff;

public final class IffChunk {
    private final int offset;
    private final IffFourCC fourCC;
    private final byte[] content;

    public IffChunk(int offset, IffFourCC fourCC, byte[] content) {
        this.offset = offset;
        this.fourCC = fourCC;
        this.content = content;
    }

    public int getOffset() {
        return offset;
    }

    public IffFourCC getFourCC() {
        return fourCC;
    }

    public byte[] getContent() {
        return content;
    }

    public int getSize() {
        return content.length + 8;
    }

    @Override
    public String toString() {
        return "IffChunk(" +
            "offset=" + offset + ", " +
            "fourCC='" + fourCC + "', " +
            "size=" + getSize() +
            ")";
    }
}
