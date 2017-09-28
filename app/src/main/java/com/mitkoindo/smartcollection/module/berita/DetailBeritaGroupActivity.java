package com.mitkoindo.smartcollection.module.berita;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.BeritaAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.MobileNews;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    //get news url
    private String url_GetNews;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //id berita
    private String newsID;

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

        //cek apakah bundle contains newsid
        if (bundle.containsKey("NewsID"))
        {
            //jika iya, maka page ini dibuka dari notifikasi, sehingga detail berita harus direquest dulu,
            //nggak diprovide oleh bundle
            newsID = bundle.getString("NewsID");

            //get news data
            CreateGetNewsDataRequest();
            return;
        }

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

        String documentSupportURL = getString(R.string.DocumentSupportURL);

        //cek apakah urlnya end in pdf
        if (intentURL.endsWith(".pdf"))
        {
            //jika iya, add document support url di depannya
            intentURL = documentSupportURL + intentURL;
        }

        //open intent
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(intentURL));
        startActivity(i);
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get data berita
    //----------------------------------------------------------------------------------------------
    private void CreateGetNewsDataRequest()
    {
        url_GetNews = getString(R.string.URL_Data_View);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);

        //send request
        new SendGetNewsRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create request object
    private JSONObject CreateGetNewsRequestObject()
    {
        //create empty object
        JSONObject requestObject = new JSONObject();

        //populate request object
        try
        {
            //create sorting object
            JSONObject sortingObject = new JSONObject();
            sortingObject.put("Property", "CreatedDate");
            sortingObject.put("Direction", "DESC");

            //create sorting array
            JSONArray sortingArray = new JSONArray();
            sortingArray.put(sortingObject);

            //create dbParam
            JSONObject dbParam = new JSONObject();
            dbParam.put("Page", 1);
            dbParam.put("Limit", 1);
            dbParam.put("Sort", sortingArray);

            //create filter object
            JSONObject filterObject = new JSONObject();
            filterObject.put("Property", "ToUserID");
            filterObject.put("Operator", "eq");
            filterObject.put("Value", "'" + userID + "'");

            //create filter by id
            JSONObject filterIDObject = new JSONObject();
            filterIDObject.put("Property", "ID");
            filterIDObject.put("Operator", "eq");
            /*filterIDObject.put("Value", "'" + newsID + "'");*/
            filterIDObject.put("Value", newsID);

            //create filter array
            JSONArray filterArray = new JSONArray();
            filterArray.put(filterObject);
            filterArray.put(filterIDObject);

            //add to dbParam
            dbParam.put("Filter", filterArray);

            //create request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("ViewName", "MKI_VW_NEWS_GROUP");
            requestObject.put("DBParam", dbParam);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return object
        return requestObject;
    }

    //send get news request
    private class SendGetNewsRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create used url
            String usedURL = baseURL + url_GetNews;

            //create request object
            JSONObject requestObject = CreateGetNewsRequestObject();

            //execute transaction
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetNewsResult(s);
        }
    }

    //handle resultnya
    private void HandleGetNewsResult(String resultString)
    {
        try
        {
            //parse data
            JSONArray dataArray = new JSONArray(resultString);

            //extract data
            for (int i = 0; i < dataArray.length(); i++)
            {
                //extract data dari json
                MobileNews newMobileNews = new MobileNews();
                newMobileNews.ParseData(dataArray.getJSONObject(i));

                //attach data to view
                //set views
                view_Title.setText(newMobileNews.Title);
                view_Sender.setText(newMobileNews.AuthorID);
                view_StartDate.setText(newMobileNews.StartDate);
                view_EndDate.setText(newMobileNews.EndDate);
                view_Content.setText(newMobileNews.NewsContent);

                //cek apakah ada attachment atau tidak
                if (newMobileNews.Attachment == null || newMobileNews.Attachment.isEmpty())
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
                    view_Filename.setText(newMobileNews.Attachment);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
