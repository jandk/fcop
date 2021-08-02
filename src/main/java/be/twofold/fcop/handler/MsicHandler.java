package be.twofold.fcop.handler;

import be.twofold.fcop.iff.*;

public final class MsicHandler extends WaveHandler {
    public MsicHandler() {
        super(2, 14212);
    }

    @Override
    public IffFourCC getFourCC() {
        return IffFourCC.MSIC;
    }
}
