package com.example.raisecure.home;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.raisecure.R;
import com.example.raisecure.home.base.database.SecureDatabase;
import com.example.raisecure.home.views.login.LoginController;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public  class MainActivity extends AppCompatActivity {

    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatabase();
        ViewGroup container = findViewById(R.id.controller_container);
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new LoginController()));
        }
    }

    private void initDatabase() {
        FlowManager.init(FlowConfig.builder(getApplicationContext())
                .addDatabaseConfig(DatabaseConfig.builder(SecureDatabase.class)
                        .databaseName("SecureDatabase")
                        .build())
                .build());
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
