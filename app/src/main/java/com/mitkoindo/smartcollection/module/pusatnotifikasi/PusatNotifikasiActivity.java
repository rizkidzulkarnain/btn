package com.mitkoindo.smartcollection.module.pusatnotifikasi;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;

public class PusatNotifikasiActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //list of notifikasi
    private RecyclerView view_Recycler;

    //indicator load page baru
    private ProgressBar view_PageLoadIndicator;

    //progress bar
    private ProgressBar view_ProgressBar;

    //Alert text
    private TextView view_Alert;

    //refresher
    private SwipeRefreshLayout view_Refresher;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url buat get berita
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pusat_notifikasi);

        //setup
        GetViews();
        SetupTransaction();
    }

    //get views
    private void GetViews()
    {
        view_Recycler = findViewById(R.id.PusatNotifikasi_Recycler);
        view_PageLoadIndicator = findViewById(R.id.PusatNotifikasi_PageLoadIndicator);
        view_ProgressBar = findViewById(R.id.PusatNotifikasi_ProgressBar);
        view_Alert = findViewById(R.id.PusatNotifikasi_Alert);
        view_Refresher = findViewById(R.id.PusatNotifikasi_Refresher);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);
    }
    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_PusatNotifikasi_Back(View view)
    {
        finish();
    }
}
