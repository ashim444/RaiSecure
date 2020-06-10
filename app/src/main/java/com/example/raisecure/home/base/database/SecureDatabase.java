package com.example.raisecure.home.base.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.raisecure.home.data.dao.SecretDao;
import com.example.raisecure.home.model.entity.Secret;

@Database(entities = {Secret.class}, version = SecureDatabase.VERSION)
public abstract class SecureDatabase extends RoomDatabase {
    public static final int VERSION = 1;

    public static final String DB_NAME = "SecureDatabase.db";

    private static SecureDatabase instance;

    public static  SecureDatabase getInstance(Context context) {
        if(instance == null){
            instance  = Room.databaseBuilder(context, SecureDatabase.class, SecureDatabase.DB_NAME)
                    .allowMainThreadQueries()
                    .createFromAsset("SecureDatabase.db")
                    .build();
        }
        return instance;
    }

    public abstract SecretDao getSecretDao();
}
