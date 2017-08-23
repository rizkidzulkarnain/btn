package com.mitkoindo.smartcollection.module.berita;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.BeritaAdapter;
import com.mitkoindo.smartcollection.adapter.BeritaGlobalAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.GlobalNews;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BeritaActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //list of berita
    private RecyclerView view_ListBerita;

    //generic alert
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter for berita
    private BeritaGlobalAdapter beritaAdapter;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url buat get berita
    private String baseURL;
    private String url_GetBerita;

    //auth token
    private String authToken;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);

        //setup
        GetViews();
        SetupTransaction();
        LoadNews();
    }

    //get reference ke view
    private void GetViews()
    {
        //get list berita
        view_ListBerita = findViewById(R.id.BeritaActivity_ListBerita);

        //create generic alert
        genericAlert = new GenericAlert(this);
    }

    //set transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_GetBerita = getString(R.string.URL_Data_View);

        //get auth token
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        authToken = sharedPreferences.getString(key_AuthToken, "");
    }

    //load data
    private void LoadNews()
    {
        //show loading alert
        genericAlert.ShowLoadingAlert();

        //send request buat get news
        new SendGetGlobalNewsRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_Berita_Back(View view)
    {
        finish();
    }

    //open broadcast berita
    public void HandleInput_Berita_CreateBroadcast(View view)
    {
        Intent intent = new Intent(this, BroadcastBeritaActivity.class);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    //  Load global news
    //----------------------------------------------------------------------------------------------
    //create async task buat load news
    private class SendGetGlobalNewsRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url untul transaksi
            String usedURL = baseURL + url_GetBerita;

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
            HandleGetGlobalNewsResult(s);
        }
    }

    //create request object buat get news data
    private JSONObject CreateGetNewsRequestObject()
    {
        //create empty object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sorting object
            JSONObject sortingObject = new JSONObject();
            sortingObject.put("Property", "TanggalBerita");
            sortingObject.put("Direction", "DESC");

            //create sorting array
            JSONArray sortingArray = new JSONArray();
            sortingArray.put(sortingObject);

            //create dbParam
            JSONObject dbParam = new JSONObject();
            dbParam.put("Page", 1);
            dbParam.put("Limit", 10);
            dbParam.put("Sort", sortingArray);

            //create filter object
            JSONObject filterObject = new JSONObject();
            filterObject.put("Property", "Publish");
            filterObject.put("Operator", "eq");
            filterObject.put("Value", 1);

            //create filter array
            JSONArray filterArray = new JSONArray();
            filterArray.put(filterObject);

            //create request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("ViewName", "MKI_VW_NEWS_GLOBAL");
            requestObject.put("DBParam", dbParam);
            requestObject.put("Filter", filterArray);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return object
        return requestObject;
    }

    //handle get global news
    private void HandleGetGlobalNewsResult(String resultString)
    {
        //dismiss loading alert
        genericAlert.Dismiss();

        try
        {
            //create json object
            JSONArray resultArray = new JSONArray(resultString);

            //create array of news
            ArrayList<GlobalNews> news = new ArrayList<>();

            //parse all news item
            for (int i = 0; i < resultArray.length(); i++)
            {
                GlobalNews globalNews = new GlobalNews();
                globalNews.ParseData(resultArray.getJSONObject(i));
                news.add(globalNews);
            }

            //create news adapter
            beritaAdapter = new BeritaGlobalAdapter(this, news);
            AttachNewsData();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //Attach news data
    private void AttachNewsData()
    {
        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_ListBerita.setLayoutManager(layoutManager);
        view_ListBerita.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_ListBerita.addItemDecoration(dividerItemDecoration);
        view_ListBerita.setAdapter(beritaAdapter);
    }
}
