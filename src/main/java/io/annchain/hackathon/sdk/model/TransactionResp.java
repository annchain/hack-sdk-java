package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class TransactionResp {
    int type;
    String hash;
    String[] parents;
    String from;
    String to;
    long nonce;
    String guarantee;
    String value;
}
