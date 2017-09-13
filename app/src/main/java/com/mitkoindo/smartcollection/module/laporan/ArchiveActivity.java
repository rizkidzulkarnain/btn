package com.mitkoindo.smartcollection.module.laporan;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.DebiturArchiveAdapter;
import com.mitkoindo.smartcollection.helper.RecyclerViewHelper;
import com.mitkoindo.smartcollection.helper.ResourceLoader;

public class ArchiveActivity extends AppCompatActivity
{
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

    //page title
    private TextView view_PageTitle;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        //setup
        GetViews();
        SetTransactionData();
        GetBundles();
        SetupListener();
        AttachAdapter();
    }

    //get views
    private void GetViews()
    {
        //title view
        view_PageTitle = findViewById(R.id.ArchiveActivity_Staffname);

        //fragment views
        View thisView = findViewById(R.id.ArchiveActivity_ArchiveFragment);
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
    public void SetTransactionData()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);
    }

    //get bundles
    private void GetBundles()
    {
        //get bundle
        Bundle bundle = getIntent().getExtras();

        //pastikan bundle tidak null
        if (bundle == null)
            return;

        //get data
        String staffName = bundle.getString("StaffName");
        this.userID = bundle.getString("StaffID");

        //set value
        view_PageTitle.setText(staffName);
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
                    HandleSearch();

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
                HandleSearch();
            }
        });

        //add listener to clear button
        button_Clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HandleClear();
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

    //attach adapter
    private void AttachAdapter()
    {
        //create debitur adapter
        debiturArchiveAdapter = new DebiturArchiveAdapter(this);

        //set transaksi
        debiturArchiveAdapter.SetTransactionData(baseURL, url_DataSP, authToken, userID);

        //set views
        debiturArchiveAdapter.SetViews(view_ProgressBar, view_PageLoadIndicator, view_DebiturList, view_Alert);

        //create request buat get debitur archive
        debiturArchiveAdapter.LoadInitialDebiturData();

        //attach adapter ke listview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_DebiturList.setLayoutManager(layoutManager);
        view_DebiturList.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_DebiturList.addItemDecoration(dividerItemDecoration);
        view_DebiturList.setAdapter(debiturArchiveAdapter);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_ArchiveActivity_Back(View view)
    {
        finish();
    }

    //handle search
    private void HandleSearch()
    {
        //show clear button, hide search button
        button_Clear.setVisibility(View.VISIBLE);
        button_Search.setVisibility(View.GONE);

        //create search request
        debiturArchiveAdapter.SearchDebitur(form_Search.getText().toString());
    }

    //handle clear
    private void HandleClear()
    {
        //get query
        String searchQuery = form_Search.getText().toString();

        //cek query kosong atau tidak
        if (searchQuery.isEmpty())
        {
            //jika kosong, clear search
            button_Clear.setVisibility(View.GONE);
            button_Search.setVisibility(View.VISIBLE);

            //create search request
            debiturArchiveAdapter.SearchDebitur("");
        }
        else
        {
            //clear query
            form_Search.setText("");
        }
    }
}
