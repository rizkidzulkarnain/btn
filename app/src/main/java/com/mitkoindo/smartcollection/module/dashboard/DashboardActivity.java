package com.mitkoindo.smartcollection.module.dashboard;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.CommonTabAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //tablayout
    private TabLayout view_Tablayout;

    //viewpager
    private ViewPager view_ViewPager;

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
        setContentView(R.layout.activity_dashboard);

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
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_NPL));

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

        //create npl dashboard (nanti cuma BC aja yang bisa akses)
        dashboardNPLFragment = new DashboardNPLFragment();
        dashboardNPLFragment.SetupTransactionProperties(baseURL, url_DataSP, authToken, userID);

        //add fragments
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(dashboardKunjunganFragment);
        fragments.add(dashboardPenyelesaianFragment);
        fragments.add(dashboardPTPFragment);
        fragments.add(dashboardNPLFragment);

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

        //load user id
        userID = ResourceLoader.LoadUserID(this);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_Dashboard_Back(View view)
    {
        finish();
    }
}
