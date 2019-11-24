package com.park.smet_k.bauman_gis.M_news;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.park.smet_k.bauman_gis.activity.MainActivity;
import com.park.smet_k.bauman_gis.model.News;

import java.util.ArrayList;
import java.util.List;


public class NewsViewModel extends AndroidViewModel {
    private NewsRepository mNewsRepo = new NewsRepository(getApplication());
    private LiveData<List<News>> mNews;

    public NewsViewModel(@NonNull Application application, ArrayList<News> mNewsArray) {
        super(application);
    }

    public LiveData<List<News>> getNews() {
        return mNews;
    }

    public void refresh() {
        mNewsRepo.refresh();
    }
}
