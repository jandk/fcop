package be.twofold.fcop.handler;

import be.twofold.fcop.util.*;

import javax.imageio.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;

public final class CanmHandler implements FileHandler {

    private static final int PaletteSize = 256;

    @Override
    public String getExtension() {
        return "gif";
    }

    @Override
    public void process(byte[] content, OutputStream out) throws IOException {
        ByteBuffer buffer = ByteBuffer
            .wrap(content)
            .order(ByteOrder.LITTLE_ENDIAN);

        int count = buffer.getInt();
        IndexColorModel model = readPalette(buffer);

        try (ImageOutputStream imageOut = ImageIO.createImageOutputStream(out)) {
            GifSequenceWriter writer = new GifSequenceWriter(imageOut, 50, model);
            for (int i = 0; i < count; i++) {
                writer.writeToSequence(createImage(buffer, model));
            }
            writer.close();
        }
    }

    private IndexColorModel readPalette(ByteBuffer buffer) {
        byte[] r = new byte[PaletteSize];
        byte[] g = new byte[PaletteSize];
        byte[] b = new byte[PaletteSize];
        for (int i = 0; i < PaletteSize; i++) {
            short color = buffer.getShort();

            int rr = (color & 0x7c00) >>> 10;
            int gg = (color & 0x03e0) >>> 5;
            int bb = (color & 0x001f);

            r[i] = (byte) (rr << 3 | rr >>> 2);
            g[i] = (byte) (gg << 3 | gg >>> 2);
            b[i] = (byte) (bb << 3 | bb >>> 2);
        }
        return new IndexColorModel(8, PaletteSize, r, g, b);
    }

    private BufferedImage createImage(ByteBuffer buffer, IndexColorModel model) {
        BufferedImage image = new BufferedImage(64, 48, BufferedImage.TYPE_BYTE_INDEXED, model);
        DataBufferByte dataBuffer = (DataBufferByte) image.getRaster().getDataBuffer();
        byte[] data = dataBuffer.getData();

        for (int r = 0; r < 12; r++) {
            for (int t = 0; t < 4; t++) {
                int y = t * 12 + r;
                buffer.get(data, y * 64, 64);
            }
        }

        return image;
    }

}
