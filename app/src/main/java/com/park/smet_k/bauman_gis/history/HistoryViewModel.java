package com.park.smet_k.bauman_gis.history;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.park.smet_k.bauman_gis.model.History;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private HistoryRepository mHistRepo = new HistoryRepository(getApplication());
    private LiveData<List<History>> mHistory = mHistRepo.getHistory();

    public HistoryViewModel(@NotNull Application application) {
        super(application);
    }

    public LiveData<List<History>> getHistory() {
        return mHistory;
    }
}
