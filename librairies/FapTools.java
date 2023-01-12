package com.app.fap.librairies;

import static com.app.fap.librairies.Constant.KEY_APP_UPDATE_LATER;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.app.fap.R;
import com.app.fap.component.GPSTracker;
import com.app.fap.models.IPBXInfo;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/****************************************************
 * Created by Tahiana-MadiApps on 28/07/2016.
 ****************************************************/
public class FapTools {

    /**
     * Time Duration for refreshing map 20sec
     */
    public static int refreshMapCycleValue = 10;
    public static GPSTracker gpsTracker;

    public static void showSimpleMessage(Activity activity, String title, String message) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.popup_simple_information, null);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);

        }

        TextView txtMessage = (TextView) convertView.findViewById(R.id.textViewMessage);

        Button buttonClose = (Button) convertView.findViewById(R.id.buttonClose);

        if (!TextUtils.isEmpty(message)) {
            txtMessage.setText(message);
            txtMessage.setVisibility(View.VISIBLE);
        }

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        dialog.getWindow().setAttributes(layoutParams);

        View decorView = dialog.getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        if (activity.isFinishing()) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        dialog.setCancelable(true);
        dialog.setContentView(convertView);
        try {
            if (activity != null && dialog != null && !dialog.isShowing()) {
                dialog.show();
            }

        } catch (Exception ex) {

        }
    }

    public static void showSimpleMessageAndFinish(final Activity activity, String title, String message) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.popup_simple_information, null);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);

        }

        TextView txtMessage = (TextView) convertView.findViewById(R.id.textViewMessage);

        Button buttonClose = (Button) convertView.findViewById(R.id.buttonClose);

        if (!TextUtils.isEmpty(message)) {
            txtMessage.setText(message);
            txtMessage.setVisibility(View.VISIBLE);
        }

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        dialog.getWindow().setAttributes(layoutParams);

        View decorView = dialog.getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                activity.finish();
            }
        });


        if (activity.isFinishing()) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        dialog.setCancelable(true);
        dialog.setContentView(convertView);

        try {
            if (activity != null && dialog != null && !dialog.isShowing()) {
                dialog.show();
            }

        } catch (Exception ex) {

        }
    }

    public static void showSimpleMessage(Activity activity, boolean isSuccess, String message) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.popup_error_custom, null);

        if (isSuccess) {
            ImageView imageViewResultState = (ImageView) convertView.findViewById(R.id.imageViewResultState);
            imageViewResultState.setImageDrawable(activity.getResources().getDrawable(R.drawable.ico_success));
        }

        TextView txtMessage = (TextView) convertView.findViewById(R.id.textViewMessage);

        Button buttonClose = (Button) convertView.findViewById(R.id.buttonClose);

        if (!TextUtils.isEmpty(message)) {
            txtMessage.setText(message);
            txtMessage.setVisibility(View.VISIBLE);
        }

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        dialog.getWindow().setAttributes(layoutParams);

        View decorView = dialog.getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        if (activity.isFinishing()) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        dialog.setCancelable(true);
        dialog.setContentView(convertView);
        try {
            if (activity != null && dialog != null && !dialog.isShowing()) {
                dialog.show();
            }

        } catch (Exception ex) {

        }


    }


    public static Dialog createSimpleLoaderDialog(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.popup_loader, null);

        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarGeneric);

        GenericTools.changeProgressBarColorTo(progressBar, activity.getResources().getColor(R.color.blueBase));

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(convertView);

        return dialog;
    }

    public static boolean isFirstRun(Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getBoolean(Constant.KEY_FIRST_RUN, true);
    }

    public static void setIsFirstRun(Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Constant.KEY_FIRST_RUN, false);
        editor.commit();
    }

    public static boolean isProchainPanneau(Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getBoolean(Constant.IS_PROCHAIN_PANNEAU, false);
    }

    public static void setIsProchainPanneau(Context context, boolean isProchainPanneau) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Constant.IS_PROCHAIN_PANNEAU, isProchainPanneau);
        editor.commit();
    }

    public static void saveIPBXInfo(Context context, IPBXInfo ipbxInfo) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        editor.putString(Constant.KEY_IPBX_INFO, gson.toJson(ipbxInfo));
        editor.commit();
    }

    public static void saveLoginAndPasswordToPreferences(Context context, String login, String password) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constant.KEY_LOGIN, login);
        editor.putString(Constant.KEY_PASSWORD, password);
        editor.commit();
    }

    public static void savePasswordToPreferences(Context context, String password) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constant.KEY_PASSWORD, password);
        editor.commit();
    }

    public static IPBXInfo getipIpbxInfo(Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        Gson gson = new Gson();
        return gson.fromJson(settings.getString(Constant.KEY_IPBX_INFO, ""), IPBXInfo.class);
    }

    public static String getLastPassword(Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getString(Constant.KEY_PASSWORD, "");
    }

    public static String getLastLogin(Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getString(Constant.KEY_LOGIN, "");
    }

    public static String getLastSyncDate(Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        Log.e("== Last Sync Get Time",""+settings.getString(Constant.KEY_LAST_DATE_SYNC, ""));
        return settings.getString(Constant.KEY_LAST_DATE_SYNC, "");
    }

    public static void saveLastSyncDate(Context context, String lastSyncDate) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constant.KEY_LAST_DATE_SYNC, lastSyncDate);
        Log.e("== Last Sycn Set Time", lastSyncDate);
        editor.commit();
    }

    public static Dialog showConfirmMessage(Activity activity, String title, String message) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.popup_simple_confirmation, null);
        if (!GenericTools.isNullOrEmpty(title)) {
            TextView textViewTitleConfirm = (TextView) convertView.findViewById(R.id.textViewTitle);
            textViewTitleConfirm.setText(title);
        }

        if (!GenericTools.isNullOrEmpty(message)) {
            TextView textViewMessage = (TextView) convertView.findViewById(R.id.textViewMessage);
            textViewMessage.setText(message);
        }

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(convertView);
        return dialog;
    }

    public static Dialog showConfirmMessageWithTwoButtons(Activity activity, String title, String message) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.popup_confirmation, null);
        if (!GenericTools.isNullOrEmpty(title)) {
            TextView textViewTitleConfirm = (TextView) convertView.findViewById(R.id.textViewTitle);
            textViewTitleConfirm.setText(title);
        }

        if (!GenericTools.isNullOrEmpty(message)) {
            TextView textViewMessage = (TextView) convertView.findViewById(R.id.textViewMessage);
            textViewMessage.setText(message);
        }

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(convertView);
        return dialog;
    }

    public static boolean saveBitmapToLocalDirectory(Bitmap bitmap, String filePath) {

        OutputStream outStream = null;
        boolean state = false;
        if (TextUtils.isEmpty(filePath))
            return false;
        try {
            File file = new File(filePath);
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, outStream);
            outStream.flush();
            outStream.close();
            state = true;
        } catch (Exception e) {
            return state;
        } finally {
            return state;
        }


    }

    public static String getUniqueID(Context context) {
        String myAndroidDeviceId = "";

        try {

            TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (mTelephony.getDeviceId() != null) {
                myAndroidDeviceId = mTelephony.getDeviceId();
            }
        } catch (Exception ex) {
            Log.e(context.getResources().getString(R.string.app_name), ex.getMessage());
        } finally {

            if (myAndroidDeviceId.isEmpty()) {
                myAndroidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }

        return myAndroidDeviceId;
    }

    public static int timeFormatToMinutes(int durationSeconds) {

        return Integer.valueOf(durationSeconds / 60);
    }

    public static int distanceFormatToKilometers(int distanceMeters) {

        return Integer.valueOf(distanceMeters / 1000);
    }

    public static String distanceFormat(int distanceMeters) {

        if (distanceMeters >= 1000) {
            return (distanceMeters / 1000) + " km";
        } else {
            return distanceMeters + " m";
        }
    }

    public static String timeFormat(int durationSeconds) {

        String format = "";

        int hours = durationSeconds / 3600;
        int remainder = durationSeconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        if (hours > 0) {
            format += hours + " h";
        }

        if (mins > 0) {
            if (format.length() > 0) format += " ";
            format += mins + " m";
        }

        if (secs > 0) {
            if (format.length() > 0) format += " ";
            format += secs + " s";
        }

        return format;
    }

    public static void showCustomSnackBar(Context context, View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.blueBase));
        snackbar.show();
    }

    public static int getPanelsSynchronized(String key, Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        return settings.getInt(key, 0);
    }
    public static void saveUpdateLater(boolean b, Context context) {
        SharedPreferences settings = context.getSharedPreferences("app_update", 0);
        settings.edit().putBoolean(KEY_APP_UPDATE_LATER, b).apply();
    }
    public static boolean isUpdateLater(Context context) {
        SharedPreferences settings = context.getSharedPreferences("app_update", 0);
        return settings.getBoolean(KEY_APP_UPDATE_LATER, false);
    }
    public static void setIntValueInSharedPreference(String key, int value, Context context) {

        SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();

    }
//    public static void setLongValueInSharedPreference(String key,Long value,Context context){
//
//        SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putLong(key, value);
//        editor.commit();
//
//    }
//    public static Long getLOngValue(String key,Context context){
//
//        SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(context);
//
//        return sharedPreferences.getLong(key, 0);
//
//    }

    public static int roundUp(double double_value) {
        int roundDown = (int) double_value;
        if (double_value > roundDown) {
            return roundDown + 1;
        } else {
            return roundDown;
        }

    }

    public static AlertDialog createAlertDialog(Activity activity, DialogOkClickListener listener, String title, String text, Bundle extras) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(text).setCancelable(false).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null)
                    listener.onOkClicked(extras);
                else
                    dialog.dismiss();
            }
        });
        return builder.create();
    }
    public static AlertDialog createAlertDialog(Activity activity, DialogOkClickListener listener, String title,
                                                String text, String positiveText,  String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(text).setCancelable(false).setPositiveButton(positiveText, (dialog, id) -> {
            if (listener != null)
                listener.onOkClicked(null);
            else
                dialog.dismiss();
        });
        builder.setNegativeButton(negativeText, (dialogInterface, i) -> dialogInterface.dismiss());
        return builder.create();
    }



    public interface DialogOkClickListener {
        void onOkClicked(Bundle extras);
    }

}
