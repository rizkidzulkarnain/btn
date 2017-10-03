package com.mitkoindo.smartcollection.module.dashboard;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.CommonTabAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;

import java.util.ArrayList;

public class StaffDashboardActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //tablayout
    private TabLayout view_Tablayout;

    //viewpager
    private ViewPager view_ViewPager;

    //option menu
    private ImageView button_OptionMenu;

    //staff name
    private TextView view_StaffName;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Fragments
    //----------------------------------------------------------------------------------------------
    private DashboardKunjunganFragment dashboardKunjunganFragment;
    private DashboardPenyelesaianFragment dashboardPenyelesaianFragment;
    private DashboardPTPFragment dashboardPTPFragment;
    private DashboardNPLFragment dashboardNPLFragment;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        //Setup
        GetViews();
        SetupTransaction();
        SetupViews();
    }

    //get views
    private void GetViews()
    {
        //get tab component
        view_Tablayout = findViewById(R.id.DashboardActivity_Tab);
        view_ViewPager = findViewById(R.id.DashboardActivity_ViewPager);
        view_StaffName = findViewById(R.id.DashboardStaff_StaffName);
        button_OptionMenu = findViewById(R.id.DashboardActivity_OptionMenu);
    }

    //setup views
    private void SetupViews()
    {
        //dashboard mode
        String DASHBOARDMODE_CURRENT = getString(R.string.dashboardKunjungan_Mode_Today);
        String DASHBOARDMODE_MONTH = getString(R.string.dashboardKunjungan_Mode_Monthly);
        String dashboardPenyelesaian_Current = getString(R.string.dashboardPenyelesaian_Mode_Today);
        String dashboardPenyelesaian_Month = getString(R.string.dashboardPenyelesaian_Mode_Monthly);
        String dashboardPTP_Current = getString(R.string.dashboardPTP_Mode_Today);
        String dashboardPTP_Month = getString(R.string.dashboardPTP_Mode_Monthly);

        //create fragment titles
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_Kunjungan));
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_Penyelesaian));
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_PTP));

        //create fragments
        dashboardKunjunganFragment = new DashboardKunjunganFragment();
        dashboardKunjunganFragment.SetupTransactionProperties(baseURL, url_DataSP, authToken, userID);
        dashboardKunjunganFragment.SetDashboardMode(DASHBOARDMODE_CURRENT, DASHBOARDMODE_MONTH);

        //create penyelesaian fragment
        dashboardPenyelesaianFragment = new DashboardPenyelesaianFragment();
        dashboardPenyelesaianFragment.SetupTransactionProperties(baseURL, url_DataSP, authToken, userID);
        dashboardPenyelesaianFragment.SetDashboardMode(dashboardPenyelesaian_Current, dashboardPenyelesaian_Month);

        //create ptp fragment
        dashboardPTPFragment = new DashboardPTPFragment();
        dashboardPTPFragment.SetupTransactionProperties(baseURL, url_DataSP, authToken, userID);
        dashboardPTPFragment.SetDashboardMode(dashboardPTP_Current, dashboardPTP_Month);

        //add fragments
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(dashboardKunjunganFragment);
        fragments.add(dashboardPenyelesaianFragment);
        fragments.add(dashboardPTPFragment);

        //create tab adapter
        CommonTabAdapter dashboardTabAdapter = new CommonTabAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

        //set adapter to tab
        view_ViewPager.setAdapter(dashboardTabAdapter);
        view_Tablayout.setupWithViewPager(view_ViewPager);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        userID = bundle.getString("StaffID");

        String staffName = bundle.getString("StaffName");
        view_StaffName.setText(staffName);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_DashboardStaff_Back(View view)
    {
        finish();
    }

}
