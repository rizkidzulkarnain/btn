package com.mitkoindo.smartcollection.module.berita;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;

public class DetailBeritaGroupActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //title news
    private TextView view_Title;

    //news sender
    private TextView view_Sender;

    //news start date
    private TextView view_StartDate;

    //news expired date
    private TextView view_EndDate;

    //news content
    private TextView view_Content;

    //news filename
    private TextView view_Filename;

    //holder file
    private View holder_NoFile;
    private View holder_FileInfo;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url
    private String baseURL;

    //attachmentURL
    private String data_AttachmentURL;

    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita_group);

        //setup
        GetViews();
        SetupTransaction();
        GetBundles();
    }

    //get views
    private void GetViews()
    {
        view_Title = findViewById(R.id.DetailBeritaGroup_Title);
        view_Sender = findViewById(R.id.DetailBeritaGroup_SenderID);
        view_StartDate = findViewById(R.id.DetailBeritaGroup_StartDate);
        view_EndDate = findViewById(R.id.DetailBeritaGroup_ExpiredDate);
        view_Content = findViewById(R.id.DetailBeritaGroup_Content);
        view_Filename = findViewById(R.id.DetailBeritaGroup_FileName);

        holder_FileInfo = findViewById(R.id.DetailBeritaGroup_FileHolder);
        holder_NoFile = findViewById(R.id.DetailBeritaGroup_NoFile);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        baseURL = ResourceLoader.LoadBaseURL(this);
    }

    //get bundles
    private void GetBundles()
    {
        //get bundle data dari intent
        Bundle bundle = getIntent().getExtras();

        //pastikan bundle tidak null
        if (bundle == null)
            return;

        //get data
        String data_Title = bundle.getString("Title");
        String data_Sender = bundle.getString("Sender");
        String data_StartDate = bundle.getString("StartDate");
        String data_EndDate = bundle.getString("EndDate");
        String data_Content = bundle.getString("Content");
        data_AttachmentURL = bundle.getString("Attachment", "");

        //set views
        view_Title.setText(data_Title);
        view_Sender.setText(data_Sender);
        view_StartDate.setText(data_StartDate);
        view_EndDate.setText(data_EndDate);
        view_Content.setText(data_Content);

        //cek apakah ada attachment atau tidak
        if (data_AttachmentURL == null || data_AttachmentURL.isEmpty())
        {
            //show no data alert
            holder_NoFile.setVisibility(View.VISIBLE);

            //hide file info
            holder_FileInfo.setVisibility(View.GONE);
        }
        else
        {
            //hide no data alert
            holder_NoFile.setVisibility(View.GONE);

            //show file info
            holder_FileInfo.setVisibility(View.VISIBLE);

            //attach file url
            view_Filename.setText(data_AttachmentURL);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //tap back button
    public void HandleInput_DetailBeritaGroup_Back(View view)
    {
        finish();
    }

    //tapi file info
    public void HandleInput_DetailBeritaGroup_File(View view)
    {
        //buka attachment
        String attachmentURL = data_AttachmentURL;
        char attachmentURL_FirstCharacter = attachmentURL.charAt(0);
        if (attachmentURL_FirstCharacter == '/')
        {
            attachmentURL = attachmentURL.substring(1);
        }

        String intentURL = baseURL + attachmentURL;

        //open intent
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(intentURL));
        startActivity(i);
    }
}
