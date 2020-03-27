package com.example.raisecure.home.views.utils;

import android.content.Context;

public class RaiSaveData {
    private static final String RAI_SHARED_PREFS = "datasave";

    private Context context;

    public RaiSaveData(Context context) {
        this.context = context;
    }

    public void saveStringData(String key, String data) {
        context.getSharedPreferences(RAI_SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(key, data).apply();
    }

    public String getSavedStringData(String key) {
        return context.getSharedPreferences(RAI_SHARED_PREFS, Context.MODE_PRIVATE).getString(key, "");
    }

    public void saveBooleanData(String key, Boolean data) {
        context.getSharedPreferences(RAI_SHARED_PREFS, Context.MODE_PRIVATE).edit().putBoolean(key, data).apply();
    }

    public Boolean getSavedBooleanData(String key) {
        return context.getSharedPreferences(RAI_SHARED_PREFS, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public void removeData(String key) {
        context.getSharedPreferences(RAI_SHARED_PREFS, Context.MODE_PRIVATE)
                .edit().remove(key).apply();
    }
}
