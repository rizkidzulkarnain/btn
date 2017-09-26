package com.mitkoindo.smartcollection.module.laporan;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.CommonTabAdapter;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.module.laporan.agenttracking.ListStaffDownlineFragment;
import com.mitkoindo.smartcollection.module.laporan.reportdistribusi.ReportDistribusiDebiturFragment;
import com.mitkoindo.smartcollection.module.laporan.reportdistribusi.ReportDistribusiStaffFragment;
import com.mitkoindo.smartcollection.module.laporan.staffproductivity.StaffProductivityLandscapeFragment;
import com.mitkoindo.smartcollection.module.laporan.staffproductivity.StaffProductivityPortraitFragment;

import java.util.ArrayList;

public class LaporanActivity extends BaseActivity
{
    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //fragments
    private ArchiveFragment fragment_Archive;
    private ReportDistribusiStaffFragment fragment_ReportDistribusiStaff;
    private StaffProductivityLandscapeFragment fragment_StaffProductivityLandscape;
    private StaffProductivityPortraitFragment fragment_StaffProductivityPortrait;
    private SupervisorArchiveFragment fragment_SupervisorArchive;

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
//        setContentView(R.layout.activity_laporan);

        //setup
        SetupTransaction();
        GetViews();

        SetTabDependingOnGroup();
        /*SetupTabs();*/
        /*SetupSupervisorTab();*/
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_laporan;
    }

    @Override
    protected void setupDataBinding(View contentView) {

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
        //get tab
        view_Tabs = findViewById(R.id.Laporan_Tab);

        //get pager
        view_ViewPager = findViewById(R.id.Laporan_ViewPager);
    }

    //set tab tergantung user group
    private void SetTabDependingOnGroup()
    {
        //cek group ID user ini
        String userGroupID = ResourceLoader.LoadGroupID(this);

        //get user group
        final String userGroup_Staff1 = getString(R.string.UserGroup_Staff1);
        final String userGroup_Staff2 = getString(R.string.UserGroup_Staff2);
        final String userGroup_Staff3 = getString(R.string.UserGroup_Staff3);
        
        //cek tipe user
        if (userGroupID.equals(userGroup_Staff1) || userGroupID.equals(userGroup_Staff2) || userGroupID.equals(userGroup_Staff3))
            SetupTabs();
        else
            SetupSupervisorTab();

    }

    //setup tabs
    private void SetupTabs()
    {
        //create fragments
        fragment_Archive = new ArchiveFragment();
        fragment_StaffProductivityPortrait = StaffProductivityPortraitFragment.getInstance(ResourceLoader.LoadUserID(this));

        //set property transaksi ke fragment
        fragment_Archive.SetTransactionData(baseURL, url_DataSP, authToken, userID);

        //create fragment titles
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragmentTitles.add(getString(R.string.Laporan_Tab_Arsip));
        fragmentTitles.add(getString(R.string.Laporan_Tab_StaffProductivity));

        //create fragment list
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment_Archive);
        fragments.add(fragment_StaffProductivityPortrait);

        //set fragment ke views
        //create tab adapter
        CommonTabAdapter dashboardTabAdapter = new CommonTabAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

        //set adapter to tab
        view_ViewPager.setOffscreenPageLimit(2);
        view_ViewPager.setAdapter(dashboardTabAdapter);
        view_Tabs.setupWithViewPager(view_ViewPager);
    }

    //setup tab buat supervisor
    private void SetupSupervisorTab()
    {
        //create fragments
        fragment_SupervisorArchive = new SupervisorArchiveFragment();
        fragment_ReportDistribusiStaff = ReportDistribusiStaffFragment.getInstance();
        fragment_StaffProductivityLandscape = (StaffProductivityLandscapeFragment) StaffProductivityLandscapeFragment.getInstance(ResourceLoader.LoadUserID(this));

        //set property transaksi ke fragment
        fragment_SupervisorArchive.SetTransactionData(baseURL, url_DataSP, authToken, userID);

        //create fragment titles
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragmentTitles.add(getString(R.string.Laporan_Tab_Arsip));
        fragmentTitles.add(getString(R.string.Laporan_Tab_Monitoring));
        fragmentTitles.add(getString(R.string.Laporan_Tab_StaffProductivity));

        //create fragment list
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment_SupervisorArchive);
        fragments.add(fragment_ReportDistribusiStaff);
        fragments.add(fragment_StaffProductivityLandscape);

        //set fragment ke views
        //create tab adapter
        CommonTabAdapter dashboardTabAdapter = new CommonTabAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

        //set adapter to tab
        view_ViewPager.setOffscreenPageLimit(3);
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
