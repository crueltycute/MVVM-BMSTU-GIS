package com.park.smet_k.bauman_gis.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.park.smet_k.bauman_gis.api.ApiRepository;
import com.park.smet_k.bauman_gis.api.BgisApi;
import com.park.smet_k.bauman_gis.history.HistoryFragment;
import com.park.smet_k.bauman_gis.login.LoginActivity;
import com.park.smet_k.bauman_gis.navigation.NavigatorFragment;
import com.park.smet_k.bauman_gis.news.NewsFragment;
import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.database.DBWorker;
import com.park.smet_k.bauman_gis.settings.SettingsFragment;
import com.park.smet_k.bauman_gis.model.RouteModel;
import com.park.smet_k.bauman_gis.model.User;

import java.util.List;
import java.util.Objects;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CHANNEL_DEFAULT = "default";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String LOG_TAG = "MainActivity";

    private BgisApi mApiRepo;

    private final static String KEY_IS_FIRST = "is_first";
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";

    private DBWorker dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiRepo = ApiRepository.from(getApplication()).getAPI();

        // если первый раз запустил приложуху
        SharedPreferences preferences = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
        boolean usr = preferences.getBoolean(KEY_IS_FIRST, true);

//         if (usr) {
//             Intent intent = new Intent(this, LoginActivity.class);
//             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//             startActivity(intent);
//         }
        ////////////////

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navView.setCheckedItem(R.id.home);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getUserInfo();
        getUserRoutes();

        // скачиваем карту
//        Log.d(LOG_TAG, "INIT map");
//        Repository.getInstance().GetNewsInit();
//
//        Repository.getInstance().GetAllStairsInit();
//        Repository.getInstance().LevelsInit();
//        Repository.getInstance().MapPointsInit();

        // показываем новостную ленту
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, NewsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void getUserRoutes() {
        SharedPreferences preferences = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
        Integer userId = preferences.getInt(KEY_OAUTH, -1);
        dbHelper = new DBWorker(this);

        Callback<List<RouteModel>> callback = new Callback<List<RouteModel>>() {

            @Override
            public void onResponse(@NonNull retrofit2.Call<List<RouteModel>> call, Response<List<RouteModel>> response) {

                List<RouteModel> body = response.body();
                if (body != null) {
                    Log.d(LOG_TAG, "--- pullRoutes OK body != null --- id = " + userId.toString());
                    // очищаю таблицу
//                    Repository.getInstance().dbWorker.truncate(dbHelper);

//                    for (RouteModel i : body) {
//                        Repository.getInstance().dbWorker.insert(dbHelper, i.getPoint_from(), i.getPoint_to());
//                    }

                } else {
                    Log.d(LOG_TAG, "--- pullRoutes OK body == null --- id = " + userId.toString());
                    // TODO(): сделать логаут, тк в этом случае пользователя на сервере нет
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<List<RouteModel>> call, Throwable t) {
                Log.d(LOG_TAG, "--- pullRoutes LOGIN_ERROR onFailure ---");

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Server not reachable",
                        Toast.LENGTH_SHORT);
                toast.show();

                t.printStackTrace();
            }
        };

        // avoid static error
//        Repository.getInstance().bgisApi.pullRoutes(userId).enqueue(callback);
    }

    private void getUserInfo() {
        SharedPreferences preferences = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);

        Integer userId = preferences.getInt(KEY_OAUTH, -1);

        if (userId != -1) {
            Callback<User> callback = new Callback<User>() {

                @Override
                public void onResponse(@NonNull retrofit2.Call<User> call, Response<User> response) {

                    User body = response.body();
                    if (body != null) {
                        Log.d(LOG_TAG, "--- getUser OK body != null ---");

                        View navHeader = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
                        TextView displayName = navHeader.findViewById(R.id.display_name);

                        if (displayName == null) {
                            Log.d(LOG_TAG, "--- displayName is null ---");
                        }

                        displayName.setText(body.getLogin());
                    } else {
                        Log.d(LOG_TAG, "--- getUser OK body == null ---");

                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<User> call, Throwable t) {
                    Log.d(LOG_TAG, "--- getUser LOGIN_ERROR onFailure ---");

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Server not reachable",
                            Toast.LENGTH_SHORT);
                    toast.show();

                    t.printStackTrace();
                }
            };

            mApiRepo.getUserInfo(userId).enqueue(callback);
        } else {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            // changing logout to login
            MenuItem nav_camara = menu.findItem(R.id.logout);
            nav_camara.setTitle("Login");

            // TODO(): add new icon for login
//            nav_camara.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_launcher));

            // hiding account button
//            nav_camara = menu.findItem(R.id.account);
//            nav_camara.setVisible(false);

            // hiding header in menu
            View navHeader = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
            navHeader.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, NewsFragment.newInstance())
                        .addToBackStack(null)
                        .commit();

                drawerLayout.closeDrawers();
                break;
            }
            case R.id.history: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, HistoryFragment.newInstance())
                        .addToBackStack(null)
                        .commit();

                drawerLayout.closeDrawers();
                break;
            }
            case R.id.navigation: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, NavigatorFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                drawerLayout.closeDrawers();
                break;
            }
            case R.id.settings: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance())
                        .addToBackStack(null)
                        .commit();

                drawerLayout.closeDrawers();
                break;
            }
            case R.id.logout: {
                SharedPreferences prefs = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(KEY_OAUTH, -1);
                editor.putBoolean(KEY_IS_FIRST, true);
                editor.apply();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
        return true;
    }

}

