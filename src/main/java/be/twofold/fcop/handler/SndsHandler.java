package be.twofold.fcop.handler;

import be.twofold.fcop.iff.*;

public final class SndsHandler extends WaveHandler {
    public SndsHandler() {
        super(1, 22050);
    }

    @Override
    public IffFourCC getFourCC() {
        return IffFourCC.snds;
    }
}
