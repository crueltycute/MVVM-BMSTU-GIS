package com.park.smet_k.bauman_gis.M_news;

import androidx.lifecycle.ViewModel;

import com.park.smet_k.bauman_gis.Repository;
import com.park.smet_k.bauman_gis.model.News;

import java.util.ArrayList;
import java.util.List;


public class NewsViewModel extends ViewModel {

    public List<News> newsArrayList;  // TODO(): live data

    public NewsViewModel() {
        newsArrayList = new ArrayList<>();
    }

    public void GetNewsInit() {
        Repository.getInstance().GetNewsInit();
    }
}
