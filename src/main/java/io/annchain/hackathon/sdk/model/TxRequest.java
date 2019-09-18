package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class TxRequest {
    String[] parents;
    String from;
    String to;
    long nonce;
    String guarantee;
    String value;
    String signature;
    String pubkey;
}
