package com.example.raisecure.home.data.local;

import com.example.raisecure.home.model.entity.Secret;
import com.example.raisecure.home.model.entity.Secret_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class SecretRepo {

    public SecretRepo() {
    }

    public void deleteSecrets(long secretId) {
        SQLite.delete()
                .from(Secret.class)
                .where(Secret_Table.secretId.eq(secretId))
                .async().execute();
    }

    public void updateItem(long secretId, String secretTitle, String secretInfo) {
        Secret secret = SQLite.select().from(Secret.class).where(Secret_Table.secretId.eq(secretId)).querySingle();
        if (secret != null) {
            secret.setSecretTitle(secretTitle);
            secret.setSecretInfo(secretInfo);
            secret.update();
        }
    }

    public void getItem(long secretId) {
        SQLite.select().from(Secret.class).where(Secret_Table.secretId.eq(secretId)).querySingle();
    }

    public List<Secret> getAllItem() {
        return SQLite.select().from(Secret.class).queryList();
    }

    public long createNewSecret() {
        Secret secret = new Secret();
        secret.save();
        return secret.getSecretId();
    }
}
