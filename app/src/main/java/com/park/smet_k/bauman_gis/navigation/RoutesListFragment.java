package com.park.smet_k.bauman_gis.navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.database.DBWorker;
import com.park.smet_k.bauman_gis.model.Route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class RoutesListFragment extends Fragment {

    private final static String KEY = "list";

    private List<Route> recentRoutes = new ArrayList<>();
    private RecyclerView numbers;
    private DBWorker dbHelper;


    public static RoutesListFragment newInstance(Collection<Route> in) {
        RoutesListFragment myFragment = new RoutesListFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, (Serializable) in);
        myFragment.setArguments(bundle);

        return myFragment;
    }

    public static Fragment newInstance() {
        RoutesListFragment myFragment = new RoutesListFragment();

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
        return inflater.inflate(R.layout.recycler_fragment_route_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numbers = view.findViewById(R.id.route_list);
    }

    private void onItemClick(Route i) {
        // getChildFragmentManager не работает, нельзя бросить данные в другой фрагмент
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Bundle bundle = new Bundle();
        Fragment bottom = getFragmentManager().findFragmentById(R.id.TopFrame);

        // FIXME передавать картинки
//        RouteFragment routeFragment = RouteFragment.newInstance(i.getFrom(), i.getTo());
        RouteFragment routeFragment = new RouteFragment();

        // выставляю значения в эдит текст в нижнем баре
        EditText editText = Objects.requireNonNull(getActivity()).findViewById(R.id.InputFrom);
        editText.setText(i.getFrom());

        editText = getActivity().findViewById(R.id.InputTo);
        editText.setText(i.getTo());

        assert bottom != null;
        transaction.remove(bottom);
        transaction.add(R.id.TopFrame, routeFragment);

        transaction.addToBackStack(null);
        transaction.commit();
    }


    final String LOG_TAG = "RoutesList";

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "=== ON RESUME === ");

        dbHelper = new DBWorker(getActivity());


        List<Route> listToShow = new ArrayList<>();  // TODO(nmerk): убрать хардкод
//        List<Route> listToShow = Repository.getInstance().dbWorker.select(dbHelper, "all", "");

        AdapterRoutesList numbersAdapter = new AdapterRoutesList(getContext(), this::onItemClick);

        numbers.setLayoutManager(new LinearLayoutManager(getContext()));
        numbers.setAdapter(numbersAdapter);
        numbers.setHasFixedSize(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            recentRoutes = listToShow;
        }

        for (Route i : recentRoutes) {
            numbersAdapter.add(i);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "=== ON PAUSE === ");

        super.onPause();
    }

}

