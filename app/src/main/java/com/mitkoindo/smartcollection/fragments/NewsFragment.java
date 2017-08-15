package com.mitkoindo.smartcollection.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.NewsAdapter;
import com.mitkoindo.smartcollection.objectdata.News;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment
{
    public NewsFragment()
    {
        // Required empty public constructor
    }

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //list of news
    private RecyclerView view_NewsList;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //news adapter
    private NewsAdapter newsAdapter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_news, container, false);
        GetViews(thisView);
        SetupViews();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        view_NewsList = thisView.findViewById(R.id.NewsFragment_newsList);
    }

    //setup views
    private void SetupViews()
    {
        //create dummy news item
        ArrayList<News> all_News = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            all_News.add(new News());
        }

        //create adapter
        newsAdapter = new NewsAdapter(getActivity(), all_News);

        //attach adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        view_NewsList.setLayoutManager(layoutManager);
        view_NewsList.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_NewsList.addItemDecoration(dividerItemDecoration);
        view_NewsList.setAdapter(newsAdapter);
    }
}
