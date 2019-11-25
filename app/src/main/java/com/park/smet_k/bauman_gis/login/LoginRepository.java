package com.park.smet_k.bauman_gis.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.park.smet_k.bauman_gis.api.ApiRepository;
import com.park.smet_k.bauman_gis.api.BgisApi;
import com.park.smet_k.bauman_gis.model.User;
import com.park.smet_k.bauman_gis.prefs.PreferencesRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private final String LOG_TAG = "LoginRepository";

    private final BgisApi mApiRepo;
    private final Context mContext;
    private final PreferencesRepository mPrefsRepo;
    private String mCurrentUser;
    private MutableLiveData<AuthProgress> mAuthProgress;
    private MutableLiveData<AuthProgress> mRegisterProgress;


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
        if (TextUtils.equals(login, mCurrentUser) && mAuthProgress.getValue() == AuthProgress.LOGIN_IN_PROGRESS) {
            return mAuthProgress;
        } else if (!TextUtils.equals(login, mCurrentUser) && mAuthProgress != null) {
            mAuthProgress.postValue(AuthProgress.LOGIN_FAILED);
        }

        mCurrentUser = login;
        mAuthProgress = new MutableLiveData<>(AuthProgress.LOGIN_IN_PROGRESS);
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

                    progress.postValue(AuthProgress.LOGIN_SUCCESS);
                } else {
                    Log.d(LOG_TAG, "--- Login OK body == null ---");
                    progress.postValue(AuthProgress.LOGIN_FAILED);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progress.postValue(AuthProgress.LOGIN_FAILED);
            }
        });
    }

    public LiveData<AuthProgress> register(@NonNull String login, @NonNull String password) {
        if (TextUtils.equals(login, mCurrentUser) && mRegisterProgress.getValue() == AuthProgress.REGISTER_IN_PROGRESS) {
            return mRegisterProgress;
        } else if (!TextUtils.equals(login, mCurrentUser) && mAuthProgress != null) {
            mRegisterProgress.postValue(AuthProgress.REGISTER_FAILED);
        }

        mCurrentUser = login;
        mRegisterProgress = new MutableLiveData<>(AuthProgress.REGISTER_IN_PROGRESS);
        register(mRegisterProgress, login, password);

        return mRegisterProgress;
    }

    private void register(final MutableLiveData<AuthProgress> progress, @NonNull final String login, @NonNull final String password) {
        mApiRepo.userSignUp(new User(login, password)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User body = response.body();
                if (body != null) {
                    Log.d(LOG_TAG, "--- Register OK body != null ---");

                    progress.postValue(AuthProgress.REGISTER_SUCCESS);
                } else {
                    Log.d(LOG_TAG, "--- Register OK body == null ---");
                    progress.postValue(AuthProgress.REGISTER_FAILED);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progress.postValue(AuthProgress.REGISTER_FAILED);
            }
        });
    }


    enum AuthProgress {
        LOGIN_IN_PROGRESS,
        LOGIN_SUCCESS,
        LOGIN_FAILED,
        REGISTER_IN_PROGRESS,
        REGISTER_SUCCESS,
        REGISTER_FAILED
    }
}
