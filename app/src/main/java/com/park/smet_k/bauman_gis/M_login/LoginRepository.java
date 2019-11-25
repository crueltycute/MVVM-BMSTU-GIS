package com.park.smet_k.bauman_gis.M_login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.park.smet_k.bauman_gis.M_api.ApiRepository;
import com.park.smet_k.bauman_gis.api.BgisApi;
import com.park.smet_k.bauman_gis.model.User;
import com.park.smet_k.bauman_gis.prefs.PreferencesRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private final String LOG_TAG = "LoginRepository";

    private final static String KEY_IS_FIRST = "is_first";
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";

    private final BgisApi mApiRepo;
    private final Context mContext;
    private final PreferencesRepository mPrefsRepo;
    private String mCurrentUser;
    private MutableLiveData<AuthProgress> mAuthProgress;


    public LoginRepository(Context context) {
        mContext = context;
        mApiRepo = ApiRepository.from(mContext).getAPI();
        mPrefsRepo = PreferencesRepository.from(mContext);
    }

    public boolean IsLoged() {
        return mPrefsRepo.getIsFirst();
    }

    public void SkipAuth() {
        // сохраняю айди незареганного пользователя
        mPrefsRepo.putUserID(-1);
        // уже логинился
        mPrefsRepo.putIsFirst(false);

    }

    public LiveData<AuthProgress> login(@NonNull String login, @NonNull String password) {
        if (TextUtils.equals(login, mCurrentUser) && mAuthProgress.getValue() == AuthProgress.IN_PROGRESS) {
            return mAuthProgress;
        } else if (!TextUtils.equals(login, mCurrentUser) && mAuthProgress != null) {
            mAuthProgress.postValue(AuthProgress.FAILED);
        }

        mCurrentUser = login;
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);
        login(mAuthProgress, login, password);

        return mAuthProgress;
    }

    private void login(final MutableLiveData<AuthProgress> progress, @NonNull final String login, @NonNull final String password) {
        mApiRepo.userLogin(new User(login, password)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User body = response.body();
                if (body != null) {
                    Log.d(LOG_TAG, "--- Login OK body != null ---");

                    // сохраняю айди пользователя
                    mPrefsRepo.putUserID(body.getId());
                    // уже логинился
                    mPrefsRepo.putIsFirst(false);

                    progress.postValue(AuthProgress.SUCCESS);
                } else {
                    Log.d(LOG_TAG, "--- Login OK body == null ---");
                    progress.postValue(AuthProgress.FAILED);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progress.postValue(AuthProgress.FAILED);
            }
        });
    }

    public LiveData<AuthProgress> register(@NonNull String login, @NonNull String password) {
        if (TextUtils.equals(login, mCurrentUser) && mAuthProgress.getValue() == AuthProgress.IN_PROGRESS) {
            return mAuthProgress;
        } else if (!TextUtils.equals(login, mCurrentUser) && mAuthProgress != null) {
            mAuthProgress.postValue(AuthProgress.FAILED);
        }

        mCurrentUser = login;
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);
        register(mAuthProgress, login, password);

        return mAuthProgress;
    }

    private void register(final MutableLiveData<AuthProgress> progress, @NonNull final String login, @NonNull final String password) {
        mApiRepo.userSignUp(new User(login, password)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User body = response.body();
                if (body != null) {
                    Log.d(LOG_TAG, "--- Register OK body != null ---");

                    progress.postValue(AuthProgress.SUCCESS);
                } else {
                    Log.d(LOG_TAG, "--- Register OK body == null ---");
                    progress.postValue(AuthProgress.FAILED);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progress.postValue(AuthProgress.FAILED);
            }
        });
    }


    enum AuthProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}
