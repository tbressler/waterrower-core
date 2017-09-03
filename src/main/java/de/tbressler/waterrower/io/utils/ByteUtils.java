package de.tbressler.waterrower.io.utils;

import io.netty.buffer.ByteBuf;

/**
 * Helper class for byte, byte buffers and byte arrays.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ByteUtils {

    /* The HEX values. */
    final private static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();


    /* Private constructor. */
    private ByteUtils()  {}


    /**
     * Returns the data of the buffer as String for debug purposes.
     *
     * @param buffer The byte buffer.
     * @return The data of the buffer as String.
     */
    public static String bufferToString(ByteBuf buffer) {
        return "ByteBuf[index=" + buffer.readerIndex() + ",bytes=" + buffer.readableBytes() + "]";
    }


    /**
     * Returns the byte array as Hex-String.
     *
     * Source:
     * http://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     *
     * @param bytes The byte array.
     * @return The byte array as Hex-String.
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}
