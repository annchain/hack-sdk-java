package io.annchain.hackathon.sdk;

import com.alibaba.fastjson.JSON;
import io.annchain.hackathon.sdk.model.*;
import lombok.Data;
import okhttp3.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import java.io.IOException;
import java.nio.ByteBuffer;

@Component
@Data
public class OgSolver {
    private Account account;
    private String url;
    private String token;

    OkHttpClient client = new OkHttpClient();
    MediaType jsonMedia = MediaType.parse("application/json");
    public static Logger logger = LoggerFactory.getLogger(OgSolver.class);


    @Autowired
    public OgSolver(
            @Value("${api.url}") String url,
            @Value("${api.token}") String token,
            @Value("${privkey}") String privateKey) {
        this.url = url;
        this.account = new Account(privateKey);
        this.token = token;
    }

    String post(String path, String json) throws IOException {
        RequestBody body = RequestBody.create(jsonMedia, json);
        Request request = new Request.Builder()
                .url(this.url + path)
                .post(body)
                .addHeader("cookie", "token=" + this.token)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    String get(String path) throws IOException {
        Request request = new Request.Builder()
//                .url("http://ptsv2.com/t/gzl29-1568792694/post")
                .url(this.url + path)
                .get()
                .addHeader("cookie", "token=" + this.token)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public void sendTx(Transaction tx) throws IOException, DecoderException {
        byte[] signature = this.signTx(tx);
        TxRequest tr = new TxRequest();
        tr.setParents(tx.getParents());
        tr.setFrom(safeEncodeHex(tx.getFrom()));
        tr.setTo(tx.getTo());
        tr.setNonce(tx.getNonce());
        tr.setGuarantee(tx.getGuarantee().toString());
        tr.setValue(tx.getValue().toString());
        tr.setSignature(safeEncodeHex(Hex.encodeHexString(signature)));
        tr.setPubkey(safeEncodeHex(this.account.getPublicKey()));

        String json = JSON.toJSONString(tr);
        logger.info("sending new tx: "+ json);
        String resp = post("/new_transaction", json);
        logger.info(resp);
    }

    private String safeEncodeHex(String hex) {
        if (!hex.startsWith("0x")) {
            return "0x" + hex;
        }
        return hex;
    }

    public byte[] signTx(Transaction tx) throws IOException, DecoderException {
        byte[] target = tx.signatureTargets();
        System.out.println(String.valueOf(Hex.encodeHex(target)));

        byte[] targetHashed = Hash.sha256(target);
        Sign.SignatureData signature = Sign.signMessage(targetHashed, this.account.getKeyPair(), false);

        ByteBuffer bb = ByteBuffer.allocate(65);
        bb.put(signature.getR());
        bb.put(signature.getS());
        bb.put((byte) (signature.getV()[0] - 27));
        return bb.array();
    }

    public QueryNonceResp queryNonce(String address) throws IOException {
        String path = "/query_nonce?address=" + address;
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QueryNonceResp.class);
    }

    public QueryBalanceResp queryBalance(String address) throws IOException {
        String path = "/query_balance?address=" + address;
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QueryBalanceResp.class);
    }
    public QueryTransactionResp queryTransaction(String hash) throws IOException {
        String path = "/transaction?hash=" + hash;
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QueryTransactionResp.class);
    }

    public QuerySequencerResp querySequencerByHash(String hash) throws IOException {
        String path = "/sequencer?hash=" + hash;
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QuerySequencerResp.class);
    }

    public QuerySequencerResp querySequencerByHeight(long height) throws IOException {
        String path = "/sequencer?height=" + height;
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QuerySequencerResp.class);
    }

    public QueryTxsResp queryTxsByAddress(String address) throws IOException {
        String path = "/transactions?address=" + address;
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QueryTxsResp.class);
    }

    public QueryTxsResp queryTxsByHeight(long height) throws IOException {
        String path = "/transactions?height=" + height;
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QueryTxsResp.class);
    }
    public QueryPoolTxsResp queryAllTipsInPool() throws IOException {
        String path = "/query_pool_tips";
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QueryPoolTxsResp.class);
    }

    public QueryPoolTxsResp queryAllTxsInPool() throws IOException {
        String path = "/query_pool_txs";
        String resp = get(path);
        logger.info("resp: "+ resp);
        return JSON.parseObject(resp, QueryPoolTxsResp.class);
    }
}