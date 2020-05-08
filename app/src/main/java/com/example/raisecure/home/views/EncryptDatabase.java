package com.example.raisecure.home.views;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.IOException;

public class EncryptDatabase {
    private static final String ASSET_NAME = ".db";
    private String DB_PATH;
    private Context mContext;

    public EncryptDatabase(Context context,String key, String dbName) {
        DB_PATH = context.getDatabasePath(dbName + ASSET_NAME).getPath();
        this.mContext = context;
        try {
            encryptDataBase(dbName, key);
        } catch (IOException e) {

        }
    }

    public void encryptDataBase(String dbName, String passphrase) throws IOException {

        File originalFile = mContext.getDatabasePath(dbName + ASSET_NAME);
        String originalPath = originalFile.getPath();//just to compare
        if (originalFile.exists()) {
            SQLiteDatabase.loadLibs(mContext);
            File newFile = File.createTempFile("encryptDB", "db", mContext.getCacheDir());

            SQLiteDatabase db = SQLiteDatabase.openDatabase(originalFile.getAbsolutePath(), "", null, SQLiteDatabase.OPEN_READWRITE);

            db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';",
                    newFile.getAbsolutePath(), passphrase));

            db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
            db.rawExecSQL("DETACH DATABASE encrypted;");

            int version = db.getVersion();

            db.close();

            db = SQLiteDatabase.openDatabase(newFile.getAbsolutePath(), passphrase, null, SQLiteDatabase.OPEN_READWRITE);

            db.setVersion(version);
            db.close();

            originalFile.delete();
            newFile.renameTo(originalFile);
        } else {
            Log.d(Const.TAG, "encryptDataBase: ");

        }
    }


}
