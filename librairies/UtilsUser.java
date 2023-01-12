package com.app.fap.librairies;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.fap.models.OpenCampagne;
import com.app.fap.models.User;
import com.app.fap.models.UserPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UtilsUser {
    private static User user;
    public static final String UserInfoKey = "UserInfo";
    public static final String LastUserIdKey = "LastUserIdKey";

    public static long getLastUserId(Context context) {
        if (user != null)
            return user.getIdUser();
        else if (context != null)
            return getLastUserFromPreferences(context);
        else
            return 0;
    }

    public static User getUser(Context context) {
        if (user != null)
            return user;
        else if (context != null)
            return getSavedUserFromPreferences(context);
        else
            return null;
    }

    public static void saveUserToPreferences(Context context, User user) {
        SharedPreferences settings = context.getSharedPreferences(UserInfoKey, 0);
        SharedPreferences.Editor editor = settings.edit();


        if (user != null) {
            UserPreference userPreference = new UserPreference(user);
            Gson gson = new Gson();
            try {
                String test = gson.toJson(userPreference);
                editor.putString("userfap", test);
            } catch (Exception ex) {
                Log.e("UtilsUser", ex.getMessage());
            }

        } else {
            editor.putString("userfap", "");
        }
        editor.commit();
    }

    public static void saveLastUserId(Context context, long userId) {
        SharedPreferences settings = context.getSharedPreferences(UserInfoKey, 0);
        SharedPreferences.Editor editor = settings.edit();

        try {
            editor.putLong(LastUserIdKey, userId);
        } catch (Exception ex) {
            Log.e("UtilsUser", ex.getMessage());
        }

        editor.commit();
    }

    public static List<OpenCampagne> getOpenCampagneList(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(UserInfoKey, 0);
        return new Gson().fromJson(settings.getString(key, null), type);
    }

    private static final Type type = new TypeToken<List<OpenCampagne>>() {
    }.getType();


    public static void setOpenCampagneList(Context context, String key, List<OpenCampagne> openCampagnes) {
        SharedPreferences settings = context.getSharedPreferences(UserInfoKey, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, new Gson().toJson(openCampagnes, type));
        editor.commit();
    }


    private static User getSavedUserFromPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(UserInfoKey, 0);
        String userAta = settings.getString("userfap", "");

        if (!GenericTools.isNullOrEmpty(userAta)) {
            Gson gson = new Gson();
            UserPreference userPreference = gson.fromJson(userAta, UserPreference.class);
            user = userPreference.getUserFromPreference();
            return user;
        } else {
            return null;
        }
    }


    private static long getLastUserFromPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(UserInfoKey, 0);
        return settings.getLong(LastUserIdKey, 0);
    }

    public static void resetUser() {
        user = null;
    }

    public static void saveCampagneUpdates(Context context, Boolean b) {
        SharedPreferences settings = context.getSharedPreferences("CampagneInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Constant.KEY_UPDATED, b);
        editor.commit();

    }

    public static Boolean isCampagneUpdated(Context context) {
        SharedPreferences settings = context.getSharedPreferences("CampagneInfo", 0);
        return settings.getBoolean(Constant.KEY_UPDATED, false);
    }

    public static void saveCampagneUpdates(Context context, String s) {
        SharedPreferences settings = context.getSharedPreferences("CampagneInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constant.KEY_CAMPAGNE_UPDATE, s);
        editor.commit();

    }

    public static String getCampagneUpdates(Context context) {
        SharedPreferences settings = context.getSharedPreferences("CampagneInfo", 0);
        return settings.getString(Constant.KEY_CAMPAGNE_UPDATE, "");
    }

    public static void savePanelLastTime(Context context, Long l) {
        SharedPreferences settings = context.getSharedPreferences("CampagneInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(Constant.KEY_PANEL_TIME, l);
        editor.commit();

    }

    public static long getPanelLastTime(Context context) {
        SharedPreferences settings = context.getSharedPreferences("CampagneInfo", 0);
        return settings.getLong(Constant.KEY_PANEL_TIME, 0);
    }

    public static void savePanelTimeStamp(Context context, Long l) {
        SharedPreferences settings = context.getSharedPreferences("CampagneInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(Constant.KEY_PANEL_LAST_TIMESTAMP, l);
        editor.commit();

    }

    public static long getPanelTimeStamp(Context context) {
        SharedPreferences settings = context.getSharedPreferences("CampagneInfo", 0);
        return settings.getLong(Constant.KEY_PANEL_LAST_TIMESTAMP, 0);
    }
}
