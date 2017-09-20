package com.mitkoindo.smartcollection.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ericwijaya on 8/19/17.
 */

public class Utils {

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getAppVersion(Context aContext) {
        try {
            PackageInfo packageInfo = aContext.getPackageManager()
                    .getPackageInfo(aContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static int convertDensityPixel(int dip, Resources r) {
        return (int) (dip * r.getDisplayMetrics().density);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBound s= true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int getOrientationFromExif(String imagePath) {
        int orientation = -1;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Log.e("Send", "Unable to get image exif orientation", e);
        }

        return orientation;
    }

    public static String dateToString(Date aDate, String aFormat) {
        if (aDate != null) {
            Locale id = new Locale("in", "ID");
            SimpleDateFormat dateFormat = new SimpleDateFormat(aFormat, id);
            try {
                String datetime = dateFormat.format(aDate);
                return datetime;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static String changeDateFormat(String aDate, String aFromFormat, String aToFormat) {
        if (!TextUtils.isEmpty(aDate)) {
            Locale id = new Locale("in", "ID");
            SimpleDateFormat format = new SimpleDateFormat(aFromFormat, id);
            try {
                Date date = format.parse(aDate);
                return dateToString(date, aToFormat);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static String convertDoubleToString(double eval, String text) {
        String res = String.valueOf(eval);

        res = removeLastChar(res, text);

        return res;
    }

    @NonNull
    public static String removeLastChar(String res, String text) {
        int length = text.length();

        if (res.length() > length) {
            res = res.substring((res.length() - length), res.length()).equals(text)
                    ? res.substring(0, (res.length() - length)) : res;
        }
        return res;
    }

    public static int stringToInt(String stringNumber) {
        int number = 0;
        try {
            number = Integer.parseInt(stringNumber);
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        return number;
    }
}
