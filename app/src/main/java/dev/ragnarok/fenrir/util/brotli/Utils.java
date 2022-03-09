/* Copyright 2015 Google Inc. All Rights Reserved.

   Distributed under MIT license.
   See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
*/

package dev.ragnarok.fenrir.util.brotli;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * A set of utility methods.
 */
final class Utils {

    private static final byte[] BYTE_ZEROES = new byte[1024];

    private static final int[] INT_ZEROES = new int[1024];


    static void fillBytesWithZeroes(byte[] dest, int start, int end) {
        int cursor = start;
        while (cursor < end) {
            int step = Math.min(cursor + 1024, end) - cursor;
            System.arraycopy(BYTE_ZEROES, 0, dest, cursor, step);
            cursor += step;
        }
    }


    static void fillIntsWithZeroes(int[] dest, int start, int end) {
        int cursor = start;
        while (cursor < end) {
            int step = Math.min(cursor + 1024, end) - cursor;
            System.arraycopy(INT_ZEROES, 0, dest, cursor, step);
            cursor += step;
        }
    }

    static void copyBytes(byte[] dst, int target, byte[] src, int start, int end) {
        System.arraycopy(src, start, dst, target, end - start);
    }

    static void copyBytesWithin(byte[] bytes, int target, int start, int end) {
        System.arraycopy(bytes, start, bytes, target, end - start);
    }

    static int readInput(InputStream src, byte[] dst, int offset, int length) {
        try {
            return src.read(dst, offset, length);
        } catch (IOException e) {
            throw new BrotliRuntimeException("Failed to read input", e);
        }
    }

    static void closeInput(InputStream src) throws IOException {
        src.close();
    }

    static byte[] toUsAsciiBytes(String src) {
        // NB: String#getBytes(String) is present in JDK 1.1, while other variants require JDK 1.6 and
        // above.
        return src.getBytes(StandardCharsets.US_ASCII);
    }

    static ByteBuffer asReadOnlyBuffer(ByteBuffer src) {
        return src.asReadOnlyBuffer();
    }

    static int isReadOnly(ByteBuffer src) {
        return src.isReadOnly() ? 1 : 0;
    }

    static int isDirect(ByteBuffer src) {
        return src.isDirect() ? 1 : 0;
    }

    // Crazy pills factory: code compiled for JDK8 does not work on JRE9.
    static void flipBuffer(Buffer buffer) {
        buffer.flip();
    }

    static int isDebugMode() {
        boolean assertsEnabled = Boolean.parseBoolean(System.getProperty("BROTLI_ENABLE_ASSERTS"));
        return assertsEnabled ? 1 : 0;
    }

    // See BitReader.LOG_BITNESS
    static int getLogBintness() {
        boolean isLongExpensive = Boolean.parseBoolean(System.getProperty("BROTLI_32_BIT_CPU"));
        return isLongExpensive ? 5 : 6;
    }
}
