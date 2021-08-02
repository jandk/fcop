package be.twofold.fcop.handler;

import be.twofold.fcop.iff.*;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;

public final class CbmpHandler implements FileHandler {

    private static final int Width = 256;
    private static final int Height = 256;

    @Override
    public IffFourCC getFourCC() {
        return IffFourCC.Cbmp;
    }

    @Override
    public String getExtension() {
        return "png";
    }

    @Override
    public void process(byte[] content, OutputStream out) throws IOException {
        try (IffReader reader = new IffReader(new ByteArrayInputStream(content))) {
            decodeBitmap(reader, out);
        }
    }

    private void decodeBitmap(IffReader reader, OutputStream out) throws IOException {
        IffChunk chunk = reader.stream()
            .filter(iffChunk -> iffChunk.getFourCC() == IffFourCC.PX16)
            .findFirst()
            .orElseThrow(() -> new IOException("Could not find PX16 chunk"));

        ShortBuffer pixels = ByteBuffer
            .wrap(chunk.getContent())
            .order(ByteOrder.LITTLE_ENDIAN)
            .asShortBuffer();

        BufferedImage image = new BufferedImage(Width, Height, BufferedImage.TYPE_USHORT_555_RGB);
        DataBufferUShort buffer = ((DataBufferUShort) image.getRaster().getDataBuffer());
        pixels.get(buffer.getData());

        ImageIO.write(image, "png", out);
    }

}
