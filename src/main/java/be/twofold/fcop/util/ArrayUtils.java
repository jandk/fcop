package be.twofold.fcop.util;

import java.util.*;

public final class ArrayUtils {
    private ArrayUtils() {
        throw new UnsupportedOperationException();
    }

    public static int indexOf(byte[] array, byte value, int from, int to) {
        Objects.checkFromToIndex(from, to, array.length);
        for (int i = from; i < to; i++) {
            if (array[i] == 0) {
                return i;
            }
        }
        return -1;
    }
}
