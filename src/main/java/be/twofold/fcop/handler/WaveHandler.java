package be.twofold.fcop.handler;

import java.io.*;
import java.nio.*;

public abstract class WaveHandler implements FileHandler {

    private final int channels;
    private final int samplerate;

    protected WaveHandler(int channels, int samplerate) {
        this.channels = channels;
        this.samplerate = samplerate;
    }

    @Override
    public final void process(byte[] content, OutputStream out) throws IOException {
        int size = content.length - 4;
        out.write(generateWavHeader(size));
        out.write(content, 4, size);
    }

    @Override
    public final String getExtension() {
        return "wav";
    }

    private byte[] generateWavHeader(int size) {
        ByteBuffer header = ByteBuffer
            .allocate(44)
            .order(ByteOrder.LITTLE_ENDIAN);

        int nAvgBytesPerSec = channels * samplerate;

        // RIFF chunk
        header.putInt(0x46464952);       // "RIFF"
        header.putInt(size + 36);        // "RIFF" size
        header.putInt(0x45564157);       // "WAVE"

        // fmt_ chunk
        header.putInt(0x20746d66);         // "fmt "
        header.putInt(16);                 // "fmt " size
        header.putShort((short) 1);        // wFormatTag
        header.putShort((short) channels); // nChannels
        header.putInt(samplerate);         // nSamplesPerSec
        header.putInt(nAvgBytesPerSec);    // nAvgBytesPerSec
        header.putShort((short) 1);        // nBlockAlign
        header.putShort((short) 8);        // wBitsPerSample
        header.putInt(0x61746164);         // "data"
        header.putInt(size);               // "data" size

        return header.array();
    }

}
