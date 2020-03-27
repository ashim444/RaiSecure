package com.example.raisecure.home.networking;

import com.example.raisecure.home.data.remote.RestKey;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RaiApi {
    @GET(Config.EndPintsKey.KEY)
    Single<RestKey> getKey();
}
