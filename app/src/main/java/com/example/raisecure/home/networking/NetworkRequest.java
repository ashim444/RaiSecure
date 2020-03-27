package com.example.raisecure.home.networking;

import com.example.raisecure.home.data.remote.RestKey;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class NetworkRequest {

    public Single<RestKey> getApiKey() {
        return RaiApiRepo.getRaiApi().getKey().subscribeOn(Schedulers.io());
    }
}
