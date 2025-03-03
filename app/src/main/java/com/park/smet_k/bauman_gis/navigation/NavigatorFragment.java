package com.park.smet_k.bauman_gis.navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.park.smet_k.bauman_gis.App;
import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.database.DBWorker;
import com.park.smet_k.bauman_gis.main.MainActivity;
import com.park.smet_k.bauman_gis.model.GoRoute;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class NavigatorFragment extends Fragment {
    private NavigatorViewModel mNavigatorViewModel;


    private final String LOG_TAG = "NavigatorFragment";
    private final static String KEY_IS_FIRST = "is_first";
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";
    private DBWorker dbHelper;
    private String cur_from = "";
    private String cur_to = "";

    public static NavigatorFragment newInstance() {
        NavigatorFragment myFragment = new NavigatorFragment();

        Bundle bundle = new Bundle();
        myFragment.setArguments(bundle);

        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Navigation");

        Repository.init(getActivity());

        return inflater.inflate(R.layout.activity_navigator, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button startNewActivityBtn = view.findViewById(R.id.Calculate);

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.TopFrame, RoutesListFragment.newInstance())
                .commit();

        // создаем объект для создания и управления версиями БД
//        dbHelper = new DBWorker(getActivity());

        Observer<GoRoute> observer = goRoute -> {
            Log.d(LOG_TAG, "here");
            Toast toast = Toast.makeText(getContext(),
                    "found",
                    Toast.LENGTH_SHORT);
            toast.show();

            if (goRoute.getPoints() == null) {
                return;
            }

            if (!goRoute.getPoints().isEmpty()) {
//                List<Pair<Bitmap, Integer>> route = mNavigatorViewModel.buildBitMaps(getResources(), goRoute.getPoints());
                toggleState(goRoute);
            }
        };

        Observer<String> observerError = error -> {
            Log.d(LOG_TAG, error);
            Toast toast = Toast.makeText(getContext(),
                    error,
                    Toast.LENGTH_SHORT);
            toast.show();
        };

        mNavigatorViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NavigatorViewModel.class);

        mNavigatorViewModel
                .getRoute()
                .observe(getViewLifecycleOwner(), observer);

        mNavigatorViewModel
                .getError()
                .observe(getViewLifecycleOwner(), observerError);


        // TODO: delete
//        mNavigatorViewModel.find("TP", "51");

        startNewActivityBtn.setOnClickListener(v -> {

            EditText check_edit = view.findViewById(R.id.InputFrom);
            cur_from = check_edit.getText().toString();

            check_edit = view.findViewById(R.id.InputTo);
            cur_to = check_edit.getText().toString();

            if (cur_from.equals(cur_to)) {
                Toast toast = Toast.makeText(getContext(),
                        "points are same",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            mNavigatorViewModel.find(cur_from, cur_to);

            // TODO(nmerk): show loader

//            if (Repository.getInstance().StairsGraph == null || Repository.getInstance().StairsGraph.getGraphSize() == 0) {
//                Toast toast = Toast.makeText(getContext(),
//                        "stairs graph empty",
//                        Toast.LENGTH_SHORT);
//                toast.show();
//                return;
//            }
//
//            if (Repository.getInstance().StairsArray == null || Repository.getInstance().StairsArray.size() == 0) {
//                Toast toast = Toast.makeText(getContext(),
//                        "stairs array empty",
//                        Toast.LENGTH_SHORT);
//                toast.show();
//                return;
//            }

//            // заносим данные в БД
//            Repository.getInstance().dbWorker.insert(dbHelper, cur_from, cur_to);
//
//            // пушим на сервер
//            Callback<RouteModel> callback = new Callback<RouteModel>() {
//
//                @Override
//                public void onResponse(@NonNull Call<RouteModel> call, Response<RouteModel> response) {
//                    RouteModel body = response.body();
//                    if (body != null) {
//                        Log.d(LOG_TAG, "--- Login OK body != null ---");
//                    } else {
//                        Log.d(LOG_TAG, "--- Login OK body == null ---");
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<RouteModel> call, Throwable t) {
//                    Log.d(LOG_TAG, "--- Login LOGIN_ERROR onFailure ---");
//                    Toast toast = Toast.makeText(getContext(),
//                            "Мы сломали :(",
//                            Toast.LENGTH_SHORT);
//                    toast.show();
//                    t.printStackTrace();
//                }
//            };
//
//            SharedPreferences preferences = getContext().getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
//
//            Integer userId = preferences.getInt(KEY_OAUTH, -1);
//            // avoid static error
//            assert pointFrom != null;
//            assert pointTo != null;
//            Repository.getInstance().bgisApi.pushRoute(new RouteModel(userId, pointFrom.getName(), pointTo.getName())).enqueue(callback);

//            toggleState();
        });
    }

    private void toggleState(GoRoute route) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        Fragment bottom = getActivity().getSupportFragmentManager().findFragmentById(R.id.TopFrame);

        RouteFragment routeFragment = RouteFragment.newInstance(route);

        if (bottom != null && bottom.isAdded()) {
            transaction.remove(bottom);
            Log.d(LOG_TAG, "=== REMOVED FRAGMENT === ");
            transaction.add(R.id.TopFrame, routeFragment);
        } else {
            transaction.add(R.id.TopFrame, routeFragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }
}
