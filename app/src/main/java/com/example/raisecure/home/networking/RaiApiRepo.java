package com.example.raisecure.home.networking;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RaiApiRepo {

    private static RaiApi raiApi;

    public static RaiApi getRaiApi() {
        if (raiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            raiApi = retrofit.create(RaiApi.class);
        }
        return raiApi;
    }
}
