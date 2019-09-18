package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class QueryPoolTxsResp {
    PoolTxs data;
    String err;
}
