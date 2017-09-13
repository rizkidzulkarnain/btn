package com.mitkoindo.smartcollection.module.dashboard;

import android.content.Intent;
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
import com.mitkoindo.smartcollection.adapter.StaffAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.Staff;

public class StaffDashboardSelectorActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //refresher
    private SwipeRefreshLayout view_Refresher;

    //recyclerview
    private RecyclerView view_Recycler;

    //progress bar
    private ProgressBar view_ProgressBar;

    //indicator buat load page baru
    private ProgressBar view_PageLoadIndicator;

    //alert text
    private TextView view_Alert;

    //search form
    private EditText view_SearchForm;

    //search button
    private ImageView view_SearchButton;

    //clear button
    private ImageView view_ClearButton;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token;
    private String authToken;

    //user id
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter staff
    private StaffAdapter staffAdapter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard_selector);

        //setup
        GetViews();
        SetupTransaction();
        AttachAdapter();
        SetupListener();
    }

    //get views
    private void GetViews()
    {
        view_Refresher = findViewById(R.id.StaffSelector_Refresher);
        view_Recycler = findViewById(R.id.StaffSelector_List);
        view_ProgressBar = findViewById(R.id.StaffSelector_ProgressBar);
        view_Alert = findViewById(R.id.StaffSelector_AlertView);
        view_SearchForm = findViewById(R.id.StaffSelector_SearchForm);
        view_SearchButton = findViewById(R.id.StaffSelector_SearchButton);
        view_ClearButton = findViewById(R.id.StaffSelector_ClearButton);
        view_PageLoadIndicator = findViewById(R.id.StaffSelector_PageLoadingIndicator);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //load user id
        userID = ResourceLoader.LoadUserID(this);
    }

    //setup listener
    private void SetupListener()
    {
        //set listener pada refresh
        view_Refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                view_Refresher.setRefreshing(false);
                staffAdapter.LoadInitialData();
            }
        });

        //add listener pada search
        view_SearchForm.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    //Handle search
                    view_SearchButton.setVisibility(View.GONE);
                    view_ClearButton.setVisibility(View.VISIBLE);
                    staffAdapter.CreateSearchRequest(view_SearchForm.getText().toString());

                    return true;
                }

                return false;
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_StaffDashboardSelector_Back(View view)
    {
        finish();
    }

    //handle search button
    public void HandleInput_StaffDashboardSelector_Search(View view)
    {
        //Handle search
        view_SearchButton.setVisibility(View.GONE);
        view_ClearButton.setVisibility(View.VISIBLE);
        staffAdapter.CreateSearchRequest(view_SearchForm.getText().toString());
    }

    //handle clear button
    public void HandleInput_StaffDashboardSelector_Clear(View view)
    {
        //clear search
        String searchtext = view_SearchForm.getText().toString();
        if (searchtext.isEmpty())
        {
            //clear search query
            view_SearchButton.setVisibility(View.VISIBLE);
            view_ClearButton.setVisibility(View.GONE);
            staffAdapter.CreateSearchRequest("");
        }
        else
        {
            view_SearchForm.setText("");
        }
    }

    //open staff dashboard
    private void OpenStaffDashboard(int index)
    {
        //get current staff
        Staff currentStaff = staffAdapter.GetStaffAt(index);

        //pastikan staff nggak null
        if (currentStaff == null)
            return;

        //open intent
        Intent intent = new Intent(this, StaffDashboardActivity.class);
        intent.putExtra("StaffID", currentStaff.USERID);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    //  Attach adapter
    //----------------------------------------------------------------------------------------------
    private void AttachAdapter()
    {
        //create adapter
        staffAdapter = new StaffAdapter(this);

        //set property
        staffAdapter.SetTransactionData(baseURL, url_DataSP, authToken, userID);
        staffAdapter.SetViews(view_ProgressBar, view_PageLoadIndicator, view_Recycler, view_Alert);

        //add listener
        staffAdapter.setClickListener(new ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                OpenStaffDashboard(position);
            }
        });

        //load data
        staffAdapter.LoadInitialData();

        //attach adapter ke listview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_Recycler.setLayoutManager(layoutManager);
        view_Recycler.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_Recycler.addItemDecoration(dividerItemDecoration);
        view_Recycler.setAdapter(staffAdapter);
    }
}
