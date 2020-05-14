package com.example.raisecure.home.base;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.sch.stetho.sqlcipher.DatabasePasswordProvider;
import com.sch.stetho.sqlcipher.SqlCipherDatabaseDriver;

import java.io.File;


public class RaiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(new Stetho.Initializer(this) {
            @Override
            protected Iterable<DumperPlugin> getDumperPlugins() {
                return new Stetho.DefaultDumperPluginsBuilder(getApplicationContext()).finish();
            }

            @Override
            protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                final DatabasePasswordProvider databasePasswordProvider = new DatabasePasswordProvider() {
                    @Override
                    public String getDatabasePassword(File databaseFile) {
                        return "123124124124124124124";
                    }
                };
                return new Stetho.DefaultInspectorModulesBuilder(getApplicationContext())
                        .provideDatabaseDriver(new SqlCipherDatabaseDriver(getApplicationContext(), databasePasswordProvider))
                        .finish();
            }
        });
    }
}
