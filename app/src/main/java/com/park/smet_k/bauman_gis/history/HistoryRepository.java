package com.park.smet_k.bauman_gis.history;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.park.smet_k.bauman_gis.api.ApiRepository;
import com.park.smet_k.bauman_gis.api.BgisApi;
import com.park.smet_k.bauman_gis.model.History;
import com.park.smet_k.bauman_gis.model.HistoryModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryRepository {
    private final static MutableLiveData<List<History>> mHistory = new MutableLiveData<>();

    static {
        mHistory.setValue(Collections.emptyList());
    }

    private final Context mContext;
    private BgisApi mApi;

    public HistoryRepository(Context context) {
        mContext = context;
        mApi = ApiRepository.from(mContext).getAPI();
    }

    public LiveData<List<History>> getHistory() {
        return mHistory;
    }

    private static List<History> transform(List<HistoryModel> modelList) {
        List<History> res = new ArrayList<>();

        for (HistoryModel historyModel : modelList) {
            try {
                History history = map(historyModel);
                res.add(0, history);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private static History map(HistoryModel model) throws ParseException {
        return new History(model.getFrom(), model.getTo());
    }
}
