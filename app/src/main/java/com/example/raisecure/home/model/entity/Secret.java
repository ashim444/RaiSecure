package com.example.raisecure.home.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Secret {
    @PrimaryKey(autoGenerate = true)
    private long secretId;
    private String secretTitle;
    private String secretInfo;


    public long getSecretId() {
        return secretId;
    }

    public void setSecretId(long secretId) {
        this.secretId = secretId;
    }

    public String getSecretTitle() {
        return secretTitle;
    }

    public void setSecretTitle(String secretTitle) {
        this.secretTitle = secretTitle;
    }

    public String getSecretInfo() {
        return secretInfo;
    }

    public void setSecretInfo(String secretInfo) {
        this.secretInfo = secretInfo;
    }
}
