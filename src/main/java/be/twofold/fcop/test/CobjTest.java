package be.twofold.fcop.test;

import be.twofold.fcop.iff.*;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class CobjTest {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\Jan\\Desktop\\missions\\Mp.out\\obj\\Cobj.030.Cobj");
        List<IffChunk> chunks;
        try (IffReader reader = new IffReader(Files.newInputStream(path))) {
            chunks = reader.stream().collect(Collectors.toUnmodifiableList());
        }

        Vector3[] vertices = null;
        Vector3[] normals = null;
        for (IffChunk chunk : chunks) {
            System.out.println(chunk);
            ByteBuffer buffer = ByteBuffer
                .wrap(chunk.getContent(), 4, chunk.getContent().length - 4)
                .order(ByteOrder.LITTLE_ENDIAN);
            System.out.println(Arrays.toString(buffer.array()));

            switch (chunk.getFourCC()) {
                case DQL3:
                    decode3DQL(buffer);
                    break;
                case DTL3:
                    decode3DTL(buffer);
                    break;
                case DNL4:
                    normals = decode4DNL(buffer);
                    break;
                case DVL4:
                    vertices = decode4DVL(buffer);
                    break;
            }
        }

        System.out.println("vertices = " + Arrays.toString(vertices));
        System.out.println("normals = " + Arrays.toString(normals));

//        String[] split = hex.split(" ");
//        byte[] bytes = new byte[split.length];
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = (byte) Integer.parseInt(split[i], 16);
//        }
//
//        short[] shorts = new short[bytes.length / 2];
//        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
//
//        for (int i = 0; i < shorts.length; i += 4) {
//            System.out.println(shorts[i] + "\t" + shorts[i + 1] + "\t" + shorts[i + 2] + "\t" + shorts[i + 3]);
//        }
    }

    private static void decode3DQL(ByteBuffer buffer) {
        int count = buffer.getInt();
        System.out.println("count = " + count);

        Set<Short> set = new HashSet<>();
        for (int i = 0; i < count; i++) {
//            int i1 = buffer.getInt();
            short s1 = buffer.getShort();
            short s2 = buffer.getShort();
            int i2 = buffer.getInt();
            int i3 = buffer.getInt();
//            short s3 = buffer.getShort();
//            short s4 = buffer.getShort();
//            short s5 = buffer.getShort();
//            short s6 = buffer.getShort();
            System.out.println(Stream.of(s1, s2, i2, i3)
                .map(Objects::toString)
                .collect(Collectors.joining("\t")));

            set.add(s2);
        }

        System.out.println(set.size());
    }

    private static void decode3DTL(ByteBuffer buffer) {
    }

    private static Vector3[] decode4DNL(ByteBuffer buffer) {
        int count = buffer.getInt();
        System.out.println("count = " + count);

        Vector3[] normals = new Vector3[count];
        for (int i = 0; i < count; i++) {
            float x = buffer.getShort();
            float y = buffer.getShort();
            float z = buffer.getShort();
            buffer.getShort();
            normals[i] = new Vector3(x, y, z).scale((float) (1.0 / 4096.0));
        }
        return normals;
    }

    private static Vector3[] decode4DVL(ByteBuffer buffer) {
        int count = buffer.getInt();
        System.out.println("count = " + count);

        Vector3[] vertices = new Vector3[count];
        for (int i = 0; i < count; i++) {
            float x = buffer.getShort();
            float y = buffer.getShort();
            float z = buffer.getShort();
            buffer.getShort();
            vertices[i] = new Vector3(x, y, z);
        }
        return vertices;
    }

}
