package com.park.smet_k.bauman_gis.M_news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.activity.MainActivity;
import com.park.smet_k.bauman_gis.model.News;
import com.park.smet_k.bauman_gis.recycler.AdapterNewsList;

import java.util.Objects;

public class NewsFragment extends Fragment {
    private NewsViewModel mViewModel = new NewsViewModel();
    private RecyclerView mRecyclerView;

    private final String LOG_TAG = "NewsList";
    
    public static Fragment newInstance() {
        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("News");

        Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        View view = inflater.inflate(R.layout.server_news_fragment, container, false);

        Button updateBtn = Objects.requireNonNull(view).findViewById(R.id.update);
        assert updateBtn != null;

        updateBtn.setOnClickListener(v -> {
            mViewModel.GetNewsInit();

            updateBtn.startAnimation(animAlpha);

            // TODO(nmerk): придумать способ обновлять новости лучший, чем замена фрагмента
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.TopFrameNews, NewsListFragment.newInstance())
//                    .commit();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.news_list);
    }

    private void onItemClick(News i) {
        // TODO(smet1): здесь пока ничего нет, если будет надо, взять из RoutesListFragment
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "=== ON RESUME === ");
        
        AdapterNewsList adapterNewsList = new AdapterNewsList(getContext(), this::onItemClick);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapterNewsList);
        mRecyclerView.setHasFixedSize(true);

        if (mViewModel.newsArrayList == null) {
            mViewModel.GetNewsInit();
        }

        for (News i : mViewModel.newsArrayList) {
            adapterNewsList.add(i);
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "=== ON PAUSE === ");

        super.onPause();
    }
}