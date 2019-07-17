package com.tizzy;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.List;

public class Block {

    int index;
    Timestamp timestamp;
    List<JSONObject> transactions;
    int proof;
    String previous_hash;

    public Block(int index, Timestamp timestamp, List<JSONObject> transactions, int proof, String previous_hash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.proof = proof;
        this.previous_hash = previous_hash;
    }

    public String getPrevious_hash() {
        return previous_hash;
    }

    public JSONObject getJSON() {
        return new JSONObject()
                .put("index", index)
                .put("timestamp", timestamp)
                .put("transactions", transactions)
                .put("proof", proof)
                .put("previous_hash", previous_hash);
    }
}
