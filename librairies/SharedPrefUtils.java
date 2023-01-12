package com.app.fap.librairies;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    private static final String SHARED_PREF_NAME = "com.app.fap.sharedpref";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }

    public static String getSavedString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

}
