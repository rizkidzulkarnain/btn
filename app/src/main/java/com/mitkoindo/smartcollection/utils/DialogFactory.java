package com.mitkoindo.smartcollection.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.mitkoindo.smartcollection.R;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class DialogFactory {
    public static AlertDialog createLoadingDialog(Context context) {
        return ProgressDialog.show(context, null, context.getString(R.string.loading), true, false);
    }
}
