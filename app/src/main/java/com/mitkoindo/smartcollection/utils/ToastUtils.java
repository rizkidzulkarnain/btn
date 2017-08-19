package com.mitkoindo.smartcollection.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class ToastUtils {

    public static Toast toastLong(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();

        return toast;
    }

    public static void toastLong(Context context, int resId) {
        Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_LONG).show();
    }

    public static void toastShort(Context context, int resId) {
        Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
