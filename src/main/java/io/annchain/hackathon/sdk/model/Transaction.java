package io.annchain.hackathon.sdk.model;

import lombok.Data;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.Arrays;

@Data
public class Transaction {
    private static final String BLACKHOLE_ADDRESS = "0000000000000000000000000000000000000000";
    private String[] parents;
    private String from;
    private String to;
    private long nonce;
    private BigInteger guarantee;
    private BigInteger value;

    private byte[] longToBytes(long v) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.BIG_ENDIAN);
        LongBuffer lb = bb.asLongBuffer();
        lb.put(v);
        return bb.array();
    }

    public byte[] signatureTargets() throws IOException, DecoderException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (parents.length == 1) {

            baos.write(safeDecodeHex(parents[0]));
        } else if (parents.length == 2) {
            if (parents[0].compareTo(parents[1]) < 0) {
                baos.write(safeDecodeHex(parents[0]));
                baos.write(safeDecodeHex(parents[1]));
            } else {
                baos.write(safeDecodeHex(parents[1]));
                baos.write(safeDecodeHex(parents[0]));
            }
        }
        baos.write(longToBytes(this.nonce));
        baos.write(safeDecodeHex(this.from));
        if (StringUtils.isNotBlank(this.to)) {
            baos.write(safeDecodeHex(this.to));
        } else {
            baos.write(safeDecodeHex(BLACKHOLE_ADDRESS));
        }
        baos.write(fixedBitIntegerToByteArray(this.value));
        baos.write(fixedBitIntegerToByteArray(this.guarantee));

        return baos.toByteArray();
    }


    public static byte[] fixedBitIntegerToByteArray(BigInteger i) {
        byte[] original = i.toByteArray();
        if (original[0] == 0 && !i.equals(BigInteger.ZERO)) {
            // extra byte because of two's complement
            return Arrays.copyOfRange(original, 1, original.length);
        }
        return original;
    }

    public static byte[] safeDecodeHex(String hex) throws DecoderException {
        if (hex.startsWith("0x")){
            hex = hex.substring(2);
        }
        return Hex.decodeHex(hex);
    }
}
