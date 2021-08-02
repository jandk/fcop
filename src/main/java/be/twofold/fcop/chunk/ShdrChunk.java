package be.twofold.fcop.chunk;

import be.twofold.fcop.iff.*;

import java.nio.*;

public final class ShdrChunk {

    private final IffFourCC fourCC;
    private final int sequence;

    private ShdrChunk(IffFourCC fourCC, int sequence) {
        this.fourCC = fourCC;
        this.sequence = sequence;
    }

    public static ShdrChunk parse(byte[] content) {
        ByteBuffer buffer = ByteBuffer
            .wrap(content)
            .order(ByteOrder.LITTLE_ENDIAN);

        IffFourCC fourCC = IffFourCC.fromCode(buffer.getInt(16));
        int sequence = buffer.getInt(20);

        return new ShdrChunk(fourCC, sequence);
    }

    public IffFourCC getFourCC() {
        return fourCC;
    }

    public int getSequence() {
        return sequence;
    }
}
