package com.park.smet_k.bauman_gis.M_news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.activity.MainActivity;
import com.park.smet_k.bauman_gis.model.News;
import com.park.smet_k.bauman_gis.recycler.AdapterNewsList;

import java.util.List;

public class NewsFragment extends Fragment {
    private NewsViewModel mNewsViewModel;

    private RecyclerView mRecyclerView;

    private final String LOG_TAG = "NewsList";

    public static Fragment newInstance() {
        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("News");

        return inflater.inflate(R.layout.server_news_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.news_list);

        AdapterNewsList adapterNewsList = new AdapterNewsList(getContext(), this::onItemClick);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapterNewsList);
        mRecyclerView.setHasFixedSize(true);

        Observer<List<News>> observer = new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                if (news != null) {
                    adapterNewsList.setNews(news);
                }
            }
        };

//        mNewsViewModel = new ViewModelProvider(getActivity()).get(NewsViewModel.class);
//        mNewsViewModel.getNews().observe(getViewLifecycleOwner(), observer);
        mNewsViewModel = new ViewModelProvider(getActivity())
                .get(NewsViewModel.class);
        mNewsViewModel
                .getNews()
                .observe(getViewLifecycleOwner(), observer);
    }

    private void onItemClick(News i) {
        // TODO(smet1): здесь пока ничего нет, если будет надо, взять из RoutesListFragment
    }

//    @Override
//    public void onResume() {
//        Log.d(LOG_TAG, "=== ON RESUME === ");
//
//        AdapterNewsList adapterNewsList = new AdapterNewsList(getContext(), this::onItemClick);
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.setAdapter(adapterNewsList);
//        mRecyclerView.setHasFixedSize(true);
//
//        if (mViewModel.newsArrayList == null) {
//            mViewModel.GetNewsInit();
//        }
//
//        for (News i : mViewModel.newsArrayList) {
//            adapterNewsList.add(i);
//        }
//
//        super.onResume();
//    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "=== ON PAUSE === ");

        super.onPause();
    }
}
