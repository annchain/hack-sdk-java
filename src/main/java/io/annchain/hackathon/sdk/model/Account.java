package io.annchain.hackathon.sdk.model;

import org.apache.commons.lang3.StringUtils;
import org.web3j.crypto.*;

import java.math.BigInteger;

public class Account {
    private BigInteger publicKey;
    private BigInteger privateKey;
    private String address;
    private ECKeyPair keyPair;

    public Account(String privateKeyHex) {
        this.privateKey = new BigInteger(privateKeyHex, 16);
        this.publicKey = Sign.publicKeyFromPrivate(this.privateKey);
        this.keyPair = new ECKeyPair(this.privateKey, this.publicKey);

        String password = "ANY";
        WalletFile aWallet = null;
        try {
            aWallet = Wallet.createLight(password, keyPair);
            this.address = aWallet.getAddress();
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    public String getPublicKey() {
        // uncompressed format needs a 04
        return "04" + StringUtils.leftPad(this.publicKey.toString(16), 128, '0');
    }

    public String getPrivateKey() {
        return StringUtils.leftPad(this.privateKey.toString(16), 64, '0');
    }

    public String getAddress() {
        return StringUtils.leftPad(this.address, 40, '0');
    }

    public ECKeyPair getKeyPair() {
        return this.keyPair;
    }

//    public static String compressPubKey(BigInteger pubKey) {
//        String pubKeyYPrefix = pubKey.testBit(0) ? "03" : "02";
//        String pubKeyHex = pubKey.toString(16);
//        String pubKeyX = pubKeyHex.substring(0, 64);
//        return pubKeyYPrefix + pubKeyX;
//    }


    public static void main(String[] args) {
        Account account = new Account("af1b6df8cc06d79902029c0e446c3dc2788893185759d2308b5bb10aa0614b7d");
        System.out.println("Private key: " + account.getPrivateKey());
        System.out.println("Public key: " + account.getPublicKey());
        System.out.println("Address: " + account.getAddress());

    }
}
