package io.annchain.hackathon;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.encoders.HexEncoder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.HexDumpEncoder;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HackathonApplicationTests {

	@Test
	public void testSignatureTargets() throws IOException, DecoderException {
		Transaction tx = new Transaction();
		tx.setParents(new String[]{"5fe495a402ae8eea2ecea8625602decbed0ff507b5b6276edb06d560f7a20ac4"});
		tx.setNonce(3);
		tx.setFrom("f1b4b3de579ff16888f3340f39c45f207f2cd84d");
		tx.setValue(BigInteger.valueOf(0));
		tx.setGuarantee(BigInteger.valueOf(1));
		Assert.assertEquals(
				"5fe495a402ae8eea2ecea8625602decbed0ff507b5b6276edb06d560f7a20ac40000000000000003f1b4b3de579ff16888f3340f39c45f207f2cd84d00000000000000000000000000000000000000000001",
				Hex.encodeHexString(tx.signatureTargets()));
		System.out.println();
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
		OgSolver os = new OgSolver("");
		byte[] signature = os.signTx(tx, account);
		System.out.println(Hex.encodeHexString(signature));

	}
}

// 0xc0a44a212529f3dab31bb04930914024715ad2f15a9bf18c47f9ff629e5cd62d21c5c9dd274b488efbee9cba0d05727c27f512986f251713b8c32209238b0f9a01
// [v = 1b, r = 68ec84a369c6ce548a66136debbdee3234f4618c6c98597e14f86fef63ea1c3c, s = 3c8ea2cf22e3897d4fb721fdbad2ee5277368e4a4e74f23a3c26ced126b916a8]