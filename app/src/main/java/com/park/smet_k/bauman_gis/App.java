package com.park.smet_k.bauman_gis;

import android.app.Application;
import android.content.Context;

import com.park.smet_k.bauman_gis.M_api.ApiRepository;
import com.park.smet_k.bauman_gis.M_news.NewsRepository;

public class App extends Application {
    private NewsRepository mNewsRepo;
    private ApiRepository mApiRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiRepo = new ApiRepository();
    }

    public static App from(Context context) {
        return (App) context.getApplicationContext();
    }

    public ApiRepository getApiRepo() {
        return mApiRepo;
    }

    public NewsRepository getNewsRepo() {
        return mNewsRepo;
    }
}

