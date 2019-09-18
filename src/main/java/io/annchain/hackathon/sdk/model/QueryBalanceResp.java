package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class QueryBalanceResp {
    QueryBalanceRespData data;
    String err;
}
