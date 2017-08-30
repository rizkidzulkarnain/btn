package com.mitkoindo.smartcollection.module.ptp_reminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;

public class PTPReminderActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //ptp list
    private RecyclerView view_List;

    //alert
    private TextView view_Alert;

    //progress bar
    private ProgressBar view_ProgressBar;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSp;

    //auth token
    private String authToken;

    //user id
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptpreminder);

        //setup
        GetViews();
        SetupTransaction();
    }

    //get views
    private void GetViews()
    {
        view_List = findViewById(R.id.PTP_Reminder_List);
        view_Alert = findViewById(R.id.PTP_Reminder_Alert);
        view_ProgressBar = findViewById(R.id.PTP_Reminder_ProgressBar);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSp = getString(R.string.URL_Data_StoreProcedure);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //load user id
        userID = ResourceLoader.LoadUserID(this);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    public void HandleInput_PTPReminder_Back(View view)
    {
        finish();
    }
}
