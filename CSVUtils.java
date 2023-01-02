import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class CSVUtils {

    public File generateCSVFromDB (ArrayList<DBLocation> dbLocations, Context context)
            throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH_mm_ss", Locale.getDefault()).format(new Date());
        String Filename = "Location_" + date +"_"+ time;
        File csvFile = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.MediaColumns.DISPLAY_NAME, Filename);
            cv.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
            cv.put(MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + "/Customer/Location/");
            Uri uri1 = context.getContentResolver()
                    .insert(MediaStore.Files.getContentUri("external"), cv);
            Timber.e(">FileUri >>> " + uri1);
            OutputStream outputStream1 = context.getContentResolver().openOutputStream(uri1);
            StringBuilder sb1 = getCsvFormate(dbLocations);
            outputStream1.write(sb1.toString().getBytes());
            outputStream1.close();
            Log.d("TAG", "File created successfully");
            csvFile = FilePathUtils.getFile(context, uri1);
        } else {
            csvFile = FilePathUtils
                    .initCreateFolder(context, "Customer/Location", Filename + ".csv");

            if (csvFile != null) {
                FileOutputStream fOut = new FileOutputStream(csvFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                StringBuilder sb1 = getCsvFormate(dbLocations);
                myOutWriter.append(sb1);
                myOutWriter.close();
                fOut.flush();
                fOut.close();
            }
        }
        //        File csvFile = FilePathUtils
        //                .initCreateFolder(context, "Customer/Location", Filename+".csv");


        return csvFile;
    }

    @NonNull private StringBuilder getCsvFormate (ArrayList<DBLocation> dbLocations) {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("longitude");
        sb1.append(",");
        sb1.append("latitude");
        sb1.append(",");
        sb1.append("isGps");
        sb1.append(",");
        sb1.append("timeInIst");
        sb1.append(",");
        sb1.append("hasNetwork");
        sb1.append(",");
        sb1.append("uploadedThroughStoreApi");
        sb1.append(",");
        sb1.append("customerId");
        sb1.append("\r\n");
        for (DBLocation dbLocation : dbLocations) {
            sb1.append(dbLocation.getLatitude());
            sb1.append(",");
            sb1.append(dbLocation.getLongitude());
            sb1.append(",");
            sb1.append(dbLocation.isGeo_location_switch());
            sb1.append(",");
            sb1.append(dbLocation.getTimeInIST());
            sb1.append(",");
            sb1.append(dbLocation.isHasNetwork());
            sb1.append(",");
            sb1.append(dbLocation.isUploadedThroughStoreApi());
            sb1.append(",");
            sb1.append(dbLocation.getCustomerId());
            sb1.append("\r\n");
        }
        return sb1;
    }

    public void generateCSVFile (String longitude, String latitude, String isGps,
            String current_time, String customerId, Context context) throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HHmm", Locale.getDefault()).format(new Date());
        String Filename = "Location_coordi" /*+ date + time*/;
        Uri uri = checkFileAlreadyExist(context);
        if (uri != null) {
            // TODO(): update CSV
            StringBuilder sb = new StringBuilder();
            sb.append(longitude);
            sb.append(",");
            sb.append(latitude);
            sb.append(",");
            sb.append(isGps);
            sb.append(",");
            sb.append(current_time);
            sb.append(",");
            sb.append(customerId);
            sb.append("\r\n");
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            outputStream.write(bytes);
            outputStream.write(sb.toString().getBytes());
            outputStream.close();
            Log.d("TAG", "File updated successfully");

        } else {
            // TODO(): create new CSV
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.MediaColumns.DISPLAY_NAME, Filename);
            cv.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
            cv.put(MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + "/folder_name/");
            Uri uri1 = context.getContentResolver()
                    .insert(MediaStore.Files.getContentUri("external"), cv);
            Timber.e(">FileUri >>> " + uri);
            OutputStream outputStream1 = context.getContentResolver().openOutputStream(uri1);
            StringBuilder sb1 = new StringBuilder();
            sb1.append("longitude");
            sb1.append(",");
            sb1.append("latitude");
            sb1.append(",");
            sb1.append("isGps");
            sb1.append(",");
            sb1.append("time");
            sb1.append(",");
            sb1.append("customerId");
            sb1.append("\r\n");
            sb1.append(longitude);
            sb1.append(",");
            sb1.append(latitude);
            sb1.append(",");
            sb1.append(isGps);
            sb1.append(",");
            sb1.append(current_time);
            sb1.append(",");
            sb1.append(customerId);
            sb1.append("\r\n");
            outputStream1.write(sb1.toString().getBytes());
            outputStream1.close();
            Log.d("TAG", "File created successfully");
        }
    }

   /* public boolean checkFileAlreadyExist (Context context , Uri fileUri) {
        boolean res;
        ContentResolver cr = context.getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cur = cr.query(Uri.parse(fileUri.toString()), projection, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                String filePath = cur.getString(0);
                // true= if it exists
                // false= File was not found
                res = new File(filePath).exists();
            } else {
                // Uri was ok but no entry found.
                res = false;
            }
            cur.close();
        } else {
            // content Uri was invalid or some other error occurred
            res = false;
        }

        return res;
    }*/

    public Uri checkFileAlreadyExist (Context context) {
        Uri uri = null;
        try {
            Uri contentUri = MediaStore.Files.getContentUri("external");

            String selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?";

            String[] selectionArgs = new String[]{Environment.DIRECTORY_DOWNLOADS +
                    "/folder_name/"};    //must include "/" in front and end

            Cursor cursor = context.getContentResolver()
                    .query(contentUri, null, selection, selectionArgs, null);


            if (cursor.getCount() == 0) {
                return uri;
                //            Toast.makeText(view.getContext(), "No file found in \"" + Environment.DIRECTORY_DOCUMENTS + "/folder_name/\"", Toast.LENGTH_LONG).show();
            } else {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String fileName = cursor.getString(
                            cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

                    if (fileName.contains("Location_coordi")) {
                        @SuppressLint("Range") long id =
                                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

                        uri = ContentUris.withAppendedId(contentUri, id);

                        break;
                    }
                }
                cursor.close();
                if (uri == null) {

                    //                Toast.makeText(view.getContext(), "\"menuCategory.txt\" not found", Toast.LENGTH_SHORT).show();
                    return uri;
                } else {
                    return uri;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return uri;
        }
    }

}
