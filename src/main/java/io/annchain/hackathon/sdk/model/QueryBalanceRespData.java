package io.annchain.hackathon.sdk.model;

import lombok.Data;

@Data
public class QueryBalanceRespData {
    String address;
    String balance;
}
