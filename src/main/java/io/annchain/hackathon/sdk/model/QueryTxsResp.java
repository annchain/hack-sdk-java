package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class QueryTxsResp {
    TransactionResp[] data;
    String err;
}
