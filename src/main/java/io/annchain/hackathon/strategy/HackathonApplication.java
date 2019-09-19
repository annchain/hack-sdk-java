package io.annchain.hackathon.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.annchain.hackathon.sdk.OgSolver;
import io.annchain.hackathon.sdk.model.QueryNonceResp;
import io.annchain.hackathon.sdk.model.SequencerResp;
import io.annchain.hackathon.sdk.model.Transaction;
import io.annchain.hackathon.sdk.model.TransactionResp;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.math.BigInteger;

@SpringBootApplication(scanBasePackages = "io.annchain.hackathon")
public class HackathonApplication implements CommandLineRunner {

    public static Logger logger = LoggerFactory.getLogger(HackathonApplication.class);
    public static final int TXTYPE_SEQUENCER = 1;
    public static final int TXTYPE_TX = 0;

    @Autowired
    OgSolver ogSolver;

    public static void main(String[] args) {
        SpringApplication.run(HackathonApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }

    @KafkaListener(topics = "${app.topic}", groupId = "${api.token}")
    public void listen(@Payload String json) throws Exception {
        logger.info(json);
        JSONObject obj = JSON.parseObject(json);
        switch (obj.getIntValue("type")) {
            case TXTYPE_SEQUENCER:
                SequencerResp seq = obj.getObject("data", SequencerResp.class);
                handleSequencer(seq);
                break;
            case TXTYPE_TX:
                TransactionResp tx = obj.getObject("data", TransactionResp.class);
                handleTx(tx);
                break;
        }

    }

    private void handleTx(TransactionResp tx) {
        logger.info("received tx: " + tx.getHash());
    }

    private void handleSequencer(SequencerResp seq) throws IOException {
        logger.info("received seq: " + seq.getHash());

        QueryNonceResp qnr = ogSolver.queryNonce(this.ogSolver.getAccount().getAddress());
        Transaction tx = new Transaction();
        tx.setParents(new String[]{seq.getHash()});
        tx.setFrom(ogSolver.getAccount().getAddress());
        tx.setTo(null);
        tx.setNonce(qnr.getNonce() + 1);
        tx.setGuarantee(BigInteger.valueOf(2));
        tx.setValue(BigInteger.valueOf(0));
        try {
            ogSolver.sendTx(tx);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    }

}
