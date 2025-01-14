package com.park.smet_k.bauman_gis.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.main.MainActivity;
import com.park.smet_k.bauman_gis.database.DBWorker;

import java.util.Objects;

// TODO(): переделать настройки на PreferenceFragmentCompat
// https://developer.android.com/guide/topics/ui/settings/?hl=ru

public class SettingsFragment extends Fragment {
    private final static String KEY_IS_FIRST = "is_first";
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";
    private DBWorker dbWorker;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Settings");

        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbWorker = new DBWorker(getActivity());
        Button reset = Objects.requireNonNull(getActivity()).findViewById(R.id.clear_button);
//        Button reset = getActivity().findViewById(R.id.clear_button);

        reset.setOnClickListener(v -> {
            Toast toast = Toast.makeText(getActivity(),
            "И тут сломали...",
            Toast.LENGTH_SHORT);
            toast.show();
            return;
//            Repository.getInstance().dbWorker.truncate(dbWorker);
//            SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
//            SharedPreferences preferences = getContext().getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
//
//            Integer userId = preferences.getInt(KEY_OAUTH, -1);
//
//            Callback<Message> callback = new Callback<Message>() {
//                @Override
//                public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<Message> call, Throwable t) {
//                    t.printStackTrace();
//                }
//
//            };
//
//            // avoid static error
//            Repository.getInstance().bgisApi.deleteHistory(userId).enqueue(callback);
//
//            Toast toast = Toast.makeText(getActivity(),
//                    "История маршрутов очищена!",
//                    Toast.LENGTH_SHORT);
//            toast.show();
        });
    }
}
