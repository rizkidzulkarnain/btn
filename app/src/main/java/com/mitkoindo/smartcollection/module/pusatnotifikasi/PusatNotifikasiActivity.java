package com.mitkoindo.smartcollection.module.pusatnotifikasi;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.NotificationAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.berita.DetailBeritaGroupActivity;
import com.mitkoindo.smartcollection.module.chat.ChatWindowActivity;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.NotificationData;

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
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter
    private NotificationAdapter notificationAdapter;

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
        AttachAdapter();
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

    //attach adapter
    private void AttachAdapter()
    {
        //create adapter
        notificationAdapter = new NotificationAdapter(this);

        //set transaksi
        notificationAdapter.SetTransactionData(baseURL, url_DataSP, authToken, userID);

        //set views
        notificationAdapter.SetViews(view_ProgressBar, view_Recycler, view_Alert);

        //handle input
        notificationAdapter.setClickListener(new ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                OpenNotificationDetail(position);
            }
        });

        //create request
        notificationAdapter.CreateGetNotificationRequest();

        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_Recycler.setLayoutManager(layoutManager);
        view_Recycler.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_Recycler.addItemDecoration(dividerItemDecoration);
        view_Recycler.setAdapter(notificationAdapter);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_PusatNotifikasi_Back(View view)
    {
        finish();
    }

    //open notification detail
    private void OpenNotificationDetail(int index)
    {
        //get item at position
        NotificationData notificationData = notificationAdapter.GetItemAt(index);

        //pastikan item tidak null
        if (notificationData == null)
            return;

        //set intent
        Intent intent = null;

        //switch result tergantung item type
        switch (notificationData.PageType)
        {
            case "PagePtp" :
                //open detail debitur
                intent = DetailDebiturActivity.instantiate(this, notificationData.PageID,
                        "", ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE);
                break;
            case "PageChat" :
                //open chat window untuk staff ini
                intent = new Intent(this, ChatWindowActivity.class);
                intent.putExtra("PartnerID", notificationData.PageID);

                //get staffname
                String staffName = notificationData.Message.substring(17);
                intent.putExtra("PartnerName", staffName);
                break;
            case "PageNewsGroup" :
                //open detail berita
                intent = new Intent(this, DetailBeritaGroupActivity.class);
                intent.putExtra("NewsID", notificationData.PageID);
                break;
            case "PageAssignment" :
                //open detail debitur
                intent = DetailDebiturActivity.instantiate(this, notificationData.PageID,
                        "", ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE);
                break;
            default:break;
        }

        //pastikan intent tidak null
        if (intent == null)
            return;

        //start activitynya
        startActivity(intent);
    }
}
