import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;




import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.Locale;

import okhttp3.ResponseBody;

public class FilePathUtils {
    public static final String DOCUMENTS_DIR = "documents";
    // configured android:authorities in AndroidManifest (https://developer.android.com/reference/android/support/v4/content/FileProvider)
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    public static final String HIDDEN_PREFIX = ".";
    /**
     * TAG for log messages.
     */
    static final String TAG = "FileUtils";
    private static final boolean DEBUG = false; // Set to true to enable logging
    /**
     * File and folder comparator. TODO Expose sorting option method
     */
    public static Comparator<File> sComparator = (f1, f2) -> {
        // Sort alphabetically by lower case, which is much cleaner
        return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
    };
    /**
     * File (not directories) filter.
     */
    public static FileFilter sFileFilter = file -> {
        final String fileName = file.getName();
        // Return files only (not directories) and skip hidden files
        return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
    };
    /**
     * Folder (directories) filter.
     */
    public static FileFilter sDirFilter = file -> {
        final String fileName = file.getName();
        // Return directories only and skip hidden directories
        return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
    };

    private FilePathUtils () {
    } //private constructor to enforce Singleton pattern

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    public static String getExtension (String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal (String url) {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://");
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    public static boolean isMediaUri (Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    public static Uri getUri (File file) {
        return (file != null) ? Uri.fromFile(file) : null;
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    public static File getPathWithoutFilename (File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname =
                        filepath.substring(0, filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    /**
     * @return The MIME type for the given file.
     */
    public static String getMimeType (File file) {

        String extension = getExtension(file.getName());

        if (extension.length() > 0) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
        }

        return "application/octet-stream";
    }

    /**
     * @return The MIME type for the give Uri.
     */
    public static String getMimeType (Context context, Uri uri) {
        File file = new File(getPath(context, uri));
        return getMimeType(file);
    }

    /**
     * @return The MIME type for the give String Uri.
     */
    public static String getMimeType (Context context, String url) {
        String type = context.getContentResolver().getType(Uri.parse(url));
        if (type == null) {
            type = "application/octet-stream";
        }
        return type;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is local.
     */
    public static boolean isLocalStorageDocument (Uri uri) {
        return AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument (Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument (Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument (Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri (Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn (Context context, Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Files.FileColumns.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG) {
                    DatabaseUtils.dumpCursor(cursor);
                }

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            //Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @see #isLocal(String)
     * @see #getFile(Context, Uri)
     */
    public static String getPath (final Context context, final Uri uri) {
        //        String absolutePath = getLocalPath(context, uri);
        //        return absolutePath != null ? absolutePath : uri.toString();
        String type = getMimeTypefromUri(context, uri);
        if (checkUrlAudioWithMimeType(type) || checkUrlIsImage(type) ||
                checkUrlIsVideoWithMimeType(type) || checkUrlIsVideoWithMimeTypeDF(type)) {
            String absolutePath = getLocalPath(context, uri);
            return absolutePath != null ? absolutePath : uri.toString();
        } else {
            String absolutePath = copyFileToInternalStorage(context, uri, "");
            return absolutePath != null ? absolutePath : uri.toString();
        }
    }

    public static File createDir (Context context, String folderName) {
        File folder = null;
//        if (folderName == ConstantKeys.FOLDER_CAPTURE) {
//            folder = new File(Environment.getExternalStorageDirectory().getPath(), folderName);
//        } else {
            folder = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .getPath(), folderName);
//        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
        /*try {
            File[] externalStorageVolumes = ContextCompat.getExternalFilesDirs(context, null);
            File primaryExternalStorage = externalStorageVolumes[0];
            String filepath = primaryExternalStorage.getPath();
            folder = new File(filepath, folderName);
        } catch (Exception e) {
            e.printStackTrace();
           *//* showAlert(context, false, context.getString(R.string.app_name),
                    context.getString(R.string.alert_error_while_download_file),
                    context.getString(R.string.ok), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }, null, null);*//*
        }
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        return folder;*/
    }

    public static File initCreateFolder (Context context, String folder,
            String fileNameWithExtension) {
        try {
            File newFolder = createDir(context, folder);
            return new File(newFolder.getAbsolutePath() + "/" + fileNameWithExtension);
        } catch (Exception e) {
            e.printStackTrace();
           /* MethodUtils.showAlert(context, false, context.getString(R.string.app_name),
                    context.getString(R.string.alert_error_while_download_file),
                    context.getString(R.string.ok), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }, null, null);*/
        }
        return null;
    }

    public static boolean checkUrlIsVideoWithMimeTypeDF (String url) {
        if (url == null) {
            return false;
        }
        return (url.toLowerCase().endsWith("mp4")) || (url.toLowerCase().endsWith("avi")) ||
                (url.toLowerCase().endsWith("mov")) || (url.toLowerCase().endsWith("mkv")) ||
                (url.toLowerCase().endsWith("video"));
    }


    public static boolean checkUrlIsImage (String url) {
        if (url == null) {
            return false;
        }
        return url.toLowerCase().endsWith("png") || url.toLowerCase().endsWith("jpg") ||
                url.toLowerCase().endsWith("jpeg");
    }

    public static boolean checkUrlIsVideoWithMimeType (String url) {
        if (url == null) {
            return false;
        }
        return (url.toLowerCase().endsWith("mp4")) || (url.toLowerCase().endsWith("avi")) ||
                (url.toLowerCase().endsWith("mov")) || (url.toLowerCase().endsWith("mkv")) ||
                (url.toLowerCase().endsWith("msvideo")) ||
                (url.toLowerCase().endsWith("x-troff-msvideo")) ||
                (url.toLowerCase().endsWith("x-msvideo")) ||
                (url.contains("video") && url.toLowerCase().endsWith("mpeg")) ||
                (url.contains("video") && url.toLowerCase().endsWith("x-mpeg"));
    }

    public static boolean checkUrlAudioWithMimeType (String url) {
        if (url == null) {
            return false;
        }
        return (url.toLowerCase().endsWith("m4a")) || (url.toLowerCase().endsWith("amr")) ||
                (url.toLowerCase().endsWith("wav")) || (url.toLowerCase().endsWith("x-wav")) ||
                (url.toLowerCase().endsWith("mp3")) ||
                (url.contains("audio") && url.toLowerCase().endsWith("mpeg"));
    }

    public static boolean checkUrlIsVideo (String url) {
        if (url == null) {
            return false;
        }
        return url.toLowerCase().endsWith("mp4") || url.toLowerCase().endsWith("avi") ||
                url.toLowerCase().endsWith("mov") || url.toLowerCase().endsWith("mkv") ||
                url.toLowerCase().endsWith("msvideo") ||
                url.toLowerCase().endsWith("x-troff-msvideo") ||
                url.toLowerCase().endsWith("x-msvideo") || url.toLowerCase().endsWith("mpeg") ||
                url.toLowerCase().endsWith("x-mpeg");
    }

    public static String getMimeTypefromUri (Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static String getFileExtensionFromUrl (String url) {
        if (!TextUtils.isEmpty(url)) {
            int filenamePos = url.lastIndexOf('/');
            String filename = 0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!filename.isEmpty() /*&&
                    Pattern.matches("[a-zA-Z_ \\[\\]\\(\\)\\'0-9\\.\\-\\(\\)\\%]+", filename)*/) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    String extension = filename.substring(dotPos + 1);

                    int fragment = extension.lastIndexOf('#');
                    if (fragment > 0) {
                        extension = extension.substring(0, fragment);
                    }

                    int query = extension.lastIndexOf('?');
                    if (query > 0) {
                        extension = extension.substring(0, query);
                    }

                    try {
                        if (extension.length() > 4) {
                            extension = MimeTypeMap.getFileExtensionFromUrl(
                                    Uri.fromFile(new File(Uri.parse(url).getPath())).toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return extension;
                    }
                    return extension;
                }
            }
        }
        return "";
    }


    private static String copyFileToInternalStorage (Context context, Uri uri, String newDirName) {

        Cursor returnCursor = context.getContentResolver()
                .query(uri, new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE}, null,
                        null, null);


        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));

        File output;
        if (!newDirName.equals("")) {
            File dir = new File(context.getFilesDir() + "/" + newDirName);
            if (!dir.exists()) {
                dir.mkdir();
            }
            output = new File(context.getFilesDir() + "/" + newDirName + "/" + name);
        } else {
            output = new File(context.getFilesDir() + "/" + name);
        }
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(output);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }

        return output.getPath();
    }

    private static String getLocalPath (final Context context, final Uri uri) {

        if (DEBUG) {
            Log.d(TAG + " File -",
                    "Authority: " + uri.getAuthority() + ", Fragment: " + uri.getFragment() +
                            ", Port: " + uri.getPort() + ", Query: " + uri.getQuery() +
                            ", Scheme: " + uri.getScheme() + ", Host: " + uri.getHost() +
                            ", Segments: " + uri.getPathSegments().toString());
        }

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri);
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    String filePath = null;
                    if (Build.VERSION.SDK_INT > 20) {
                        //getExternalMediaDirs() added in API 21
                        File extenal[] = context.getExternalMediaDirs();
                        if (extenal.length > 1) {
                            filePath = extenal[1].getAbsolutePath();
                            filePath =
                                    filePath.substring(0, filePath.indexOf("Android")) + split[1];
                        }
                    } else {
                        filePath = "/storage/" + type + "/" + split[1];
                    }
                    return filePath;
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }


                String[] contentUriPrefixesToTry =
                        new String[]{"content://downloads/public_downloads",
                                "content://downloads/my_downloads"};

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri;
                    if (id != null && id.startsWith("msf:")) {
                        contentUri = Uri.withAppendedPath(Uri.parse(contentUriPrefix), id);
                    } else {
                        contentUri = ContentUris
                                .withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    }
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                    }
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    //                    contentUri = MediaStore.Files.getContentUri("external");
                    return copyFileToInternalStorage(context, uri, "Customer");
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see #getPath(Context, Uri)
     */
    public static File getFile (Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    public static String getReadableFileSize (int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec =
                new DecimalFormat("###.#", new DecimalFormatSymbols(Locale.ENGLISH));
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return dec.format(fileSize) + suffix;
    }

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     */
    public static Intent createGetContentIntent () {
        // Implicitly allow the user to select a particular kind of data
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // The MIME data type filter
        intent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }


    /**
     * Creates View intent for given file
     *
     * @param file
     * @return The intent for viewing file
     */
    public static Intent getViewIntent (Context context, File file) {
        //Uri uri = Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(context, AUTHORITY, file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = file.toString();
        if (url.contains(".doc") || url.contains(".docx")) {
            // Word document
            intent.setDataAndType(uri,
                    "application/msword |application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        } else if (url.contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.contains(".ppt") || url.contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri,
                    "application/vnd.ms-powerpoint|application/vnd.openxmlformats-officedocument.presentationml.presentation");
        } else if (url.contains(".xls") || url.contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri,
                    "application/vnd.ms-excel |application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if (url.contains(".zip") || url.contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.contains(".wav") || url.contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.contains(".3gp") || url.contains(".mpg") || url.contains(".mpeg") ||
                url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

  /*  public static File getDownloadsDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }*/

    public static File getDocumentCacheDir (@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        logDir(context.getCacheDir());
        logDir(dir);

        return dir;
    }

    private static void logDir (File dir) {
        if (!DEBUG) return;
        Log.d(TAG, "Dir=" + dir);
        File[] files = dir.listFiles();
        for (File file : files) {
            Log.d(TAG, "File=" + file.getPath());
        }
    }

    @Nullable public static File generateFileName (@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }

        logDir(directory);

        return file;
    }

    /**
     * Writes response body to disk
     *
     * @param body ResponseBody
     * @param path file path
     * @return File
     */
    public static File writeResponseBodyToDisk (ResponseBody body, String path) {
        try {
            File target = new File(path);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(target);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return target;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static void saveFileFromUri (Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] readBytesFromFile (String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }

    public static File createTempImageFile (Context context, String fileName) throws IOException {
        // Create an image file name
        File storageDir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    public static String getFileName (@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    public static String getName (String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }
}
