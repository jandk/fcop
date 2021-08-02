package be.twofold.fcop.iff;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;

public final class IffReader implements Iterable<IffChunk>, AutoCloseable {

    private static final int GroupSize = 24 * 1024;

    private InputStream input;

    public IffReader(InputStream input) {
        this.input = Objects.requireNonNull(input, "input is null");
    }

    @Override
    public Iterator<IffChunk> iterator() {
        return new Itr(input);
    }

    public Stream<IffChunk> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public void close() throws IOException {
        if (input == null)
            return;
        try {
            input.close();
        } finally {
            input = null;
        }
    }

    private static final class Itr implements Iterator<IffChunk> {
        private static final int HeaderSize = 8;

        private final InputStream input;
        private int offset = 0;
        private IffChunk next;

        private Itr(InputStream input) {
            this.input = Objects.requireNonNull(input, "input is null");
        }

        @Override
        public boolean hasNext() {
            if (next != null) {
                return true;
            }
            next = readChunk();
            return next != null;
        }

        @Override
        public IffChunk next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            IffChunk result = next;
            next = null;
            return result;
        }

        private IffChunk readChunk() {
            try {
                if (GroupSize - (offset % GroupSize) == 4) {
                    input.skip(4);
                    offset += 4;
                }
                byte[] header = input.readNBytes(HeaderSize);
                if (header.length < HeaderSize) {
                    return null;
                }

                ByteBuffer buffer = ByteBuffer.wrap(header)
                    .order(ByteOrder.LITTLE_ENDIAN);

                IffFourCC fourCC = IffFourCC.fromCode(buffer.getInt());
                int chunkSize = buffer.getInt();
                byte[] content = input.readNBytes(chunkSize - HeaderSize);

                IffChunk chunk = new IffChunk(offset, fourCC, content);
                offset += chunkSize;
                return chunk;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

}
