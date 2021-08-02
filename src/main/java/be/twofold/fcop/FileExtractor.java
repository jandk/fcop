package be.twofold.fcop;

import be.twofold.fcop.chunk.*;
import be.twofold.fcop.handler.*;
import be.twofold.fcop.iff.*;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public final class FileExtractor {
    private static final Map<IffFourCC, FileHandler> Handlers = new EnumMap<>(Map.of(
        IffFourCC.Cbmp, new CbmpHandler(),
        IffFourCC.Cwav, new CwavHandler(),
        IffFourCC.MSIC, new MsicHandler(),
        IffFourCC.canm, new CanmHandler(),
        IffFourCC.snds, new SndsHandler()
    ));

    private final Path input;
    private final Path output;

    private final List<ShocFile> files = new ArrayList<>();
    private ByteArrayOutputStream msic = new ByteArrayOutputStream();
    private ByteArrayOutputStream sdat = new ByteArrayOutputStream();
    private ShdrChunk shdr;
    private SwvrChunk swvr;

    public FileExtractor(Path input, Path output) {
        this.input = Objects.requireNonNull(input, "input is null");
        this.output = Objects.requireNonNull(output, "output is null");
        if (input.equals(output)) {
            throw new IllegalArgumentException("Input and output cannot be the same");
        }
    }

    public void extract() throws IOException {
        readAllChunks();
        writeAllFiles();
    }

    private void readAllChunks() {
        List<IffChunk> chunks;
        try (IffReader reader = new IffReader(Files.newInputStream(input))) {
            chunks = reader.stream().collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        for (IffChunk chunk : chunks) {
            IffFourCC fourCC = chunk.getFourCC();
            switch (fourCC) {
                case CTRL:
                case FILL:
                    break;
                case MSIC:
                    byte[] content = chunk.getContent();
                    msic.write(content, 20, content.length - 20);
                    break;
                case SHOC:
                    decodeShoc(chunk);
                    break;
                case SWVR:
                    saveFile();
                    swvr = SwvrChunk.parse(chunk.getContent());
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown chunk type: " + fourCC);
            }
        }

        saveFile();

        // Save the music separately
        if (msic.size() > 0) {
            files.add(new ShocFile(0, IffFourCC.MSIC, msic.toByteArray(), swvr.getFilename()));
        }

        msic = null;
        sdat = null;
        shdr = null;
        swvr = null;
    }

    private void writeAllFiles() throws IOException {
        if (!Files.exists(output)) {
            Files.createDirectories(output);
        }

        for (ShocFile file : files) {
            IffFourCC fourCC = file.getFourCC();
            FileHandler handler = Handlers.get(fourCC);
            String extension = handler == null ? fourCC.name() : handler.getExtension();
            String filename = file.getFilename()
                .map(s -> s + "." + extension)
                .orElseGet(() -> String.format("%s.%03d.%s", fourCC, file.getSequence(), extension));

            try (OutputStream out = Files.newOutputStream(output.resolve(filename))) {
                if (handler != null) {
                    handler.process(file.getContent(), out);
                } else {
                    out.write(file.getContent());
                }
            }
        }
    }

    private void decodeShoc(IffChunk chunk) {
        ByteBuffer buffer = ByteBuffer.wrap(chunk.getContent())
            .order(ByteOrder.LITTLE_ENDIAN);

        IffFourCC fourCC = IffFourCC.fromCode(buffer.getInt(8));
        switch (fourCC) {
            case SHDR:
                saveFile();
                shdr = ShdrChunk.parse(chunk.getContent());
                break;
            case SDAT:
                byte[] content = chunk.getContent();
                sdat.write(content, 12, content.length - 12);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported fourCC " + fourCC);
        }
    }

    private void saveFile() {
        if (shdr == null) {
            return;
        }

        files.add(new ShocFile(
            shdr.getSequence(),
            shdr.getFourCC(),
            sdat.toByteArray(),
            swvr != null ? swvr.getFilename() : null
        ));

        shdr = null;
        sdat.reset();
    }

}
