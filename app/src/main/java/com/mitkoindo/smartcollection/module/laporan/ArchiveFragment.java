package com.mitkoindo.smartcollection.module.laporan;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.mitkoindo.smartcollection.adapter.DebiturArchiveAdapter;
import com.mitkoindo.smartcollection.helper.RecyclerViewHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchiveFragment extends Fragment
{
    public ArchiveFragment()
    {
        // Required empty public constructor
    }

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //search form
    private EditText form_Search;

    //search button
    private ImageView button_Search;

    //clear button
    private ImageView button_Clear;

    //list of debitur
    private RecyclerView view_DebiturList;

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
    //base url dan url yang digunakan
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter archive
    private DebiturArchiveAdapter debiturArchiveAdapter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_archive, container, false);
        GetViews(thisView);
        SetupListener();
        CreateGetDebiturRequest();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        form_Search = thisView.findViewById(R.id.ArchiveFragment_SearchForm);
        button_Search = thisView.findViewById(R.id.ArchiveFragment_SearchButton);
        button_Clear = thisView.findViewById(R.id.ArchiveFragment_ClearButton);
        view_DebiturList = thisView.findViewById(R.id.ArchiveFragment_List);
        view_PageLoadIndicator = thisView.findViewById(R.id.ArchiveFragment_PageLoadingIndicator);
        view_ProgressBar = thisView.findViewById(R.id.ArchiveFragment_ProgressBar);
        view_Alert = thisView.findViewById(R.id.ArchiveFragment_AlertText);
        view_Refresher = thisView.findViewById(R.id.ArchiveFragment_SwipeRefresh);
    }

    //set transaction data
    public void SetTransactionData(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //setup listener
    private void SetupListener()
    {
        //add listener to search
        form_Search.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    //Handle search
                    CreateSearchRequest();

                    return true;
                }

                return false;
            }
        });

        //add listener to swipe refresh
        view_Refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                view_Refresher.setRefreshing(false);
                debiturArchiveAdapter.LoadInitialDebiturData();
            }
        });

        //add listener to search button
        button_Search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HandleInput_ArchiveFragment_Search();
            }
        });

        //add listener to clear button
        button_Clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HandleInput_ArchiveFragment_Clear();
            }
        });

        //add scroll listener to list
        view_DebiturList.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                //pastikan adapter tidak null
                if (debiturArchiveAdapter == null)
                    return;

                //cek apakah sudah sampai item terakhir
                if (RecyclerViewHelper.isLastItemDisplaying(view_DebiturList))
                {
                    //jika iya, load new page
                    debiturArchiveAdapter.CreateLoadNewPageRequest();
                }
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get debitur
    //----------------------------------------------------------------------------------------------
    private void CreateGetDebiturRequest()
    {
        //create debitur adapter
        debiturArchiveAdapter = new DebiturArchiveAdapter(getActivity());

        //set transaksi
        debiturArchiveAdapter.SetTransactionData(baseURL, url_DataSP, authToken, userID);

        //set views
        debiturArchiveAdapter.SetViews(view_ProgressBar, view_PageLoadIndicator, view_DebiturList, view_Alert);

        //create request buat get debitur archive
        debiturArchiveAdapter.LoadInitialDebiturData();

        //attach adapter ke listview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        view_DebiturList.setLayoutManager(layoutManager);
        view_DebiturList.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_DebiturList.addItemDecoration(dividerItemDecoration);
        view_DebiturList.setAdapter(debiturArchiveAdapter);
    }

    //create request buat search
    private void CreateSearchRequest()
    {
        //get query
        String searchQuery = form_Search.getText().toString();

        debiturArchiveAdapter.SearchDebitur(searchQuery);

        //hide search button, show clear button
        button_Search.setVisibility(View.GONE);
        button_Clear.setVisibility(View.VISIBLE);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle search
    private void HandleInput_ArchiveFragment_Search()
    {
        CreateSearchRequest();
    }

    //handle clear
    private void HandleInput_ArchiveFragment_Clear()
    {
        //clear query
        String query = form_Search.getText().toString();

        //cek apakah query kosong atau tidak
        if (query.isEmpty())
        {
            //refresh list
            debiturArchiveAdapter.LoadInitialDebiturData();

            //hide clear button, show search button
            button_Search.setVisibility(View.VISIBLE);
            button_Clear.setVisibility(View.GONE);
        }
        else
        {
            //clear query
            form_Search.setText("");
        }
    }
}
