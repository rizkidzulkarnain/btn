package com.mitkoindo.smartcollection.module.misc;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import java.io.ByteArrayOutputStream;

public class ImageViewerActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //bitmap
    private Bitmap data_LoadedImage;

    //url image
    private String url_Image;

    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //webview
    private WebView webView;

    //generic alert
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        //webview buat load image
        webView = findViewById(R.id.ImageViewer_Webview);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);

        //create generic alert
        genericAlert = new GenericAlert(this);

        //get bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            url_Image = bundle.getString("ImageUrl");
            CreateDownloadImageRequest();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    public void HandleInput_ImageViewer_Back(View view)
    {
        finish();
    }

    //----------------------------------------------------------------------------------------------
    //  Load images
    //----------------------------------------------------------------------------------------------
    private void CreateDownloadImageRequest()
    {
        genericAlert.ShowLoadingAlert();
        new SendDownloadImageRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send load image request
    private class SendDownloadImageRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            NetworkConnection networkConnection = new NetworkConnection("", "");
            data_LoadedImage = networkConnection.SendDownloadImageRequest(url_Image);
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            AttachImageToWebview();
        }
    }

    //attach image to webview
    private void AttachImageToWebview()
    {
        genericAlert.Dismiss();
        if (data_LoadedImage == null)
            return;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        data_LoadedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String dataURL= "data:image/png;base64," + imgageBase64;
        /*String dataURL= "<style>img{display: inline;height: auto;max-width: 100%;}</style>data:image/png;base64," + imgageBase64;*/

        webView.loadUrl(dataURL);
    }
}
