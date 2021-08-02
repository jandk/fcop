package be.twofold.fcop.chunk;

import java.nio.*;

public final class CtrlChunk {

    private final int[] offsets;

    private CtrlChunk(int[] offsets) {
        this.offsets = offsets;
    }

    public static CtrlChunk parse(byte[] content) {
        IntBuffer buffer = ByteBuffer.wrap(content)
            .order(ByteOrder.LITTLE_ENDIAN)
            .asIntBuffer();

        int[] offsets = new int[4];
        buffer.get(offsets);

        return new CtrlChunk(offsets);
    }

}
