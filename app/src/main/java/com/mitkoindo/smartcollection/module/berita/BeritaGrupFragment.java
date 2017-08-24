package com.mitkoindo.smartcollection.module.berita;


import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.BeritaAdapter;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeritaGrupFragment extends Fragment
{


    public BeritaGrupFragment()
    {
        // Required empty public constructor
    }

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //view data
    private RecyclerView view_ListBerita;

    //progress bar
    private ProgressBar view_ProgressBar;

    //message
    private TextView view_Message;

    //layout buat refresh
    private SwipeRefreshLayout view_SwipeRefresher;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter for berita
    /*private BeritaAdapter beritaAdapter;*/

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url yang digunakan
    private String baseURL;
    private String url_GetNews;

    //auth token
    private String authToken;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_berita_grup, container, false);
        GetViews(thisView);
        CreateGetNewsRequest();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        view_ListBerita = thisView.findViewById(R.id.BeritaGroupFragment_ListBerita);
        view_ProgressBar = thisView.findViewById(R.id.BeritaGroupFragment_ProgressBar);
        view_Message = thisView.findViewById(R.id.BeritaGroupFragment_Message);

        view_SwipeRefresher = thisView.findViewById(R.id.BeritaGroupFragment_SwipeRefresh);

        //set listener
        view_SwipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                CreateGetNewsRequest();
            }
        });
    }

    //set transaction data
    public void SetTransactionData(String baseURL, String url_GetNews, String authToken)
    {
        this.baseURL = baseURL;
        this.url_GetNews = url_GetNews;
        this.authToken = authToken;
    }

    //----------------------------------------------------------------------------------------------
    //  Start transaction buat load berita
    //----------------------------------------------------------------------------------------------
    private void CreateGetNewsRequest()
    {
        //show progress bar & hide recycler view
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_ListBerita.setVisibility(View.GONE);
        view_Message.setVisibility(View.GONE);
        view_SwipeRefresher.setRefreshing(false);

        //send request
        new SendGetNewsRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
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
            HandleGetGroupNewsResult(s);
        }
    }

    //create request object
    private JSONObject CreateGetNewsRequestObject()
    {
        //create empty object
        JSONObject requestObject = new JSONObject();

        //return object
        return requestObject;
    }

    //handle get global news
    private void HandleGetGroupNewsResult(String resultString)
    {

    }

    //Attach news data
    private void AttachNewsData()
    {
        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        view_ListBerita.setLayoutManager(layoutManager);
        view_ListBerita.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_ListBerita.addItemDecoration(dividerItemDecoration);
        /*view_ListBerita.setAdapter(beritaAdapter);*/

        //show list
        view_ListBerita.setVisibility(View.VISIBLE);
        view_Message.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);
    }
}
