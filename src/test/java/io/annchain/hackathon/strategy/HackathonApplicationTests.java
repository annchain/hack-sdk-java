package io.annchain.hackathon.strategy;

import io.annchain.hackathon.sdk.OgSolver;
import io.annchain.hackathon.sdk.model.Account;
import io.annchain.hackathon.sdk.model.QueryBalanceResp;
import io.annchain.hackathon.sdk.model.Transaction;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HackathonApplicationTests {

    public static final String token = "98765467890";
//    public static final String url = "http://47.100.122.212:30020";
    public static final String url = "http://172.28.152.102:32020";

    @Test
    public void testSignatureTargets() throws IOException, DecoderException {
        Transaction tx = new Transaction();
        tx.setParents(new String[]{"5fe495a402ae8eea2ecea8625602decbed0ff507b5b6276edb06d560f7a20ac4"});
        tx.setNonce(3);
        tx.setFrom("f1b4b3de579ff16888f3340f39c45f207f2cd84d");
        tx.setValue(BigInteger.valueOf(0));
        tx.setGuarantee(BigInteger.valueOf(255));
        Assert.assertEquals(
                "5fe495a402ae8eea2ecea8625602decbed0ff507b5b6276edb06d560f7a20ac40000000000000003f1b4b3de579ff16888f3340f39c45f207f2cd84d000000000000000000000000000000000000000000ff",
                Hex.encodeHexString(tx.signatureTargets()));
    }

    @Test
    public void testSignature() throws IOException, DecoderException, SignatureException {
        Transaction tx = new Transaction();
        tx.setParents(new String[]{"5fe495a402ae8eea2ecea8625602decbed0ff507b5b6276edb06d560f7a20ac4"});
        tx.setNonce(3);
        tx.setFrom("f1b4b3de579ff16888f3340f39c45f207f2cd84d");
        tx.setValue(BigInteger.valueOf(0));
        tx.setGuarantee(BigInteger.valueOf(1));

        Account account = new Account("af1b6df8cc06d79902029c0e446c3dc2788893185759d2308b5bb10aa0614b7d");
        OgSolver os = new OgSolver("http://", "", account.getPrivateKey());
        byte[] signature = os.signTx(tx);
        System.out.println(Hex.encodeHexString(signature));
    }

    @Test
    public void testAPI() throws IOException {
        Account account = new Account("567d6a1f2d5bafae059308d91cb8a5452cb48032bed42a9ff5a601a7bab5e183");
        OgSolver os = new OgSolver(url, token, account.getPrivateKey());
//        System.out.println(os.queryBalance(account.getAddress()));
//        System.out.println(os.queryTransaction("0x77b0e883e5c0d1bf27d5acde8cdacb6fbff4fd5b6c41ff707f7c173288d9cd3d"));
//        System.out.println(os.querySequencerByHash("0xf545b2e13d7aff012ec06874082b2f3dbcda94ae6c6a8e15b05010d219fe7600"));
//        System.out.println(os.querySequencerByHeight(1980));
//        System.out.println(os.queryTxsByAddress(account.getAddress()));
//        System.out.println(os.queryTxsByHeight(1980));
//        System.out.println(os.queryAllTipsInPool());
        System.out.println(os.queryAllTxsInPool());

    }

    @Test
    public void testToArray() {
        BigInteger[] bis = new BigInteger[]{
                BigInteger.valueOf(0),
                BigInteger.valueOf(1),
                BigInteger.valueOf(128),
                BigInteger.valueOf(129),
                BigInteger.valueOf(255),
                BigInteger.valueOf(256),
                BigInteger.valueOf(65535),
                BigInteger.valueOf(65536),
        };
        for (BigInteger bi : bis) {
            System.out.printf("%s %s\r\n", bi.toString(), Hex.encodeHexString(Transaction.fixedBitIntegerToByteArray(bi)));
        }
    }
}