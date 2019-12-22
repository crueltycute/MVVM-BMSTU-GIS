package com.park.smet_k.bauman_gis.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.main.MainActivity;
import com.park.smet_k.bauman_gis.model.History;

import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment {
    private HistoryViewModel mHistoryViewModel;

    private RecyclerView mRecyclerView;

    private final String LOG_TAG = "HistoryList";

    public static HistoryFragment newInstance() {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("History");

        return inflater.inflate(R.layout.item_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.history_list);

        HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), this::onItemClick);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(historyAdapter);
        mRecyclerView.setHasFixedSize(true);

        Observer<List<History>> observer = new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                if (histories != null) {
                    historyAdapter.setHistories(histories);
                }
            }
        };

        mHistoryViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(HistoryViewModel.class);
        mHistoryViewModel
                .getHistory()
                .observe(getViewLifecycleOwner(), observer);
    }


    private void onItemClick(History i) {
        // TODO(smet1): здесь пока ничего нет, если будет надо, взять из RoutesListFragment
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "=== ON PAUSE === ");

        super.onPause();
    }
}
