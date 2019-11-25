package com.park.smet_k.bauman_gis.news;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.park.smet_k.bauman_gis.api.ApiRepository;
import com.park.smet_k.bauman_gis.api.BgisApi;
import com.park.smet_k.bauman_gis.model.News;
import com.park.smet_k.bauman_gis.model.NewsModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private final static MutableLiveData<List<News>> mNews = new MutableLiveData<>();

    static {
        mNews.setValue(Collections.emptyList());
    }

    private final Context mContext;
    private BgisApi mApi;


    public NewsRepository(Context context) {
        mContext = context;
        mApi = ApiRepository.from(mContext).getAPI();
    }

    public LiveData<List<News>> getNews() {
        return mNews;
    }

    public void refresh() {
        mApi.getNews().enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mNews.postValue(transform(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                Log.e("news repository", "failed to load", t);
            }
        });
    }

    private static List<News> transform(List<NewsModel> modelList) {
        List<News> res = new ArrayList<>();

        for (NewsModel newsModel : modelList) {
            try {
                News news = map(newsModel);
                res.add(0, news);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private static News map(NewsModel model) throws ParseException {
        return new News(model.getTitle(), model.getPayload());
    }
}
