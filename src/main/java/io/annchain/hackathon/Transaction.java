package io.annchain.hackathon;

import lombok.Data;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;

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
            baos.write(Hex.decodeHex(parents[0]));
        } else if (parents.length == 2) {
            if (parents[0].compareTo(parents[1]) < 0) {
                baos.write(Hex.decodeHex(parents[0]));
                baos.write(Hex.decodeHex(parents[1]));
            } else {
                baos.write(Hex.decodeHex(parents[1]));
                baos.write(Hex.decodeHex(parents[0]));
            }
        }
        baos.write(longToBytes(this.nonce));
        baos.write(Hex.decodeHex(this.from));
        if (StringUtils.isNotBlank(this.to)){
            baos.write(Hex.decodeHex(this.to));
        }else{
            baos.write(Hex.decodeHex(BLACKHOLE_ADDRESS));
        }
        baos.write(this.value.toByteArray());
        baos.write(this.guarantee.toByteArray());

        return baos.toByteArray();
    }
}
