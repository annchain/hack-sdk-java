package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class QueryTransactionResp {
    TransactionResp data;
    String err;
}
