package com.example.raisecure.home.model.entity;

import com.example.raisecure.home.base.database.SecureDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = SecureDatabase.class)
public class Secret extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    private long secretId;

    @Column
    private String secretTitle;

    @Column
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
