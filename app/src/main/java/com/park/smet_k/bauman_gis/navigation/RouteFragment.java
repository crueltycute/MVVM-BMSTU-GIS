package com.park.smet_k.bauman_gis.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.model.GoPoint;
import com.park.smet_k.bauman_gis.model.GoRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteFragment extends Fragment {
    private NavigatorViewModel mNavigatorViewModel;

    // просчет маршрута при создании фрагмента
    String LOG_TAG = "RouteFragment";
    ArrayList<Integer> route;

    private RecyclerView mapsList;
    private List<Pair<Bitmap, Integer>> pathList = new ArrayList<>();


    // насколько нужно умножать пискельные координаты,
    // чтобы они легли на битмапу нормально, без искажений

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        GoRoute goRoute = (GoRoute) bundle.getSerializable("kek");

        assert goRoute != null;

        List<GoPoint> points = goRoute.getPoints();

        mNavigatorViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NavigatorViewModel.class);

        pathList = mNavigatorViewModel.buildBitMaps(getResources(), goRoute.getPoints());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blue, container, false);
    }

    public static RouteFragment newInstance(GoRoute route) {
        Bundle args = new Bundle();

        args.putSerializable("kek", route);

        RouteFragment fragment = new RouteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapsList = view.findViewById(R.id.path_list);

        AdapterPathList adapterPathList = new AdapterPathList(getContext(), this::onItemClick);

        mapsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mapsList.setAdapter(adapterPathList);
        mapsList.setHasFixedSize(true);

//        for (Pair<Bitmap, Integer> i : pathList) {
//            adapterPathList.add(i);
//        }
        for (int i = pathList.size() - 1; i >= 0; i--) {
            adapterPathList.add(pathList.get(i));
        }
    }

    private void onItemClick(Pair<Bitmap, Integer> i) {
        // TODO(): здесь пока ничего нет, если будет надо, взять из RoutesListFragment
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }
}
