package com.mitkoindo.smartcollection.dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.mitkoindo.smartcollection.R;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class DialogFactory {

    public static AlertDialog.Builder dialogBuilder(Context context,
                                                    String title, String message,
                                                    String positiveButtonLabel,
                                                    String negativeButtonLabel,
                                                    String neutralButtonLabel,
                                                    DialogInterface.OnClickListener onPositiveButtonClick,
                                                    DialogInterface.OnClickListener onNegativeButtonClick,
                                                    DialogInterface.OnClickListener onNeutralButtonClick,
                                                    boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(cancelable);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (onPositiveButtonClick != null) {
            builder.setPositiveButton(positiveButtonLabel, onPositiveButtonClick);
        }
        if (onNegativeButtonClick != null) {
            builder.setNegativeButton(negativeButtonLabel, onNegativeButtonClick);
        }
        if (onNeutralButtonClick != null) {
            builder.setNeutralButton(neutralButtonLabel, onNeutralButtonClick);
        }
        return builder;
    }

    public static AlertDialog createDialog(Context context,
                                           String title, String message,
                                           String positiveButtonLabel,
                                           String negativeButtonLabel,
                                           String neutralButtonLabel,
                                           DialogInterface.OnClickListener onPositiveButtonClick,
                                           DialogInterface.OnClickListener onNegativeButtonClick,
                                           DialogInterface.OnClickListener onNeutralButtonClick,
                                           boolean cancelable) {

        return dialogBuilder(context, title, message, positiveButtonLabel, negativeButtonLabel, neutralButtonLabel,
                onPositiveButtonClick, onNegativeButtonClick, onNeutralButtonClick, cancelable).create();
    }

    public static AlertDialog createDialog(Context context,
                                           String title, String message,
                                           String positiveButtonLabel,
                                           String negativeButtonLabel,
                                           DialogInterface.OnClickListener onPositiveButtonClick,
                                           DialogInterface.OnClickListener onNegativeButtonClick) {
        return createDialog(context, title, message, positiveButtonLabel, negativeButtonLabel, null,
                onPositiveButtonClick, onNegativeButtonClick, null, false);
    }

    public static AlertDialog createDialog(Context context,
                                           String title, String message,
                                           String positiveButtonLabel,
                                           DialogInterface.OnClickListener onPositiveButtonClick) {
        return createDialog(context, title, message,
                positiveButtonLabel, null,
                onPositiveButtonClick, null);
    }

    public static AlertDialog createLoadingDialog(Context context) {
        return ProgressDialog.show(context, null, context.getString(R.string.Text_PleaseWait), true, false);
    }
}
