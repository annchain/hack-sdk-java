package io.annchain.hackathon.sdk.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class QueryNonceResp {
    @JSONField(name="data")
    long nonce;
    String err;
}
