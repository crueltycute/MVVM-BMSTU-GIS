package com.park.smet_k.bauman_gis;

import android.app.Application;

import com.park.smet_k.bauman_gis.compontents.AppComponent;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent.init(this);
    }
}

