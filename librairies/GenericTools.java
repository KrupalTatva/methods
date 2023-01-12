package com.app.fap.librairies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.app.fap.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/****************************************************
 * Created by Tahiana-MadiApps on 28/07/2016.
 ****************************************************/
public class GenericTools {

    public static void hideStatusBar(Activity activty) {
        if (activty != null && activty.isFinishing()) {
            View decorView = activty.getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);

            activty.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }

    public static void setActionBarTitle(AppCompatActivity activity, String title, boolean isWithAction) {
      /*  ActionBar actionBar = activity.getSupportActionBar();
        LayoutInflater inflater = LayoutInflater.from(activity);

        View v = inflater.inflate(R.layout.action_bar_centered_title, null);
        if(!isWithAction)
        {
            v = inflater.inflate(R.layout.action_bar_centered_title_without_action, null);
        }


        TextView titleTextView = (TextView) v.findViewById(R.id.txtTitle);
        titleTextView.setText(title);
        actionBar.setCustomView(v);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/
    }

    public static void setActionBarTitleWithHomeIcon(AppCompatActivity activity, String title) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public static void navigateTo(Activity senderActivity, Class destination) {
        Intent intent = new Intent(senderActivity, destination);
        senderActivity.startActivity(intent);
    }

    /*
    Permet de générer les marges d'un view group
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /*
       Redimensionne une listeView
    */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 10;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    /*
      Redimensionne une expandListView
   */
    public static void setExpandListViewHeightBasedOnChildren(ExpandableListView listView, int complementaryHeight) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            //totalHeight += listItem.getHeight();
            totalHeight += complementaryHeight;

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        totalHeight += (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }


    /*
      Vérifie si le provider de localisation est activié et fonctionnel
   */
    public static boolean checkLocationProvider(final Context context) {
        LocationManager lm = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        boolean result = false;
/*
        if(lm==null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try
        {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex){}
        try
        {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex)
        {}

        if(!gps_enabled && !network_enabled)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Géolocalisation");
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {


                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            *//*dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });*//*
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
            dialog.show();

        }
        else*/
        result = true;
        return result;

    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int height, int width) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNullOrEmpty(EditText textBox) {
        if (textBox.getText() != null && textBox.getText().length() > 0) {
            return false;
        } else {
            return true;
        }

    }

    public static boolean isNullOrEmpty(TextView textBox) {
        if (textBox.getText() != null && textBox.getText().length() > 0) {
            return false;
        } else {
            return true;
        }

    }

    public static final String stringToMD5(final String s) {
        final String MD5 = "MD5";
        try {

            String result = s;
            if (s != null) {
                MessageDigest md = MessageDigest.getInstance("MD5"); // or "SHA-1"
                md.update(s.getBytes("UTF-8"));
                BigInteger hash = new BigInteger(1, md.digest());
                result = hash.toString(16);
                while (result.length() < 32) { // 40 for SHA-1
                    result = "0" + result;
                }
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<Address> getAddressFromLocation(Activity activity, double latitude, double longitude) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(activity, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);// Here 1 represent max location result to returned, by documents it recommended 1 to 5

            return addresses;
        } catch (IOException ex) {
            return null;
        }


       /* String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();*/
    }


    public static void changeProgressBarColorTo(ProgressBar progressBar, int color) {
        progressBar.getIndeterminateDrawable().setColorFilter(color,
                PorterDuff.Mode.SRC_IN);
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidCellPhone(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(number).matches();
        }

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int[] splitToComponentTimes(BigDecimal seconds) {
        long longVal = seconds.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours, mins, secs};
        return ints;
    }

    public static float convertDpToPixel(float dp, Activity context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static <T> T[] concatenateArray(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }


    /**
     * get uri to drawable or any other resource type if u wish
     *
     * @param context    - context
     * @param drawableId - drawable res id
     * @return - uri
     */
    public static final Uri getUriToDrawable(@NonNull Context context, @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    public static boolean isDeviceOnline(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager.getActiveNetworkInfo() == null)
            return false;
        else
            return connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String convertStringToMD5(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")), 0, s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Get the free disk available space in boolean to download requested file
     *
     * @return boolean value according to size availability
     */

    public static boolean isMemorySizeAvailableAndroid(long download_bytes, boolean isExternalMemory) {
        boolean isMemoryAvailable = false;
        long freeSpace = 0;

        // if isExternalMemory get true to calculate external SD card available size
        if (isExternalMemory) {
            try {
                StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                freeSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
                if (freeSpace > download_bytes) {
                    isMemoryAvailable = true;
                } else {
                    isMemoryAvailable = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isMemoryAvailable = false;
            }
        } else {
            // find phone available size
            try {
                StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
                freeSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
                if (freeSpace > download_bytes) {
                    isMemoryAvailable = true;
                } else {
                    isMemoryAvailable = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isMemoryAvailable = false;
            }
        }

        return isMemoryAvailable;
    }


    /**
     * @return Number of bytes available on External storage
     */
    public static long getAvailableSpaceInBytes() {
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

        return availableSpace;
    }


    /**
     * @return Number of kilo bytes available on External storage
     */
    public static long getAvailableSpaceInKB() {
        final long SIZE_KB = 1024L;
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        return availableSpace / SIZE_KB;
    }

    /**
     * @return Number of Mega bytes available on External storage
     */
    public static long getAvailableSpaceInMB() {
        final long SIZE_KB = 1024L;
        final long SIZE_MB = SIZE_KB * SIZE_KB;
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        return availableSpace / SIZE_MB;
    }

    /**
     * @return Number of gega bytes available on External storage
     */
    public static long getAvailableSpaceInGB() {
        final long SIZE_KB = 1024L;
        final long SIZE_GB = SIZE_KB * SIZE_KB * SIZE_KB;
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        return availableSpace / SIZE_GB;
    }

    public static Bitmap getVidioThumbnail(String path) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
            if (bitmap != null) {
                return bitmap;
            }
        }
        // MediaMetadataRetriever is available on API Level 8 but is hidden until API Level 10
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();
            final Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, path);
            // The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                bitmap = (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                final byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
                if (bitmap == null) {
                    bitmap = (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
                }
            }
        } catch (Exception e) {
            bitmap = null;
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (final Exception ignored) {
            }
        }
        return bitmap;
    }

    public static void enableOrDesableEditText(EditText editText, boolean state) {
        editText.setEnabled(state);
    }

    public static void enableOrDesableSpinner(Spinner spinner, boolean state) {
        spinner.setEnabled(state);
    }

    public static List<Address> locationToLocalityAddress(Location location, Activity activity) {
        List<Address> addresses = null;
        try {

            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            return addresses;
        }

    }

    public static void browseWebsite(String website, Context context) {
        if (!website.contains("http://") || !website.contains("https://")) {
            website = "http://" + website;
        }
        try {

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(website));
            context.startActivity(i);
        } catch (Exception ex) {
            Log.e("BROWSER", ex.getMessage());
        }

    }

    public static boolean isEditTextFilled(Context context, EditText editText) {
        if (GenericTools.isNullOrEmpty(editText)) {
            editText.setError(context.getString(R.string.veuillez_remplir_ce_champ));
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isNotEmptyAndValidEmail(Activity activity, EditText editText) {
        if (!GenericTools.isEditTextFilled(activity, editText)) {
            return false;
        }
        if (!GenericTools.isValidEmail(editText.getText().toString())) {
            editText.setError(activity.getString(R.string.format_email_invalid));
            return false;
        }
        return true;
    }

    public static boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public static Bitmap rotateImage(Bitmap img, String pathFile) {
        int orientation;

        try {
            ExifInterface exif = new ExifInterface(pathFile);

            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Log.e("ExifInteface .........", "rotation =" + orientation);

            //exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

            Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
                //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
                Log.e("in orientation", "" + orientation);
                img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), m, true);
                return img;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                Log.e("in orientation", "" + orientation);
                img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), m, true);
                return img;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                Log.e("in orientation", "" + orientation);
                img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), m, true);
                return img;
            }
            return img;
        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
            return null;
        }

    }

    public static boolean deletePhysicalFile(String pathFile) {

        boolean state = false;

        try {

            File file = new File(pathFile);

            if (file.exists()) {

                state = file.delete();
            }

        } catch (OutOfMemoryError outOfMemoryError) {

            return state;
        } finally {
            return state;
        }

    }

    public static void displayItinary(LatLng depart, LatLng dest, Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + depart.latitude + "," + depart.longitude + "&daddr=" + dest.latitude + "," + dest.longitude));
            i.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");
            context.startActivity(i);
        } catch (ActivityNotFoundException exception) {
            throw exception;
            //showSimpleMessage(context,"Google Map n'est pas encore installé sur votre téléphone");*/
        }
    }

    public static Bitmap drawTextToBitmap(Context ctx, int drawable, int count, String color) {

        Resources r = ctx.getResources();
        Bitmap res = BitmapFactory.decodeResource(r, drawable);
        res = res.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(res);
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/RobotoMedium.ttf");
        textPaint.setTypeface(typeface);
        textPaint.setColor(Color.parseColor(color));
        float density = ctx.getResources().getDisplayMetrics().density;

        if (count >= 100) {
            textPaint.setTextSize(10 * density);
        } else if (count >= 1000) {
            textPaint.setTextSize(8 * density);
        } else {
            textPaint.setTextSize(12 * density);
        }

        String markerSize = "x " + String.valueOf(count);
        c.drawText(markerSize, res.getWidth() / 2, res.getHeight() / 2.5f + textPaint.getTextSize() / 3, textPaint);

        return res;
    }

    public static boolean isValidCoordinate(double lat, double lng) {
        if ((-90.f <= lat && lat <= 90.f) && (-180.f <= lng && lng <= 180.f)) {
            return true;
        }
        return false;

    }

    public static int convertStringToIntIfExist(String[] values, int index) {
        int result = -1;

        if (values != null && values.length >= index + 1)
            result = Integer.parseInt(values[index]);
        return result;
    }

    public static void showToast(Activity activity, String toastMessage) {
        Toast.makeText(activity.getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
    }

    public static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

}
