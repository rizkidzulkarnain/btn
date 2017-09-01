package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.GlobalNews;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 23/08/2017.
 */

public class BeritaGlobalAdapter extends RecyclerView.Adapter<BeritaGlobalAdapter.BeritaGlobalViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //context
    private Activity context;

    //list of berita
    private ArrayList<GlobalNews> globalNews;

    //flag boleh load data baru atau tidak
    private boolean allowLoadData;

    //counter page
    private int currentPage;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //indicator buat load page baru
    private ProgressBar view_ProgressBar_PageIndicator;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public BeritaGlobalAdapter(Activity context, ArrayList<GlobalNews> globalNews)
    {
        this.context = context;
        this.globalNews = globalNews;

        //saat inisialisasi, set flag allow load data
        this.allowLoadData = true;

        //set current page ke dua, karena page 1 sudah diload di berita fragment
        this.currentPage = 2;
    }

    @Override
    public BeritaGlobalViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater inflater = context.getLayoutInflater();
        View thisView = inflater.inflate(R.layout.adapter_berita_global, parent, false);
        return new BeritaGlobalViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(BeritaGlobalViewHolder holder, int position)
    {
        //pastikan index tidak melebihi posisi
        if (position >= getItemCount())
            return;

        //get current news
        GlobalNews currentNews = globalNews.get(position);

        //attach data to view
        holder.Title.setText(currentNews.Subject);
        holder.SendTime.setText(currentNews.TanggalBerita);
        holder.Content.setText(Html.fromHtml(currentNews.Content));
    }

    @Override
    public int getItemCount()
    {
        return globalNews.size();
    }

    //----------------------------------------------------------------------------------------------
    //  Set views
    //----------------------------------------------------------------------------------------------
    //set progress bar
    public void SetView_ProgressBar(ProgressBar progressBar)
    {
        this.view_ProgressBar_PageIndicator = progressBar;
    }

    //----------------------------------------------------------------------------------------------
    //  Set transaksi
    //----------------------------------------------------------------------------------------------
    public void SetupTransaction(String baseURL, String url_DataSP, String authToken)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class BeritaGlobalViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title;
        TextView SendTime;
        TextView Content;

        public BeritaGlobalViewHolder(View itemView)
        {
            super(itemView);

            Title = itemView.findViewById(R.id.AdapterBeritaGlobal_Title);
            SendTime = itemView.findViewById(R.id.AdapterBeritaGlobal_SendTime);
            Content = itemView.findViewById(R.id.AdapterBeritaGlobal_Content);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat load next page
    //----------------------------------------------------------------------------------------------
    public void CreateLoadNewPageRequest()
    {
        //cek apakah adapter ini boleh load data baru atau tidak
        if (!allowLoadData)
            return;

        //jika boleh, show progress bar
        view_ProgressBar_PageIndicator.setVisibility(View.VISIBLE);

        //disable load data permission
        allowLoadData = false;

        //dan execute request buat load page baru
        new SendLoadNewPageRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request buat load data baru
    private class SendLoadNewPageRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetNewsRequestObject();

            //send transaksi
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleLoadNewPageResult(s);
        }
    }

    //create request object
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
            dbParam.put("Page", currentPage);
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

    //handle result dari load page
    private void HandleLoadNewPageResult(String result)
    {
        //disable progress bar
        view_ProgressBar_PageIndicator.setVisibility(View.GONE);

        //cek result
        if (result == null || result.isEmpty())
        {
            return;
        }

        //jika result 404, maka sudah tidak ada page baru lagi, maka matikan flag allow load
        if (result.equals("Not Found"))
        {
            allowLoadData = false;
            return;
        }

        try
        {
            //ekstrak data
            JSONArray dataArray = new JSONArray(result);

            //add data ke list of news
            for (int i = 0; i < dataArray.length(); i++)
            {
                GlobalNews newGlobalNews = new GlobalNews();
                newGlobalNews.ParseData(dataArray.getJSONObject(i));
                globalNews.add(newGlobalNews);
            }

            //refresh adapter
            notifyDataSetChanged();

            //dan naikkan page counter
            currentPage++;

            //enable load data permission
            allowLoadData = true;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
