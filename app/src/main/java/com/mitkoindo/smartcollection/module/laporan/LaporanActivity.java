package com.mitkoindo.smartcollection.module.laporan;

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

public class LaporanActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //fragments
    private ArchiveFragment fragment_Archive;
    private AgentTrackingFragment fragment_AgentTracking;
    private DebiturMonitorFragment fragment_MonitorDebitur;
    private StaffProductivityFragment fragment_StaffProductivity;

    //tab layout
    private TabLayout view_Tabs;

    //viewpager
    private ViewPager view_ViewPager;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url buat get berita
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        //setup
        SetupTransaction();
        GetViews();
        SetupTabs();
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);
    }

    //get views
    private void GetViews()
    {
        //create fragments
        fragment_Archive = new ArchiveFragment();
        fragment_AgentTracking = new AgentTrackingFragment();
        fragment_MonitorDebitur = new DebiturMonitorFragment();
        fragment_StaffProductivity = new StaffProductivityFragment();

        //set property transaksi ke fragment
        fragment_Archive.SetTransactionData(baseURL, url_DataSP, authToken, userID);

        //get tab
        view_Tabs = findViewById(R.id.Laporan_Tab);

        //get pager
        view_ViewPager = findViewById(R.id.Laporan_ViewPager);
    }

    //setup tabs
    private void SetupTabs()
    {
        //create fragment titles
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragmentTitles.add(getString(R.string.Laporan_Tab_Arsip));
        fragmentTitles.add(getString(R.string.Laporan_Tab_AgentTracking));
        fragmentTitles.add(getString(R.string.Laporan_Tab_Monitoring));
        fragmentTitles.add(getString(R.string.Laporan_Tab_StaffProductivity));

        //create fragment list
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment_Archive);
        fragments.add(fragment_AgentTracking);
        fragments.add(fragment_MonitorDebitur);
        fragments.add(fragment_StaffProductivity);

        //set fragment ke views
        //create tab adapter
        CommonTabAdapter dashboardTabAdapter = new CommonTabAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

        //set adapter to tab
        view_ViewPager.setAdapter(dashboardTabAdapter);
        view_Tabs.setupWithViewPager(view_ViewPager);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_Laporan_Back(View view)
    {
        finish();
    }
}
