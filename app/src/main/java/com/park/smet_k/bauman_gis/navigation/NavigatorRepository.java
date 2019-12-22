package com.park.smet_k.bauman_gis.navigation;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.park.smet_k.bauman_gis.api.SearchApi;
import com.park.smet_k.bauman_gis.api.SearchApiRepository;
import com.park.smet_k.bauman_gis.model.GoRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigatorRepository {
    private final static MutableLiveData<GoRoute> mRoute = new MutableLiveData<>();
    private final static MutableLiveData<String> mError = new MutableLiveData<>();

    static {
        mRoute.setValue(new GoRoute());
        mError.setValue("");
    }

    private final Context mContext;
    private SearchApi mApi;

    public NavigatorRepository(Context context) {
        mContext = context;
        mApi = SearchApiRepository.from(mContext).getAPI();
    }

    public LiveData<GoRoute> getRoute() {
        return mRoute;
    }

    public MutableLiveData<String> getError() {
        return mError;
    }

    public void find(String from, String to) {
        mApi.getRoute(from, to).enqueue(new Callback<GoRoute>() {
            @Override
            public void onResponse(Call<GoRoute> call, Response<GoRoute> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mRoute.postValue(response.body());
                }
                if (!response.isSuccessful()) {
                    if (response.body() != null) {
                        mError.postValue("can't to request, reason: " + response.body());
                    } else {
                        mError.postValue("can't to request, got code: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<GoRoute> call, Throwable t) {
                Log.e("navigator repository", "failed to load", t);
            }
        });
    }
}
