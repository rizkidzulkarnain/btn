package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.module.berita.BeritaGrupFragment;
import com.mitkoindo.smartcollection.module.berita.DetailBeritaGroupActivity;
import com.mitkoindo.smartcollection.objectdata.GlobalNews;
import com.mitkoindo.smartcollection.objectdata.MobileNews;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 22/08/2017.
 */

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //reference ke context
    private Activity context;

    //data berita
    private ArrayList<MobileNews> news = new ArrayList<>();

    //flag boleh load data baru atau tidak
    private boolean allowLoadData;

    //counter page
    private int currentPage;

    //search query
    private String searchQuery;

    //----------------------------------------------------------------------------------------------
    //  Input
    //----------------------------------------------------------------------------------------------
    //click listener
    private ItemClickListener itemClickListener;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //indicator buat load page baru
    private ProgressBar view_ProgressBar_PageIndicator;

    //recylerview
    private RecyclerView view_Recycler;
    private ProgressBar view_ProgressBar;
    private TextView view_AlertText;

    //flag buat bind views
    private boolean onBind;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user id
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public BeritaAdapter(Activity context)
    {
        this.context = context;

        //set allow load data jadi true
        allowLoadData = true;

        //set current page jadi 2, karena page pertama sudah diload di fragment
        currentPage = 2;
    }

    @Override
    public BeritaViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //get layout inflater
        LayoutInflater inflater = context.getLayoutInflater();

        //inflate layout
        View thisView = inflater.inflate(R.layout.adapter_berita, parent, false);

        //return viewholder
        return new BeritaViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(BeritaViewHolder holder, int position)
    {
        //pastikan posisi nggak melebihi index
        if (position >= getItemCount())
            return;

        //bind view
        onBind = true;

        //get current item, dan attach ke holder
        MobileNews currentNews = news.get(position);

        //attach to view
        holder.Title.setText(currentNews.Title);
        holder.Sender.setText(currentNews.AuthorID);
        holder.Content.setText(currentNews.Summary);
        holder.SendTime.setText(currentNews.StartDate);

        //cek attachment
        if (currentNews.Attachment == null || currentNews.Attachment.isEmpty())
        {
            //show no attachment text
            holder.Holder_NoFile.setVisibility(View.VISIBLE);
            holder.Holder_FileExist.setVisibility(View.GONE);
        }
        else
        {
            //show attachment
            holder.Holder_FileExist.setVisibility(View.VISIBLE);
            holder.Holder_NoFile.setVisibility(View.GONE);

            //set filename
            holder.FileName.setText(currentNews.Attachment);
        }

        //add listener to main view
        final int currentPos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenNewsDetail(currentPos);
            }
        });

        //release bind
        onBind = false;
    }

    @Override
    public int getItemCount()
    {
        return news.size();
    }

    //get current item
    public MobileNews GetCurrentNews(int index)
    {
        //pastikan index tidak melebihi item size
        if (index >= getItemCount())
            return null;

        //return news item
        return news.get(index);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle Input
    //----------------------------------------------------------------------------------------------
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    //open news detail
    private void OpenNewsDetail(int position)
    {
        //pastikan posisi tidak melebihi index
        if (position >= getItemCount())
            return;

        //get current item
        MobileNews mobileNews = news.get(position);

        //create intent
        Intent intent = new Intent(context, DetailBeritaGroupActivity.class);

        //attach extra data
        intent.putExtra("Title", mobileNews.Title);
        intent.putExtra("Sender", mobileNews.AuthorID);
        intent.putExtra("StartDate", mobileNews.StartDate);
        intent.putExtra("EndDate", mobileNews.EndDate);
        intent.putExtra("Content", mobileNews.NewsContent);
        intent.putExtra("Attachment", mobileNews.Attachment);

        //start intent
        context.startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    //  Set views
    //----------------------------------------------------------------------------------------------
    //set progress bar
    public void SetView_ProgressBar(ProgressBar progressBar)
    {
        this.view_ProgressBar_PageIndicator = progressBar;
    }

    //set views
    public void SetViews(RecyclerView view_Recycler, ProgressBar view_ProgressBar, TextView view_AlertText)
    {
        this.view_Recycler = view_Recycler;
        this.view_ProgressBar = view_ProgressBar;
        this.view_AlertText = view_AlertText;
    }

    //----------------------------------------------------------------------------------------------
    //  Set transaksi
    //----------------------------------------------------------------------------------------------
    public void SetupTransaction(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //set search query
    public void SetSearchQuery(String searchQuery)
    {
        this.searchQuery = searchQuery;
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    //Viewholder
    class BeritaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //views
        TextView Title;
        TextView Sender;
        TextView SendTime;
        TextView Content;
        ImageView FileIcon;
        TextView FileName;

        //holder file
        View Holder_NoFile;
        View Holder_FileExist;

        public BeritaViewHolder(View itemView)
        {
            super(itemView);

            Title = itemView.findViewById(R.id.AdapterBerita_Title);
            Sender = itemView.findViewById(R.id.AdapterBerita_Sender);
            SendTime = itemView.findViewById(R.id.AdapterBerita_SendTime);
            Content = itemView.findViewById(R.id.AdapterBerita_Content);
            FileIcon = itemView.findViewById(R.id.AdapterBerita_FileIcon);
            FileName = itemView.findViewById(R.id.AdapterBerita_FileName);

            Holder_NoFile = itemView.findViewById(R.id.AdapterBerita_NoFile);
            Holder_FileExist = itemView.findViewById(R.id.AdapterBerita_FileHolder);

            //add listener ke file exist holder
            Holder_FileExist.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat load data
    //----------------------------------------------------------------------------------------------
    //create request buat get data berita
    public void CreateGetNewsRequest()
    {
        //show porgress bar, hide everything else
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_AlertText.setVisibility(View.GONE);
        view_Recycler.setVisibility(View.GONE);

        //send request
        new SendGetNewsRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create search request
    public void CreateSearchRequest(String searchQuery)
    {
        this.searchQuery = searchQuery;
        CreateGetNewsRequest();
    }

    //create request object
    private JSONObject CreateGetNewsRequestObject()
    {
        //create empty object
        JSONObject requestObject = new JSONObject();

        //populate request object
        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("keyword", searchQuery);

            //create request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_NEWS_LIST");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return object
        return requestObject;
    }

    private class SendGetNewsRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetNewsRequestObject();

            //send request
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
        //pastikan result tidak null / kosong
        if (resultString == null || resultString.isEmpty() || resultString.equals("Bad Request"))
        {
            //show alert
            view_AlertText.setText(R.string.Text_SomethingWrong);
            view_AlertText.setVisibility(View.VISIBLE);
            view_ProgressBar.setVisibility(View.GONE);
            return;
        }

        //pastikan data tidak kosong
        if (resultString.equals("Not Found"))
        {
            //show alert
            view_AlertText.setText(R.string.Text_NoData);
            view_AlertText.setVisibility(View.VISIBLE);
            view_ProgressBar.setVisibility(View.GONE);
            return;
        }

        try
        {
            //extract data
            JSONArray dataArray = new JSONArray(resultString);

            //create news data array
            ArrayList<MobileNews> newNews = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++)
            {
                //extract
                MobileNews newMobileNews = new MobileNews();
                newMobileNews.ParseData(dataArray.getJSONObject(i));
                newNews.add(newMobileNews);
            }

            //attach data to list
            AttachNewsData(newNews);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show alert
            view_AlertText.setText(R.string.Text_SomethingWrong);
            view_AlertText.setVisibility(View.VISIBLE);
            view_ProgressBar.setVisibility(View.GONE);
        }
    }

    //attach news data
    private void AttachNewsData(ArrayList<MobileNews> newNews)
    {
        //initialize arraylist
        news = new ArrayList<>();
        for (int i = 0; i < newNews.size(); i++)
        {

            news.add(newNews.get(i));
        }

        //bind data
        if (!onBind)
            notifyDataSetChanged();

        //show recycler view, hide everything else
        view_Recycler.setVisibility(View.VISIBLE);
        view_AlertText.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);
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

    //send request
    private class SendLoadNewPageRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetNewsRequestObject();

            //create network connection
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

    //handle result transaksi
    private void HandleLoadNewPageResult(String resultString)
    {
        //hide progressbar
        view_ProgressBar_PageIndicator.setVisibility(View.GONE);

        //pastikan hasil transaksi tidak null
        if (resultString == null || resultString.isEmpty())
        {
            return;
        }

        //jika return 404, maka sudah tidak ada page baru lagi
        if (resultString.equals("Not Found"))
        {
            allowLoadData = false;
            return;
        }

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
                news.add(newMobileNews);
            }

            //refresh adapter
            notifyDataSetChanged();

            //naikkan page counter
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
