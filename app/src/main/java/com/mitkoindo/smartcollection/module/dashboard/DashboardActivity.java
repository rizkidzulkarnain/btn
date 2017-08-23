package com.mitkoindo.smartcollection.module.dashboard;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.DashboardTabAdapter;

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
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //get views
        GetViews();
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
        //create fragment titles
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_Kunjungan));
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_Penyelesaian));
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_PTP));
        /*fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_NPL));*/

        //create fragments
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new DashboardKunjunganFragment());
        fragments.add(new DashboardPenyelesaianFragment());
        fragments.add(new DashboardPTPFragment());
        fragments.add(new DashboardNPLFragment());

        //create tab adapter
        DashboardTabAdapter dashboardTabAdapter = new DashboardTabAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

        //set adapter to tab
        view_ViewPager.setAdapter(dashboardTabAdapter);
        view_Tablayout.setupWithViewPager(view_ViewPager);
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
