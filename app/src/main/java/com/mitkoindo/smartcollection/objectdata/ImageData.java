package com.mitkoindo.smartcollection.objectdata;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.mitkoindo.smartcollection.utilities.NetworkConnection;

/**
 * Created by W8 on 19/09/2017.
 */

public class ImageData
{
    //set imageview
    private ImageView imageView;

    //bitmap file
    private Bitmap imageFile;

    //extra view
    private View extraView;

    //load image
    public void LoadImageToImageView(Activity context, String baseURL, String targetURL, ImageView imageView)
    {
        //set imageview
        this.imageView = imageView;

        //set url
        String usedURL = baseURL + targetURL.substring(1);

        //load image
        new SendDownloadImageRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, usedURL);
    }

    //set extra view
    public void SetExtraView(View extraView)
    {
        this.extraView = extraView;
    }

    //send request buat download image
    private class SendDownloadImageRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            NetworkConnection networkConnection = new NetworkConnection("", "");
            imageFile = networkConnection.SendDownloadImageRequest(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            //set image ke view
            if (imageFile != null)
                imageView.setImageBitmap(imageFile);

            //show extra view jika ada
            if (extraView != null)
                extraView.setVisibility(View.VISIBLE);
        }
    }
}
