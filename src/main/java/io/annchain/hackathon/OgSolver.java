package io.annchain.hackathon;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SignatureException;

public class OgSolver {
    private String url;

    public OgSolver(String url) {
        this.url = url;
    }

    public void sendTx(Transaction tx) {

    }

    protected byte[] signTx(Transaction tx, Account account) throws IOException, DecoderException, SignatureException {
        byte[] target = tx.signatureTargets();
        System.out.println(String.valueOf(Hex.encodeHex(target)));

        byte[] targetHashed = Hash.sha256(target);
        Sign.SignatureData signature = Sign.signMessage(targetHashed, account.getKeyPair(), false);

        ByteBuffer bb = ByteBuffer.allocate(65);
        bb.put(signature.getR());
        bb.put(signature.getS());
        bb.put((byte) (signature.getV()[0] - 27));
        return bb.array();
    }

}
//0xc0a44a212529f3dab31bb04930914024715ad2f15a9bf18c47f9ff629e5cd62d21c5c9dd274b488efbee9cba0d05727c27f512986f251713b8c32209238b0f9a01
//  c0a44a212529f3dab31bb04930914024715ad2f15a9bf18c47f9ff629e5cd62d21c5c9dd274b488efbee9cba0d05727c27f512986f251713b8c32209238b0f9a