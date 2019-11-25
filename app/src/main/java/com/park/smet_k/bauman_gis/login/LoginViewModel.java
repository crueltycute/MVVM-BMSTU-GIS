package com.park.smet_k.bauman_gis.login;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.Objects;

public class LoginViewModel extends AndroidViewModel {
    private LoginData mLastLoginData = new LoginData("", "");

    private MediatorLiveData<LoginState> mLoginState = new MediatorLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mLoginState.setValue(LoginState.NONE);
    }

    public boolean IsLogged() {
        return new LoginRepository(getApplication()).IsLoged();
    }

    public void SkipAuth() {
        new LoginRepository(getApplication()).SkipAuth();
    }


    public LiveData<LoginState> getProgress() {
        return mLoginState;
    }

    public void login(String login, String password) {
        LoginData last = mLastLoginData;
        LoginData loginData = new LoginData(login, password);
        mLastLoginData = loginData;

        if (!loginData.isValid()) {
            mLoginState.postValue(LoginState.LOGIN_ERROR);
        } else if (last != null && last.equals(loginData)) {
            Log.w("LoginViewModel", "Ignoring duplicate request with login data");
        } else if (mLoginState.getValue() != LoginState.LOGIN_IN_PROGRESS) {
            requestLogin(loginData);
        }
    }

    private void requestLogin(final LoginData loginData) {
        mLoginState.postValue(LoginState.LOGIN_IN_PROGRESS);
        final LiveData<LoginRepository.AuthProgress> progressLiveData = new LoginRepository(getApplication())
                .login(loginData.getLogin(), loginData.getPassword());
        mLoginState.addSource(progressLiveData, authProgress -> {
            if (authProgress == LoginRepository.AuthProgress.LOGIN_SUCCESS) {
                mLoginState.postValue(LoginState.LOGIN_SUCCESS);
                mLoginState.removeSource(progressLiveData);
            } else if (authProgress == LoginRepository.AuthProgress.LOGIN_FAILED) {
                mLoginState.postValue(LoginState.LOGIN_ERROR);
                mLoginState.removeSource(progressLiveData);
            }
        });
    }

    public void register(String login, String password) {
        LoginData last = mLastLoginData;
        LoginData loginData = new LoginData(login, password);
        mLastLoginData = loginData;

        if (!loginData.isValid()) {
            mLoginState.postValue(LoginState.REGISTER_ERROR);
        } else if (last != null && last.equals(loginData)) {
            Log.w("LoginViewModel", "Ignoring duplicate request with login data");
        } else if (mLoginState.getValue() != LoginState.REGISTER_IN_PROGRESS) {
            requestRegister(loginData);


            // TODO выпилить этот костыль)00000
            requestLogin(loginData);
        }
    }

    private void requestRegister(final LoginData loginData) {
        mLoginState.postValue(LoginState.REGISTER_IN_PROGRESS);
        final LiveData<LoginRepository.AuthProgress> progressLiveData = new LoginRepository(getApplication())
                .register(loginData.getLogin(), loginData.getPassword());
        mLoginState.addSource(progressLiveData, authProgress -> {
            if (authProgress == LoginRepository.AuthProgress.REGISTER_SUCCESS) {
                mLoginState.postValue(LoginState.REGISTER_SUCCESS);
                mLoginState.removeSource(progressLiveData);
            } else if (authProgress == LoginRepository.AuthProgress.REGISTER_FAILED) {
                mLoginState.postValue(LoginState.REGISTER_ERROR);
                mLoginState.removeSource(progressLiveData);
            }
        });
    }


    enum LoginState {
        NONE,
        LOGIN_ERROR,
        LOGIN_IN_PROGRESS,
        LOGIN_SUCCESS,
        LOGIN_FAILED,

        REGISTER_ERROR,
        REGISTER_IN_PROGRESS,
        REGISTER_SUCCESS,
        REGISTER_FAILED
    }

    public static class LoginData {
        private final String mLogin;
        private final String mPassword;

        public LoginData(String login, String password) {
            mLogin = login;
            mPassword = password;
        }

        public String getLogin() {
            return mLogin;
        }

        public String getPassword() {
            return mPassword;
        }

        public boolean isValid() {
            return !TextUtils.isEmpty(mLogin) && !TextUtils.isEmpty(mPassword);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LoginData loginData = (LoginData) o;
            return Objects.equals(mLogin, loginData.mLogin) &&
                    Objects.equals(mPassword, loginData.mPassword);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mLogin, mPassword);
        }
    }
}
