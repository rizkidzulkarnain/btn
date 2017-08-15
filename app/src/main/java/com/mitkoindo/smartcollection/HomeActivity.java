package com.mitkoindo.smartcollection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mitkoindo.smartcollection.adapter.HomeMenuAdapter;
import com.mitkoindo.smartcollection.objectdata.HomeMenu;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //Recycler view
    private RecyclerView view_HomeMenu;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //home menu
    private ArrayList<HomeMenu> homeMenus;

    //adapter menu
    private HomeMenuAdapter homeMenuAdapter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //setup
        GetViews();
        SetupViews();
    }

    //get views
    private void GetViews()
    {
        view_HomeMenu = (RecyclerView)findViewById(R.id.HomeActivity_MenuGrid);
    }

    //setup view
    private void SetupViews()
    {
        //load menu array
        String[] menuTitle = getResources().getStringArray(R.array.HomeMenu);

        //create menu object
        homeMenus = new ArrayList<>();
        for (int i = 0; i < menuTitle.length; i++)
        {
            HomeMenu newHomeMenu = new HomeMenu();
            newHomeMenu.Name = menuTitle[i];
            homeMenus.add(newHomeMenu);
        }

        //create menu
        homeMenuAdapter = new HomeMenuAdapter(this, homeMenus);

        //attach adapter
        int numberOfColumns = 3;
        view_HomeMenu.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        view_HomeMenu.setAdapter(homeMenuAdapter);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle input buat settings button
    public void HandleInput_Home_OpenSettings(View view)
    {
        //test open change password
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
        finish();
    }
}
