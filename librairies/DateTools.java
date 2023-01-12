package com.app.fap.librairies;

/****************************************************
 * Created by Tahiana-MadiApps on 12/08/2016.
 ****************************************************/

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/****************************************************
 * Created on 14/06/2016.
 ****************************************************/
public class DateTools {
    static DateTools instance;
    private final DateFormat dateFormat;
    private final DateFormat timeFormat;

    private DateTools(Context context)
    {
        final String format = Settings.System.getString(context.getContentResolver(), Settings.System.DATE_FORMAT);
        if (TextUtils.isEmpty(format))
        {
            dateFormat = android.text.format.DateFormat.getMediumDateFormat(context.getApplicationContext());
        } else {

            dateFormat = new SimpleDateFormat(format);
        }
        timeFormat = android.text.format.DateFormat.getTimeFormat(context);
    }

    public static DateTools getInstance(Context context) {
        if (instance == null) {
            instance = new DateTools(context);
        }
        return instance;
    }

    /*public synchronized static String formatDateTime(Context context,long timestamp) {
        long milliseconds = timestamp * 1000;
        Date dateTime = new Date(milliseconds);
        String date = getInstance(context).dateFormat.format(dateTime);
        String time = getInstance(context).timeFormat.format(dateTime);
        return date + " " + time;
    }*/

    public synchronized static String getDate(Context context,Date dateTime)
    {
        SimpleDateFormat  formatSample = new SimpleDateFormat("dd-MM-yyyy");
        try
        {
            return formatSample.format(dateTime);
        }
        catch (Exception ex)
        {
            Log.e("DATETOOLS",ex.getMessage());
        }
        return "";
    }

    public synchronized  static String getHours(Context context,Date dateTime)
    {
        return  getInstance(context).timeFormat.format(dateTime);
    }
    public synchronized  static String get24Hours(Date dateTime)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return  dateFormat.format(dateTime);
    }

    public static Date geDateFromString(String strDate)
    {
        Date date = null;
        try
        {
            SimpleDateFormat  formatSample = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatSample.parse(strDate);
        }
        catch (Exception ex)
        {
            Log.e("DATETOOLS",ex.getMessage());
        }

        return date;
    }

    public static String getDateTodayToString(){
        return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static Date geDateFromStringOrder(String strDate)
    {
        Date date = null;
        try
        {
            SimpleDateFormat  formatSample = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            date = formatSample.parse(strDate);
        }
        catch (Exception ex)
        {
            Log.e("DATETOOLS",ex.getMessage());
        }

        return date;
    }

    public static String getNowDateString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getStringFormatedFromStringDate(String srtDate)
    {
        String strResultDate = "";
        try
        {
            Date date = DateTools.geDateFromStringOrder(srtDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strResultDate = sdf.format(date);

        }
        catch (Exception ex)
        {
            Log.e("DATETOOLS",ex.getMessage());
        }

        return strResultDate;
    }
}

