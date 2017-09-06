package com.mitkoindo.smartcollection.module.berita;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;

public class DetailBeritaGlobalActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //subjek berita
    private TextView view_Subject;

    //tanggal berita
    private TextView view_Date;

    //content berita
    private TextView view_Content;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita_global);

        //setup
        GetViews();
        GetBundles();
    }

    //get views
    private void GetViews()
    {
        view_Subject = findViewById(R.id.DetailBeritaGlobal_Subject);
        view_Date = findViewById(R.id.DetailBeritaGlobal_Date);
        view_Content = findViewById(R.id.DetailBeritaGlobal_Content);
    }

    //get bundle data
    private void GetBundles()
    {
        //get bundle
        Bundle bundle = getIntent().getExtras();

        //pastikan bundle tidak null
        if (bundle == null)
            return;

        //get data yang dipass
        String data_Subject = bundle.getString("Subject");
        String data_Date = bundle.getString("Date");
        String data_Content = bundle.getString("Content");

        //attach data to view
        view_Subject.setText(data_Subject);
        view_Date.setText(data_Date);
        view_Content.setText(Html.fromHtml(data_Content));
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_DetailBeritaGlobal_Back(View view)
    {
        finish();
    }
}
