package com.park.smet_k.bauman_gis;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class RouteFragment extends Fragment {
    // просчет маршрута при создании фрагмента
    String LOG_TAG = "RouteFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blue, container, false);
        return view;
    }

    public static RouteFragment newInstance(int from, int to) {
        Bundle args = new Bundle();
        args.putInt("from", from);
        args.putInt("to", to);

        RouteFragment fragment = new RouteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Integer from = 0;
        Integer to = 0;

        Bundle args = getArguments();
        if (args != null) {
            from = args.getInt("from");
            to = args.getInt("to");

        }

        TextView textView = view.findViewById(R.id.from);
        textView.setText(from.toString());

        textView = view.findViewById(R.id.to);
        textView.setText(to.toString());


//        GridWithWeights grid = new GridWithWeights(10, 10);
//        grid.add_rect(1, 7, 4, 9);
////        Integer x1 = 1, x2 = 4, y1 = 7, y2 = 9;
////        for (Integer x = x1; x < x2; ++x) {
////            for (Integer y = y1; y < y2; ++y) {
////                grid.walls.add(new GridLocation(x, y));
////            }
////        }
//
//
//        GridLocation start = new GridLocation(1, 4);
//        GridLocation goal = new GridLocation( 8, 5);
//
////        int check = start.compare(goal);
////        Log.d(LOG_TAG, Integer.toString(check));
//
////        Comparator<Pair<GridLocation, Double>> PQComparator = (c1, c2) -> (int) (c1.second - c2.second);
//        TreeMap<GridLocation, GridLocation> came_from = new TreeMap<>(GridLocation::compare);
//        TreeMap<GridLocation, Double> cost_so_far = new TreeMap<>(GridLocation::compare);
//
//        AStarSearch test = new AStarSearch();
//        test.doAStarSearch(grid, start, goal, came_from, cost_so_far);
//
//        ArrayList<GridLocation> path = test.reconstruct_path(start, goal, came_from);

        WeightedGraph graph = new WeightedGraph(6, 9);

        // заполняем
        graph.addEdge(0, 3, 1);
        graph.addEdge(0, 4, 2);
        graph.addEdge(1,2, 7);
        graph.addEdge(1,3, 2);
        graph.addEdge(1,4, 3);
        graph.addEdge(1,5, 3);
        graph.addEdge(2,5, 3);
        graph.addEdge(3,4, 4);
        graph.addEdge(3, 5, 6);

        // фиксируем прибыль (уличная магия)
//        textView.setText(Arrays.toString(graph.getNextVertices(0).stream().toArray(String[]::new)));
        ArrayList<Pair<Integer, Integer>> tmp = graph.getNextVertices(0);
        ArrayList<Integer> route = new ArrayList<>();
        route = graph.dijkstra(0, 2);

//        6
//        9
//        0 3 1
//        0 4 2
//        1 2 7
//        1 3 2
//        1 4 3
//        1 5 3
//        2 5 3
//        3 4 4
//        3 5 6
//        0 2
    }
}

