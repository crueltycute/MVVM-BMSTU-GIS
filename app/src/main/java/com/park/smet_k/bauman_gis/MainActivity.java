package com.park.smet_k.bauman_gis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.park.smet_k.bauman_gis.model.RouteModel;
import com.park.smet_k.bauman_gis.model.User;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CHANNEL_DEFAULT = "default";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    String LOG_TAG = "MainActivity";

    private final static String KEY_IS_FIRST = "is_first";
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";

    DBWorker dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getUserInfo();
        getUserRoutes();
    }

    private void getUserRoutes() {
        SharedPreferences preferences = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
        Integer userId = preferences.getInt(KEY_OAUTH, -1);
        dbHelper = new DBWorker(this);

        Callback<List<RouteModel>> callback = new Callback<List<RouteModel>>() {

            @Override
            public void onResponse(retrofit2.Call<List<RouteModel>> call, Response<List<RouteModel>> response) {

                List<RouteModel> body = response.body();
                if (body != null) {
                    Log.d(LOG_TAG, "--- pullRoutes OK body != null --- id = " + userId.toString());
                    // очищаю таблицу
                    AppComponent.getInstance().dbWorker.truncate(dbHelper);

                    for (RouteModel i : body) {
                        AppComponent.getInstance().dbWorker.insert(dbHelper, i.getPoint_from(), i.getPoint_to());
                    }

                } else {
                    Log.d(LOG_TAG, "--- pullRoutes OK body == null --- id = " + userId.toString());
                    // TODO(): сделать логаут, тк в этом случае пользователя на сервере нет
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<RouteModel>> call, Throwable t) {
                Log.d(LOG_TAG, "--- pullRoutes ERROR onFailure ---");

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Server not reachable",
                        Toast.LENGTH_SHORT);
                toast.show();

                t.printStackTrace();
            }
        };

        // avoid static error
        AppComponent.getInstance().bgisApi.pullRoutes(userId).enqueue(callback);
    }

    private void getUserInfo() {
        SharedPreferences preferences = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);

        Integer userId = preferences.getInt(KEY_OAUTH, -1);

        Callback<User> callback = new Callback<User>() {

            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {

                User body = response.body();
                if (body != null) {
                    Log.d(LOG_TAG, "--- getUser OK body != null ---");

                    View navHeader = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
                    TextView displayName = navHeader.findViewById(R.id.display_name);

                    displayName.setText(body.getLogin());
                } else {
                    Log.d(LOG_TAG, "--- getUser OK body == null ---");

                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Log.d(LOG_TAG, "--- getUser ERROR onFailure ---");

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Server not reachable",
                        Toast.LENGTH_SHORT);
                toast.show();

                t.printStackTrace();
            }
        };

        // avoid static error
        AppComponent.getInstance().bgisApi.getUserInfo(userId).enqueue(callback);
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
                        .replace(R.id.container, ServerNewsFragment.newInstance())
                        .addToBackStack(null)
                        .commit();

                drawerLayout.closeDrawers();
                break;
            }

            case R.id.account: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AccountFragment.newInstance())
                        .addToBackStack(null)
                        .commit();

                drawerLayout.closeDrawers();
                break;
            }
            case R.id.navigation: {
                final Intent navIntent = new Intent(MainActivity.this, NavigatorActivity.class);

                startActivity(navIntent);
                // TODO(): есть ли смысл закрывать шторку, т.к. анимация
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
