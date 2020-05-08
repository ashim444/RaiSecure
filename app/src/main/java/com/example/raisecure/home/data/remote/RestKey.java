package com.example.raisecure.home.data.remote;

import com.google.gson.annotations.SerializedName;

public class RestKey {

    @SerializedName("encryption_key")
    private String key;

    public RestKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
