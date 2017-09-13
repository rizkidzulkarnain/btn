package com.mitkoindo.smartcollection.module.laporan;


import android.content.Intent;
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
import com.mitkoindo.smartcollection.adapter.StaffAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.RecyclerViewHelper;
import com.mitkoindo.smartcollection.objectdata.Staff;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupervisorArchiveFragment extends Fragment
{
    public SupervisorArchiveFragment()
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
    private RecyclerView view_Recycler;

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
    //staff adapter
    private StaffAdapter staffAdapter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_supervisor_archive, container, false);
        GetViews(thisView);
        SetupListener();
        AttachAdapter();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        form_Search = thisView.findViewById(R.id.ArchiveFragment_SearchForm);
        button_Search = thisView.findViewById(R.id.ArchiveFragment_SearchButton);
        button_Clear = thisView.findViewById(R.id.ArchiveFragment_ClearButton);
        view_Recycler = thisView.findViewById(R.id.ArchiveFragment_List);
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
                    button_Search.setVisibility(View.GONE);
                    button_Clear.setVisibility(View.VISIBLE);
                    staffAdapter.CreateSearchRequest(form_Search.getText().toString());

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
                staffAdapter.LoadInitialData();
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
    }

    //attach adapter
    private void AttachAdapter()
    {
        //create adapter
        staffAdapter = new StaffAdapter(getActivity());

        //set property
        staffAdapter.SetTransactionData(baseURL, url_DataSP, authToken, userID);
        staffAdapter.SetViews(view_ProgressBar, view_PageLoadIndicator, view_Recycler, view_Alert);

        //add listener
        staffAdapter.setClickListener(new ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                OpenArchiveActivity(position);
            }
        });

        //load data
        staffAdapter.LoadInitialData();

        //attach adapter ke listview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        view_Recycler.setLayoutManager(layoutManager);
        view_Recycler.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_Recycler.addItemDecoration(dividerItemDecoration);
        view_Recycler.setAdapter(staffAdapter);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    private void HandleInput_ArchiveFragment_Search()
    {
        //Handle search
        button_Search.setVisibility(View.GONE);
        button_Clear.setVisibility(View.VISIBLE);
        staffAdapter.CreateSearchRequest(form_Search.getText().toString());
    }

    private void HandleInput_ArchiveFragment_Clear()
    {
        //clear query
        String query = form_Search.getText().toString();

        //cek apakah query kosong atau tidak
        if (query.isEmpty())
        {
            //refresh list
            staffAdapter.LoadInitialData();

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

    //open archive activity untuk staff yang dimaksud
    private void OpenArchiveActivity(int index)
    {
        //get staff
        Staff currentStaff = staffAdapter.GetStaffAt(index);

        //pastikan staff nggak null
        if (currentStaff == null)
            return;

        //create intent
        Intent intent = new Intent(getActivity(), ArchiveActivity.class);
        intent.putExtra("StaffName", currentStaff.FULL_NAME);
        intent.putExtra("StaffID", currentStaff.USERID);
        startActivity(intent);
    }
}
