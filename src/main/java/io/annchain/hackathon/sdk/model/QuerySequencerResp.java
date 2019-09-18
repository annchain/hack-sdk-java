package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class QuerySequencerResp {
    SequencerResp data;
    String err;
}
