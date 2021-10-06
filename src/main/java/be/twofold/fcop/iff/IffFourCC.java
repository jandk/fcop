package be.twofold.fcop.iff;

import java.util.*;

public enum IffFourCC {
    AnmD(0x416e6d44),
    CTRL(0x4354524c),
    Cact(0x43616374),
    Cbmp(0x43626d70),
    Cctr(0x43637472),
    Cdcs(0x43646373),
    Cfnt(0x43666e74),
    Cfun(0x4366756e),
    Cnet(0x436e6574),
    Cobj(0x436f626a),
    Cptc(0x43707463),
    Cpyr(0x43707972),
    Csac(0x43736163),
    Csfx(0x43736678),
    Cshd(0x43736864),
    Ctil(0x4374696c),
    Ctos(0x43746f73),
    Cwav(0x43776176),
    DBB3(0x33444242),
    DGI4(0x34444749),
    DHS3(0x33444853),
    DHY3(0x33444859),
    DMI3(0x33444d49),
    DNL4(0x34444e4c),
    DQL3(0x3344514c),
    DRF3(0x33445246),
    DRL3(0x3344524c),
    DTL3(0x3344544c),
    DVL4(0x3444564c),
    FILL(0x46494c4c),
    LkUp(0x4c6b5570),
    MSIC(0x4d534943),
    PLUT(0x504c5554),
    PX16(0x50583136),
    RPNS(0x52504e53),
    SDAT(0x53444154),
    SHDR(0x53484452),
    SHOC(0x53484f43),
    SLFX(0x534C4658),
    SWVR(0x53575652),
    ScTA(0x53635441),
    Sect(0x53656374),
    _BCC(0x43434220),
    canm(0x63616e6d),
    snds(0x736e6473);

    private final int code;

    IffFourCC(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static IffFourCC fromCode(int code) {
        return Arrays.stream(values())
            .filter(iffFourCC -> iffFourCC.code == code)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid code: 0x%08x", code)));
    }

}
