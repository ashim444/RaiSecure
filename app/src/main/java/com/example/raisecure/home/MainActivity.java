package com.example.raisecure.home;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.raisecure.R;
import com.example.raisecure.home.base.database.SecureDatabase;
import com.example.raisecure.home.model.entity.Secret;
import com.example.raisecure.home.views.Const;
import com.example.raisecure.home.views.EncryptDatabase;
import com.example.raisecure.home.views.SQLCipherHelperImpl;
import com.example.raisecure.home.views.login.LoginController;
import com.example.raisecure.home.views.utils.RaiSaveData;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

public  class MainActivity extends AppCompatActivity {

    private Router router;
    private RaiSaveData raiSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup container = findViewById(R.id.controller_container);
        raiSaveData = new RaiSaveData(this);
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new LoginController()));
        }
    }

    /**
     * @param b : if user is logged then it is true. But since i use hardcode key it make not difference
     */
    public void initDatabase(boolean b) {
//        final String key = new RaiSaveData(this).getSavedStringData(Const.SERVER_API_KEY);
        final String key = "123124124124124124124"; // here we can use the key for encryption.
        if (!raiSaveData.getSavedBooleanData(Const.APPLICATION_STATE)) {

            raiSaveData.saveBooleanData(Const.APPLICATION_STATE, true);
            FlowManager.init(FlowConfig.builder(getApplicationContext())
                    .addDatabaseConfig(DatabaseConfig.builder(SecureDatabase.class)
                            .build())
                    .build());
            openDatabase();
            encryptDataBase(key);//used only once in its whole life time
            FlowManager.close();//this need to be close because previous database is no longer there
            openDatabaseWithKey(key);//open new database with new key
        } else {

            openDatabaseWithKey(key);
        }
    }

    private void openDatabaseWithKey(String key) {
        FlowManager.init(FlowConfig.builder(getApplicationContext())
                .addDatabaseConfig(DatabaseConfig.builder(SecureDatabase.class)
                        .openHelper((databaseDefinition, helperListener) -> {
                            SQLCipherHelperImpl sqlCipherHelper = new SQLCipherHelperImpl(databaseDefinition, helperListener);
                            sqlCipherHelper.setKey(key);
                            return sqlCipherHelper;
                        })
                        .build())
                .build());
    }

    private void openDatabase() {
        SQLite.select().from(Secret.class).queryList();
    }

    private void encryptDataBase(String key) {
//        try {
        new EncryptDatabase(this, key, FlowManager.getDatabaseName(SecureDatabase.class));
//        } catch (IOException i) {
//
//        }
    }


    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
