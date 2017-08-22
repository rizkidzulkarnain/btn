package com.mitkoindo.smartcollection.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Erlangga on 22/08/2017.
 */

public class DashboardTabAdapter extends FragmentPagerAdapter
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //List fragment dalam adapter ini
    private ArrayList<Fragment> fragments;

    //list of fragment title
    private ArrayList<String> fragmentTitles;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    public DashboardTabAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> fragmentTitles)
    {
        super(fm);

        //save fragments & titles
        this.fragments = fragments;
        this.fragmentTitles = fragmentTitles;
    }

    @Override
    public Fragment getItem(int position)
    {
        //pastikan tidak throw out of bounds exception
        if (position >= getCount())
            return null;

        //return fragment at index
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        //pastikan tidak throw out of bounds exception
        if (position >= fragmentTitles.size())
            return "";

        //return fragment title
        return fragmentTitles.get(position);
    }

    //add new fragment
    public void AddFragment(Fragment newFragment)
    {
        //add fragment ke list
        fragments.add(newFragment);
    }
}
