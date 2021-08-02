package be.twofold.fcop.test;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;

public class CtilTest {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\Jan\\Desktop\\Source\\fcop\\output\\Ctil\\");
        Files.list(path)
            .filter(p -> p.getFileName().toString().endsWith(".Ctil"))
            .forEach(CtilTest::readTil);
    }

    private static void readTil(Path path) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        int fourCC = buffer.getInt();
        int length = buffer.getInt();
        short s1 = buffer.getShort();
        short s2 = buffer.getShort();
        short x3ca = buffer.getShort(0x3ca);
        int o1 = 0x5d0 + x3ca * 4;
        int o2 = o1 + s1 * 2;
        int o3 = o2 + s2 * 2;

//        esi = esi * 4 + 0x5d0;
//        eax >>>= 0x10;
//        eax = esi + eax * 2;
//        edx = eax + edx * 2;


        System.out.printf("%s\t%s\t%s\t%04x\t%04x\t%04x%n", path.getFileName(), s1, s2, o1, o2, o3);
        BufferedImage image = new BufferedImage(17, 17, BufferedImage.TYPE_3BYTE_BGR);
        DataBufferByte dataBuffer = (DataBufferByte) image.getRaster().getDataBuffer();
        buffer.get(dataBuffer.getData());

        byte[] data = dataBuffer.getData();
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) Byte.toUnsignedInt(data[i]);
        }
//        for (int i = 0; i < data.length; i += 3) {
//            data[i + 0] = 0;
//            data[i + 1] = 0;
//            data[i + 2] = 0;
//        }

        BufferedImage newImage = new BufferedImage(image.getWidth() * 16, image.getHeight() * 16, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D) newImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(image, 0, 0, newImage.getWidth(), newImage.getHeight(), null);

        try {
            ImageIO.write(newImage, "png", new File("output\\heightmap\\" + path.getFileName() + ".png"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
