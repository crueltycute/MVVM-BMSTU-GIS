package com.park.smet_k.bauman_gis.news;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.main.MainActivity;
import com.park.smet_k.bauman_gis.model.News;

import java.util.List;
import java.util.Objects;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private NewsViewModel mNewsViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
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

        View view = inflater.inflate(R.layout.server_news_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.news_list);

        NewsAdapter newsAdapter = new NewsAdapter(getContext(), this::onItemClick);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(newsAdapter);
        mRecyclerView.setHasFixedSize(true);

        Observer<List<News>> observer = new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                if (news != null) {
                    newsAdapter.setNews(news);
                }
            }
        };

        mNewsViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NewsViewModel.class);
        mNewsViewModel
                .getNews()
                .observe(getViewLifecycleOwner(), observer);

        mNewsViewModel.refresh();
    }

    private void onItemClick(News i) {
        // TODO(smet1): здесь пока ничего нет, если будет надо, взять из RoutesListFragment
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "=== ON PAUSE === ");

        super.onPause();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        mNewsViewModel.refresh();
    }
}
