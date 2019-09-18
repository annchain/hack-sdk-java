package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class SequencerResp {
    int type;
    String hash;
    String[] parents;
    String from;
    long nonce;
    String treasure;
    long height;
}
