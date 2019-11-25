package com.park.smet_k.bauman_gis.M_login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.activity.MainActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = "LoginActivity";

    private TextInputEditText emailLogin;
    private TextInputEditText passwordLogin;
    private TextInputEditText emailSignup;
    private TextInputEditText passwordSignup;

    private final static String KEY_IS_FIRST = "is_first";
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";

    private View registerForm;
    private Button registerBtn;
    private TextView registerSwitch;

    private View loginForm;
    private Button loginBtn;
    private TextView loginSwitch;

    private LoginViewModel mLoginViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        emailLogin = findViewById(R.id.edit_email_login);
        passwordLogin = findViewById(R.id.edit_password_login);

        emailSignup = findViewById(R.id.edit_email_signup);
        passwordSignup = findViewById(R.id.edit_password_signup);

        registerForm = findViewById(R.id.linearLayoutSignUp);
        registerBtn = findViewById(R.id.signup);
        registerSwitch = findViewById(R.id.textViewRegister);

        loginForm = findViewById(R.id.linearLayoutLogin);
        loginBtn = findViewById(R.id.login);
        loginSwitch = findViewById(R.id.textViewLogin);

        registerBtn.setOnClickListener(this);
        registerSwitch.setOnClickListener(this);

        loginBtn.setOnClickListener(this);
        loginSwitch.setOnClickListener(this);

        findViewById(R.id.skip).setOnClickListener(this);

        registerForm.animate().translationX(3000);
        registerBtn.animate().translationX(3000);
        registerSwitch.animate().translationX(3000);


        // =================
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // уже залогинился
        if (!mLoginViewModel.IsLogged()) {
            startMainActivity();
        }
        // =================

        mLoginViewModel.getProgress().observe(this, loginState -> {
            if (loginState == LoginViewModel.LoginState.LOGIN_FAILED) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                loginBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn_error));

            } else if (loginState == LoginViewModel.LoginState.REGISTER_FAILED) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                registerBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn_error));

            } else if (loginState == LoginViewModel.LoginState.LOGIN_ERROR) {
                Toast.makeText(this, "Invalid login or password", Toast.LENGTH_LONG).show();
                loginBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn_error));

            } else if (loginState == LoginViewModel.LoginState.REGISTER_ERROR) {
                Toast.makeText(this, "Invalid login or password", Toast.LENGTH_LONG).show();
                registerBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn_error));

            } else if (loginState == LoginViewModel.LoginState.LOGIN_IN_PROGRESS) {
                loginBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn_process));

            } else if (loginState == LoginViewModel.LoginState.REGISTER_IN_PROGRESS) {
                registerBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn_process));

            } else if (loginState == LoginViewModel.LoginState.LOGIN_SUCCESS) {
                Toast.makeText(this, "Success login", Toast.LENGTH_LONG).show();
                startMainActivity();

            } else if (loginState == LoginViewModel.LoginState.REGISTER_SUCCESS) {
                Toast.makeText(this, "Success, now login please", Toast.LENGTH_LONG).show();

                registerForm.animate().translationX(3000);
                registerBtn.animate().translationX(3000);
                registerSwitch.animate().translationX(3000);

                loginForm.animate().translationX(0);
                loginBtn.animate().translationX(0);
                loginSwitch.animate().translationX(0);
                registerBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn_process));

            } else {
                loginBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn));
                registerBtn.setBackground(getResources().getDrawable(R.drawable.contained_rounded_btn));
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        switch (v.getId()) {
            case R.id.login:
                findViewById(R.id.login).setEnabled(false);
                findViewById(R.id.textViewLogin).setEnabled(false);
                v.startAnimation(animAlpha);

                userLogin();

                findViewById(R.id.login).setEnabled(true);
                findViewById(R.id.textViewLogin).setEnabled(true);
                break;

            case R.id.signup:
                findViewById(R.id.signup).setEnabled(false);
                findViewById(R.id.textViewRegister).setEnabled(false);
                v.startAnimation(animAlpha);

                userRegister();

                findViewById(R.id.login).setEnabled(true);
                findViewById(R.id.signup).setEnabled(true);
                findViewById(R.id.textViewRegister).setEnabled(true);
                break;

            case R.id.textViewLogin:
                findViewById(R.id.login).setEnabled(false);
                findViewById(R.id.textViewLogin).setEnabled(false);
                v.startAnimation(animAlpha);


                emailLogin.clearFocus();
                emailLogin.setError(null);
                Objects.requireNonNull(emailLogin.getText()).clear();

                passwordLogin.clearFocus();
                passwordLogin.setError(null);
                Objects.requireNonNull(passwordLogin.getText()).clear();


                registerForm.animate().translationX(0);
                registerBtn.animate().translationX(0);
                registerSwitch.animate().translationX(0);

                loginForm.animate().translationX(-3000);
                loginBtn.animate().translationX(-3000);
                loginSwitch.animate().translationX(-3000);

                findViewById(R.id.login).setEnabled(true);
                findViewById(R.id.textViewLogin).setEnabled(true);
                break;

            case R.id.textViewRegister:
                findViewById(R.id.signup).setEnabled(false);
                findViewById(R.id.textViewRegister).setEnabled(false);
                v.startAnimation(animAlpha);


                emailSignup.clearFocus();
                emailSignup.setError(null);
                Objects.requireNonNull(emailSignup.getText()).clear();

                passwordSignup.clearFocus();
                passwordSignup.setError(null);
                Objects.requireNonNull(passwordSignup.getText()).clear();


                registerForm.animate().translationX(3000);
                registerBtn.animate().translationX(3000);
                registerSwitch.animate().translationX(3000);

                loginForm.animate().translationX(0);
                loginBtn.animate().translationX(0);
                loginSwitch.animate().translationX(0);

                findViewById(R.id.signup).setEnabled(true);
                findViewById(R.id.textViewRegister).setEnabled(true);
                break;

            case R.id.skip:
                Log.d(LOG_TAG, "--- Skip ---");

                mLoginViewModel.SkipAuth();
                startMainActivity();
                break;

        }
    }

    private void userLogin() {
        String email_str = Objects.requireNonNull(emailLogin.getText()).toString().trim();
        String password_str = Objects.requireNonNull(passwordLogin.getText()).toString().trim();

        if (email_str.isEmpty()) {
            emailLogin.setError("email required");
            emailLogin.requestFocus();
            return;
        }

//        if (!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()) {
//            email.setError("enter valid email");
//            email.requestFocus();
//            return;
//        }

        if (password_str.isEmpty()) {
            passwordLogin.setError("password required");
            passwordLogin.requestFocus();
            return;
        }

        mLoginViewModel.login(email_str, password_str);
    }

    private void userRegister() {
        String email_str = emailSignup.getText().toString().trim();
        String password_str = passwordSignup.getText().toString().trim();

        if (email_str.isEmpty()) {
            emailSignup.setError("email required");
            emailSignup.requestFocus();
            return;
        }

//        if (!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()) {
//            email.setError("enter valid email");
//            email.requestFocus();
//            return;
//        }

        if (password_str.isEmpty()) {
            passwordSignup.setError("password required");
            passwordSignup.requestFocus();
            return;
        }

        mLoginViewModel.register(email_str, password_str);
//        Callback<User> callback = new Callback<User>() {
//
//            @Override
//            public void onResponse(@NonNull Call<User> call, Response<User> response) {
//                User body = response.body();
//                if (body != null) {
//                    Log.d(LOG_TAG, "--- Login OK body != null ---");
//
//                    registerForm.animate().translationX(3000);
//                    registerBtn.animate().translationX(3000);
//                    registerSwitch.animate().translationX(3000);
//
//                    loginForm.animate().translationX(0);
//                    loginBtn.animate().translationX(0);
//                    loginSwitch.animate().translationX(0);
//
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "Success, now login please",
//                            Toast.LENGTH_SHORT);
//                    toast.show();
//                } else {
//                    Log.d(LOG_TAG, "--- Login OK body == null ---");
//
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "Invalid login/password",
//                            Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(LOG_TAG, "--- Login LOGIN_ERROR onFailure ---");
//                Toast toast = Toast.makeText(getApplicationContext(),
//                        "Server Error",
//                        Toast.LENGTH_SHORT);
//                toast.show();
//                t.printStackTrace();
//            }
//        };
//
//        // avoid static error
//        Repository.getInstance().bgisApi.userSignUp(new User(email_str, password_str)).enqueue(callback);
    }
}
