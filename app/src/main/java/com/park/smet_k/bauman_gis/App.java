package com.park.smet_k.bauman_gis;

import android.app.Application;
import android.content.Context;

import com.park.smet_k.bauman_gis.api.ApiRepository;
import com.park.smet_k.bauman_gis.api.SearchApiRepository;
import com.park.smet_k.bauman_gis.prefs.PreferencesRepository;

public class App extends Application {
    private ApiRepository mApiRepo;
    private SearchApiRepository mSearchApiRepo;
    private PreferencesRepository mPreferencesRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiRepo = new ApiRepository();
        mSearchApiRepo = new SearchApiRepository();
        mPreferencesRepo = new PreferencesRepository(getApplicationContext());
    }

    public static App from(Context context) {
        return (App) context.getApplicationContext();
    }

    public ApiRepository getApiRepo() {
        return mApiRepo;
    }

    public SearchApiRepository getmSearchApiRepo() {
        return mSearchApiRepo;
    }

    public PreferencesRepository getPreferencesRepo() {
        return mPreferencesRepo;
    }

}

