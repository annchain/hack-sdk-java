package io.annchain.hackathon;

import org.web3j.crypto.*;

import java.math.BigInteger;
import java.security.*;

public class Account {
    private BigInteger publicKey;
    private BigInteger privateKey;
    private String address;

    public Account(String privateKeyHex) {
        this.privateKey = new BigInteger(privateKeyHex, 16);
        this.publicKey = Sign.publicKeyFromPrivate(this.privateKey);
        ECKeyPair keyPair = new ECKeyPair(this.privateKey, this.publicKey);

        String password = "ANY";
        WalletFile aWallet = null;
        try {
            aWallet = Wallet.createLight(password, keyPair);
            this.address = aWallet.getAddress();
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    public String GetPublicKey() {
        // uncompressed format needs a 04
        return "04" + String.format("0128d",this.publicKey.toString(16));
    }

    public String GetPrivateKey() {
        return String.format("0128d",this.privateKey.toString(16));
    }

    public String GetAddress() {
        return this.address;
    }

//    public static String compressPubKey(BigInteger pubKey) {
//        String pubKeyYPrefix = pubKey.testBit(0) ? "03" : "02";
//        String pubKeyHex = pubKey.toString(16);
//        String pubKeyX = pubKeyHex.substring(0, 64);
//        return pubKeyYPrefix + pubKeyX;
//    }


    public static void main(String[] args) {
        Account account = new Account("af1b6df8cc06d79902029c0e446c3dc2788893185759d2308b5bb10aa0614b7d");
        System.out.println("Private key: " + account.GetPrivateKey());
        System.out.println("Public key: " + account.GetPublicKey());
        System.out.println("Address: " + account.GetAddress());

    }
}
