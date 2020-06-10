package com.example.raisecure.home.data.local;

import android.content.Context;

import com.example.raisecure.home.base.database.SecureDatabase;
import com.example.raisecure.home.data.dao.SecretDao;
import com.example.raisecure.home.model.entity.Secret;

import java.util.List;

public class SecretRepo {
    private SecretDao secretDao;

    public SecretRepo(Context context) {
        secretDao = SecureDatabase.getInstance(context).getSecretDao();
    }

    public void deleteSecrets(long secret) {
        secretDao.deleteItem(secret);
    }

    public void updateItem(Secret secret) {
//        Secret secret = SQLite.select().from(Secret.class).where(Secret_Table.secretId.eq(secretId)).querySingle();
//        if (secret != null) {
//            secret.setSecretTitle(secretTitle);
//            secret.setSecretInfo(secretInfo);
//            secret.update();
//        }
        secretDao.updateItem(secret);
    }

    public Secret getItem(long secretId) {
//        return SQLite.select().from(Secret.class).where(Secret_Table.secretId.eq(secretId)).querySingle();
        return secretDao.getItem(secretId);
    }

    public List<Secret> getAllItem() {
//        return SQLite.select().from(Secret.class).queryList();
        return secretDao.getAllItem();
    }

    public long createNewSecret() {
        Secret secret = new Secret();
        return secretDao.insertItem(secret);
    }
}
