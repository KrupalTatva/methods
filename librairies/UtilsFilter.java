package com.app.fap.librairies;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;



public class UtilsFilter
{
    private static String KEY_FILTER_CAMPAGNE_FILTER = "KEY_FILTER_CAMPAGNE_FILTER";
    private static String KEY_FILTER_PANEL_FILTER = "KEY_FILTER_PANEL_FILTER";
    private static String KEY_FILTER_DEPATEMENT_FILTER = "KEY_FILTER_DEPATEMENT_FILTER";
    private static String KEY_FILTER_REGION_FILTER = "KEY_FILTER_REGION_FILTER";
    private static String KEY_FILTER_CANTON_FILTER = "KEY_FILTER_CANTON_FILTER";
    private static String KEY_FILTER_COMMUNE_FILTER = "KEY_FILTER_COMMUNE_FILTER";
    private static String KEY_FILTER_ARRONDISSEMENT_FILTER = "KEY_FILTER_ARRONDISSEMENT_FILTER";
    private static String KEY_TIME_OUT = "KEY_TIME_OUT";
    private static String KEY_TWO_HOURS_OUT = "KEY_TWO_TIME_OUT";
    private static String TOTAL_TWO_HOURS = "KEY_TOTAL_TWO_TIME_OUT";
    private static String VERSION_CODE = "VERSION_CODE";



    public static void saveCampagneFilterToPreferences(Context context, Long campagneId)
{
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    SharedPreferences.Editor editor = settings.edit();

    if(campagneId >= 0)
    {
        editor.putLong(KEY_FILTER_CAMPAGNE_FILTER,campagneId );

    }
    else
    {
        editor.putLong(KEY_FILTER_CAMPAGNE_FILTER,-1);
    }
    editor.commit();
}

    public static Long getCampagneFilterFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getLong(KEY_FILTER_CAMPAGNE_FILTER, -1);
    }

    public static long getTimeOutFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getLong(KEY_TIME_OUT, 0);
    }

    public static void savePanelStateFilterToPreferences(Context context, int panleStateId)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        if(panleStateId >= 0)
        {
            editor.putInt(KEY_FILTER_PANEL_FILTER,panleStateId );

        }
        else
        {
            editor.putInt(KEY_FILTER_PANEL_FILTER,Constant.PANEL_ALL_STATES);
        }
        editor.commit();
    }

    public static void saveTimeOutToPreferences(Context context, long timeOut)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        if(timeOut >= 0)
        {
            editor.putLong(KEY_TIME_OUT,timeOut);

        }
        else
        {
            editor.putInt(KEY_FILTER_PANEL_FILTER,0);
        }
        editor.commit();
    }

    public static int getPanelStateFilterFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getInt(KEY_FILTER_PANEL_FILTER, -1);
    }

    public static void saveDepartementFilterToPreferences(Context context, String departement)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        if(!GenericTools.isNullOrEmpty(departement))
        {
            editor.putString(KEY_FILTER_DEPATEMENT_FILTER,departement );

        }
        else
        {
            editor.putString(KEY_FILTER_DEPATEMENT_FILTER,"");
        }
        editor.commit();
    }

    public static String getDepartementFilterFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getString(KEY_FILTER_DEPATEMENT_FILTER, null);
    }

    public static void saveRegionFilterToPreferences(Context context, String region)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        if(!GenericTools.isNullOrEmpty(region))
        {
            editor.putString(KEY_FILTER_REGION_FILTER,region );

        }
        else
        {
            editor.putString(KEY_FILTER_REGION_FILTER,"");
        }
        editor.commit();
    }

    public static String getRegionFilterFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getString(KEY_FILTER_REGION_FILTER, null);
    }

    public static void saveCantonFilterToPreferences(Context context, String canton)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        if(!GenericTools.isNullOrEmpty(canton))
        {
            editor.putString(KEY_FILTER_CANTON_FILTER,canton );

        }
        else
        {
            editor.putString(KEY_FILTER_CANTON_FILTER,"");
        }
        editor.commit();
    }

    public static String getCantonFilterFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getString(KEY_FILTER_CANTON_FILTER, null);
    }

    public static void saveCommuneFilterToPreferences(Context context, String commune)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        if(!GenericTools.isNullOrEmpty(commune))
        {
            editor.putString(KEY_FILTER_COMMUNE_FILTER,commune );

        }
        else
        {
            editor.putString(KEY_FILTER_COMMUNE_FILTER,"");
        }
        editor.commit();
    }

    public static String getCommuneFilterFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getString(KEY_FILTER_COMMUNE_FILTER, null);
    }

    public static void saveArrondissementFilterToPreferences(Context context, String arrondissement)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        if(!GenericTools.isNullOrEmpty(arrondissement))
        {
            editor.putString(KEY_FILTER_ARRONDISSEMENT_FILTER,arrondissement );

        }
        else
        {
            editor.putString(KEY_FILTER_ARRONDISSEMENT_FILTER,"");
        }
        editor.commit();
    }

    public static String getArrondissementFilterFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getString(KEY_FILTER_ARRONDISSEMENT_FILTER, null);
    }

    public static void saveTwoHourTimerToPreferences(Context context, Long time)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(KEY_TWO_HOURS_OUT,time);
        editor.commit();
    }

    public static Long getTwoHourTimerFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getLong(KEY_TWO_HOURS_OUT, 0);
    }

    public static void saveTotalTwoHourTimerToPreferences(Context context, Long time)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(TOTAL_TWO_HOURS,time);
        editor.commit();
    }

    public static Long getTotalTwoHourTimerFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getLong(TOTAL_TWO_HOURS, 0);
    }

    public static void saveVersionCodeToPreferences(Context context, int time)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(VERSION_CODE,time);
        editor.commit();
    }

    public static Long getVersionFromPreferences(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getLong(VERSION_CODE, 0);
    }

}
