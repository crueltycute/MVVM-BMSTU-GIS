package com.park.smet_k.bauman_gis.navigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.park.smet_k.bauman_gis.model.GoRoute;

public class NavigatorViewModel extends AndroidViewModel {
    private NavigatorRepository mNavRepo = new NavigatorRepository(getApplication());
    private LiveData<GoRoute> mRoute = mNavRepo.getRoute();

    public NavigatorViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<GoRoute> getRoute() {
        return mRoute;
    }

    public void find(String from, String to) {
        mNavRepo.find(from, to);
    }
}
