package com.example.raisecure.home;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.raisecure.R;
import com.example.raisecure.home.base.database.SecureDatabase;
import com.example.raisecure.home.views.EncryptDatabase;
import com.example.raisecure.home.views.login.LoginController;
import com.example.raisecure.home.views.utils.RaiSaveData;

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
        final String key = "123124124124124124124"; // here we can use the key for encryption.
        SecureDatabase secureDatabase = SecureDatabase.getInstance(this);
    }



    private void encryptDataBase(String key) {
        new EncryptDatabase(this, key, "SecureDatabase.db");


    }


    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
