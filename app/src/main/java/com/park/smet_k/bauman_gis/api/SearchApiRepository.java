package com.park.smet_k.bauman_gis.api;

import android.content.Context;

import com.park.smet_k.bauman_gis.App;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class SearchApiRepository {
    private final SearchApi mAPI;
    private final OkHttpClient mOkHttpClient;

    public SearchApiRepository() {
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(new HttpUrl.Builder()
                        .scheme("https")
                        .host("95.163.214.14")
                        .port(8000)
                        .build())
                .client(mOkHttpClient)
                .build();

        mAPI = retrofit.create(SearchApi.class);
    }

    public SearchApi getAPI() {
        return mAPI;
    }

    public static SearchApiRepository from(Context context) {
        return App.from(context).getmSearchApiRepo();
    }
}
