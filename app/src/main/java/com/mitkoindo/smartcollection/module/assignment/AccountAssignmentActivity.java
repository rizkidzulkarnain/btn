package com.mitkoindo.smartcollection.module.assignment;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.AccountAssignmentAdapter;
import com.mitkoindo.smartcollection.adapter.CommonTabAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithFlag;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountAssignmentActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //alert dialog
    private GenericAlert genericAlert;

    //tablayout
    private TabLayout view_Tablayout;

    //view pager
    private ViewPager view_ViewPager;

    //fragments
    private AssignedDebiturFragment assignedDebiturFragment;
    private UnassignedDebiturFragment unassignedDebiturFragment;

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
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_assignment);

        //setup
        GetViews();
        SetupTransaction();
        SetupViews();
    }

    //get views
    private void GetViews()
    {
        genericAlert = new GenericAlert(this);

        //get tablayout dan view pager
        view_Tablayout = findViewById(R.id.AccountAssignmentLayout_TabLayout);
        view_ViewPager = findViewById(R.id.AccountAssignmentLayout_ViewPager);
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

    //setup views
    private void SetupViews()
    {
        //create fragment titles
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragmentTitles.add(getString(R.string.AccountAssignment_FragmentTitle_Unassigned));
        fragmentTitles.add(getString(R.string.AccountAssignment_FragmentTitle_Assigned));

        //create fragments
        assignedDebiturFragment = new AssignedDebiturFragment();
        unassignedDebiturFragment = new UnassignedDebiturFragment();

        //setup transaksi
        unassignedDebiturFragment.SetupTransactionProperty(baseURL, url_DataSP, authToken, userID);
        assignedDebiturFragment.SetupTransactionProperty(baseURL, url_DataSP, authToken, userID);

        //create fragment array
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(unassignedDebiturFragment);
        fragments.add(assignedDebiturFragment);

        //create tab adapter
        CommonTabAdapter assignmentAdapter = new CommonTabAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

        //attach fragment to layout
        view_ViewPager.setAdapter(assignmentAdapter);
        view_Tablayout.setupWithViewPager(view_ViewPager);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_AccountAssignment_Back(View view)
    {
        finish();
    }

    /*//handle assign button
    public void HandleInput_AccountAssignment_AssignButton(View view)
    {
        //get jumlah debitur yang diselect
        int selectedDebiturCount = accountAssignmentAdapter.GetSelectedDebiturCount();

        //jika tidak ada yang diselect, show warning
        if (selectedDebiturCount <= 0)
        {
            String message = getString(R.string.AccountAssignment_Alert_NoDebiturSelected);
            genericAlert.DisplayAlert(message, "");
            return;
        }
    }*/
}
