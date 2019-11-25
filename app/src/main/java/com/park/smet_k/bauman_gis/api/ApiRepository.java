package com.park.smet_k.bauman_gis.api;

import android.content.Context;

import com.park.smet_k.bauman_gis.App;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiRepository {
    private final BgisApi mAPI;
    private final OkHttpClient mOkHttpClient;

    public ApiRepository() {
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(new HttpUrl.Builder()
                        .scheme("https")
                        .host("bmstu-gis-spring.herokuapp.com")
                        .build())
                .client(mOkHttpClient)
                .build();

        mAPI = retrofit.create(BgisApi.class);
    }

    public BgisApi getAPI() {
        return mAPI;
    }

    public static ApiRepository from(Context context) {
        return App.from(context).getApiRepo();
    }
}
