

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static int selectedBattriesCount = 0;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String dd_MMM_yyyy = "dd MMM yyyy";

    public static String dateConversation(Context context, String dateTime, String inputFormat, String outputFormat) {
        String formattedTime = context.getString(R.string.not_available);
        if (TextUtils.isEmpty(inputFormat)) {
            return formattedTime;
        }
        if (TextUtils.isEmpty(outputFormat)) {
            return formattedTime;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat(outputFormat, Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }


    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static void showAlert(Context context, String title, String msg,
                                 DialogInterface.OnClickListener yesClick, DialogInterface.OnClickListener cancelClick) {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title);
        builder.setCancelable(false).setPositiveButton("Yes", yesClick)
                .setNegativeButton("Cancel", cancelClick);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isValidIFSCCode(String ifscCode) {
        Pattern pattern;
        Matcher matcher;
        final String IFSC_PATTERN = "^[A-Z]{4}\\d{7}$";
        pattern = Pattern.compile(IFSC_PATTERN);
        matcher = pattern.matcher(ifscCode);

        return matcher.matches();
    }

    public static boolean isValidPAN(String pan) {
        Pattern pattern;
        Matcher matcher;
        final String PAN_PATTERN = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$";
        pattern = Pattern.compile(PAN_PATTERN);
        matcher = pattern.matcher(pan);

        return matcher.matches();
    }

    public static boolean isValidVehicleRegistration(String reg_no) {
        Pattern pattern;
        Matcher matcher;
        //        final String VEHICLE_PATTERN = "^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$";
        final String VEHICLE_PATTERN =
                "(([A-Za-z]){2,3}(|-)(?:[0-9]){1,2}(|-)(?:[A-Za-z]){2}(|-)([0-9]){1,4})|(([A-Za-z]){2,3}(|-)([0-9]){1,4})";
        pattern = Pattern.compile(VEHICLE_PATTERN);
        matcher = pattern.matcher(reg_no);

        return matcher.matches();

    }

    public static boolean isValidMobileNumber(final String phoneNumber) {
        return (!TextUtils.isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches());
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    public static boolean checkPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat
                .checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat
                .checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkLocationPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat
                .checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public static void dismissKeyboard(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getDateAndTime(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String getDateTime(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String getCustomerDateAndTime(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output =
                new SimpleDateFormat("dd MMM yyyy | hh:mm a", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String getSwapDateAndTime(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String getDate(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String getDateWithoutDay(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String getTime(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String getMonthAndYear(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String convertDateFormat(Context context, String dateTime) {
        String formattedTime = context.getString(R.string.not_available);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(dateTime);
            formattedTime = output.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    //hide keyboard
    public static void hideKeyboard(Context ctx) {
        try {
            InputMethodManager inputManager =
                    (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

            // check if no view has focus:
            View v = ((Activity) ctx).getCurrentFocus();
            if (v == null) return;

            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isDatePassed(String monthYear) {
        Date enteredDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
            enteredDate = sdf.parse(monthYear);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        Date currentDate = new Date();
        return enteredDate == null || !enteredDate.after(currentDate);
    }

    public static boolean checkDates(String d1, String d2) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dfDate =
                new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            //If start date is after the end date
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else b = dfDate.parse(d1).equals(dfDate.parse(d2));//If two dates are equal
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static boolean checkGpsEnable(Context mContext) {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gps_enabled;
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        try {
            locationMode = Settings.Secure
                    .getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    public static String getReportStatus(Context context, String status) {
        switch (status) {
            case Constants.assigned:
                return context.getString(R.string.assigned);
            case Constants.WIP:
                return context.getString(R.string.work_in_progress);
            case Constants.completed:
                return context.getString(R.string.completed);
            case Constants.requested:
                return context.getString(R.string.requested);
            case Constants.batteries_picked:
                //                return context.getString(R.string.batteries_picked);
                return context.getString(R.string.charged_batteries_picked);
            case Constants.batteries_swapped:
                //                return context.getString(R.string.batteries_swapped);
                return context.getString(R.string.discharged_batteries_swapped);
            case Constants.batteries_inserted:
                return context.getString(R.string.batteries_inserted);
            case Constants.cancelled:
                return context.getString(R.string.cancelled);
        }
        return status;
    }

    public static float getZoomLevel(Context context, float distance) {
        if (distance > 0 && distance <= 10) {
            return 13f;
        } else if (distance > 10 && distance <= 30) {
            return 11.5f;
        }
        //        else if(distance > 20 && distance <= 30){
        //            return 11.5f;
        //        }
        else if (distance > 30 && distance <= 40) {
            return 10.5f;
        } else if (distance > 40 && distance <= 50) {
            return 10f;
        }

        return 20f;
    }

    @SuppressLint("HardwareIds")
    public static String getIMEIDeviceId(Context context) {

        /*String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure
                    .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                } else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure
                        .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d(">>>deviceId", deviceId);
        return deviceId;*/
        return Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static class StringDateComparator implements Comparator<CustomerData> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        public int compare(CustomerData lhs, CustomerData rhs) {
            try {
                return dateFormat.parse(rhs.getCreated_at())
                        .compareTo(dateFormat.parse(lhs.getCreated_at()));
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return -1;
        }
    }

    public static class BatteryTransactionDateComparator
            implements Comparator<BatteryTransactionData> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        public int compare(BatteryTransactionData lhs, BatteryTransactionData rhs) {
            try {
                return dateFormat.parse(rhs.getTime()).compareTo(dateFormat.parse(lhs.getTime()));
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return -1;
        }
    }
}
