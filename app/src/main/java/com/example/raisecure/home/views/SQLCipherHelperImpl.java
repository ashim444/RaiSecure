package com.example.raisecure.home.views;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sqlcipher.SQLCipherOpenHelper;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;

public class SQLCipherHelperImpl extends SQLCipherOpenHelper {

    private String key;

    public SQLCipherHelperImpl(DatabaseDefinition databaseDefinition, DatabaseHelperListener listener) {
        super(databaseDefinition, listener);
    }


    //needed improvement
    @Override
    protected String getCipherSecret() {
        return key;//getfrom shared preference
    }

    public void setKey(String key) {
        this.key = key;
    }
}
