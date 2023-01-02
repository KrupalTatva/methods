

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class ImageCompressor {
    private Context context;

    public ImageCompressor(Context context) {
        this.context = context;
    }

    //private SharedPreference sharedPreference = new SharedPreference(context);

    public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);

        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeFile(String path, int dstWidth, int dstHeight,
                                    ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);

        return BitmapFactory.decodeFile(path, options);
    }


    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
                                            ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }


    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                          ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }


    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    public String ImageCompressor(String path, File fileName) {
        return decodeFile(path, fileName);
    }

    public File ImageCompressor(Bitmap bitmap, File fileName) {
        return decodeBitmapFile(bitmap, fileName);
    }

    private File decodeBitmapFile(Bitmap bitmap, File fileName) {
        {
            String strMyImagePath = null;
            Bitmap scaledBitmap;

            try {
                // Part 1: Decode image

                if (!(bitmap.getWidth() <= 500 && bitmap.getHeight() <= 500)) {
                    // Part 2: Scale image
                    scaledBitmap = createScaledBitmap(bitmap, 612, 816, ScalingLogic.FIT);
                } else {
                    bitmap.recycle();
                    return null;
                }

                // Store to tmp file

                if (!fileName.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    fileName.mkdirs();
                }

                //String s = String.valueOf(System.currentTimeMillis()) + ".png";


                strMyImagePath = fileName.getAbsolutePath();
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(fileName);
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ERROR IN QRATUITY APP", e.getMessage());

                }

                scaledBitmap.recycle();
            } catch (Throwable e) {
                Log.d("ERROR IN QRATUITY APP", e.getMessage());
            }

            return fileName;

        }
    }

    private String decodeFile(String path, File fileName) {
        String strMyImagePath = null;
        Bitmap scaledBitmap;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = decodeFile(path, 800, 800, ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= 500 && unscaledBitmap.getHeight() <= 500)) {
                // Part 2: Scale image
                scaledBitmap = createScaledBitmap(unscaledBitmap, 700, 700, ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            //File mFolder = fileName;
            if (!fileName.exists()) {
                //noinspection ResultOfMethodCallIgnored
                fileName.mkdirs();
            }

            //String s = String.valueOf(System.currentTimeMillis()) + ".png";

            //File f = new File(fileName);

            strMyImagePath = fileName.getAbsolutePath();
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(fileName);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 60, fos);
                fos.flush();
                fos.close();
            }  catch (Exception e) {
                e.printStackTrace();
                Log.d("ERROR WHILE COMPRESSING", e.getMessage());
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
            Log.d("ERROR WHILE COMPRESSING", e.getMessage());
        }

        if (strMyImagePath == null) {
            return path;
        }

        return strMyImagePath;

    }

    public enum ScalingLogic {
        CROP, FIT
    }
}
