package com.park.smet_k.bauman_gis.news;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.park.smet_k.bauman_gis.model.News;

import java.util.List;


public class NewsViewModel extends AndroidViewModel {
    private NewsRepository mNewsRepo = new NewsRepository(getApplication());
    private LiveData<List<News>> mNews = mNewsRepo.getNews();

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<News>> getNews() {
        return mNews;
    }

    public void refresh() {
        mNewsRepo.refresh();
    }
}
