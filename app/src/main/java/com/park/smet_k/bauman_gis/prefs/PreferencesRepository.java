package com.park.smet_k.bauman_gis.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.park.smet_k.bauman_gis.App;

public class PreferencesRepository {
    private final static String KEY_IS_FIRST = "is_first";
    private final static String KEY_OAUTH = "oauth";
    private final static String STORAGE_NAME = "storage";

    private final SharedPreferences mPrefs;
    private final Context mContext;

    public PreferencesRepository(Context context) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
    }

    public void putUserID(int ID) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(KEY_OAUTH, ID);
        editor.apply();
    }

    public int getUserID() {
        return mPrefs.getInt(KEY_OAUTH, -1);
    }

    public void putIsFirst(boolean first) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_IS_FIRST, first);
        editor.apply();
    }

    public boolean getIsFirst() {
        return mPrefs.getBoolean(KEY_IS_FIRST, false);
    }


    public static PreferencesRepository from(Context context) {
        return App.from(context).getPreferencesRepo();
    }
}
