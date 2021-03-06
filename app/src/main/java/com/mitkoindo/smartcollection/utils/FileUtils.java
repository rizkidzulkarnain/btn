package com.mitkoindo.smartcollection.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.mitkoindo.smartcollection.MyApplication;

import java.io.File;


/**
 * Created by ericwijaya on 8/25/17.
 */

public class FileUtils {

    public static String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = MyApplication.getInstance().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static boolean deleteFile(String filePath) {
        File fdelete = new File(filePath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
