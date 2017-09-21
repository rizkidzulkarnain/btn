package com.mitkoindo.smartcollection.module.berita;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.BeritaAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.RecyclerViewHelper;
import com.mitkoindo.smartcollection.objectdata.MobileNews;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    //progress bar buat indicator load page baru
    private ProgressBar view_ProgressBar_PageIndicator;

    //search form
    private EditText view_SearchForm;

    //search button
    private ImageView view_SearchButton;

    //clear button
    private ImageView view_ClearButton;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter for berita
    private BeritaAdapter beritaAdapter;

    //search query
    private String searchQuery;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url yang digunakan
    private String baseURL;
    private String url_GetNews;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_berita_grup, container, false);
        GetViews(thisView);
        SetupAdapter();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        view_ListBerita = thisView.findViewById(R.id.BeritaGroupFragment_ListBerita);
        view_ProgressBar = thisView.findViewById(R.id.BeritaGroupFragment_ProgressBar);
        view_Message = thisView.findViewById(R.id.BeritaGroupFragment_Message);
        view_ProgressBar_PageIndicator = thisView.findViewById(R.id.BeritaGrupFragment_PageLoadingIndicator);
        view_SwipeRefresher = thisView.findViewById(R.id.BeritaGroupFragment_SwipeRefresh);
        view_SearchForm = thisView.findViewById(R.id.BeritaGroupFragment_SearchForm);
        view_SearchButton = thisView.findViewById(R.id.BeritaGroupFragment_SearchButton);
        view_ClearButton = thisView.findViewById(R.id.BeritaGroupFragment_ClearButton);

        //set listener
        view_SwipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                view_SwipeRefresher.setRefreshing(false);
                beritaAdapter.CreateGetNewsRequest();
            }
        });

        //set listener pada list
        view_ListBerita.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                //pastikan adapter tidak null
                /*if (beritaAdapter == null)
                    return;

                //load new page
                if (RecyclerViewHelper.isLastItemDisplaying(view_ListBerita))
                    beritaAdapter.CreateLoadNewPageRequest();*/
            }
        });

        //add listener to search form
        view_SearchForm.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    //Handle search
                    //hide search button, show clear button
                    view_ClearButton.setVisibility(View.VISIBLE);
                    view_SearchButton.setVisibility(View.GONE);
                    CreateSearchRequest();

                    return true;
                }

                return false;
            }
        });

        //add listener pada search button
        view_SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleInput_SearchButton();
            }
        });

        //add listener pada clear button
        view_ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleInput_ClearButton();
            }
        });
    }

    //set transaction data
    public void SetTransactionData(String baseURL, String url_GetNews, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_GetNews = url_GetNews;
        this.authToken = authToken;
        this.userID = userID;
    }

    //Attach news data
    private void SetupAdapter()
    {
        //create mobile news adapter
        beritaAdapter = new BeritaAdapter(getActivity());
        beritaAdapter.SetView_ProgressBar(view_ProgressBar_PageIndicator);
        beritaAdapter.SetupTransaction(baseURL, url_GetNews, authToken, userID);
        beritaAdapter.SetViews(view_ListBerita, view_ProgressBar, view_Message);
        beritaAdapter.CreateGetNewsRequest();
        /*beritaAdapter.SetSearchQuery(searchQuery);*/

        //set click listener ke adapter
        beritaAdapter.setClickListener(new ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                OpenAttachedFile(position);
            }
        });

        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        view_ListBerita.setLayoutManager(layoutManager);
        view_ListBerita.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_ListBerita.addItemDecoration(dividerItemDecoration);
        view_ListBerita.setAdapter(beritaAdapter);
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat search
    //----------------------------------------------------------------------------------------------
    private void CreateSearchRequest()
    {
        //set search query
        searchQuery = view_SearchForm.getText().toString();

        //send request via adapter
        beritaAdapter.CreateSearchRequest(searchQuery);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //open attachment
    private void OpenAttachedFile(int position)
    {
        //get current berita
        MobileNews newsItem = beritaAdapter.GetCurrentNews(position);

        //pastikan item tidak null
        if (newsItem == null)
            return;

        //buka attachment
        String attachmentURL = newsItem.Attachment;
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

    //Handle input pada search button
    private void HandleInput_SearchButton()
    {
        //hide search button, show clear button
        view_ClearButton.setVisibility(View.VISIBLE);
        view_SearchButton.setVisibility(View.GONE);

        CreateSearchRequest();
    }

    //Handle input pada clear button
    private void HandleInput_ClearButton()
    {
        //jika search form tidak kosong, clear textnya
        if (!view_SearchForm.getText().toString().isEmpty())
        {
            view_SearchForm.setText("");
        }
        else
        {
            //jika kosong, show search button & hide clear button
            view_ClearButton.setVisibility(View.GONE);
            view_SearchButton.setVisibility(View.VISIBLE);

            //dan refresh data
            CreateSearchRequest();
        }
    }
}
