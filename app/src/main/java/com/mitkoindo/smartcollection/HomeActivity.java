package com.mitkoindo.smartcollection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mitkoindo.smartcollection.adapter.HomeMenuAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.HomeMenu;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //Recycler view
    private RecyclerView view_HomeMenu;

    //user name holder
    private TextView view_UserName;

    //user id holder
    private TextView view_UserID;

    //user group holder
    private TextView view_UserGroup;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //home menu
    private ArrayList<HomeMenu> homeMenus;

    //adapter menu
    private HomeMenuAdapter homeMenuAdapter;

    public static Intent instantiateClearTask(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

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
        view_HomeMenu = findViewById(R.id.HomeActivity_MenuGrid);
        view_UserName = findViewById(R.id.HomeActivity_UserName);
        view_UserID = findViewById(R.id.HomeActivity_UserID);
        view_UserGroup = findViewById(R.id.HomeActivity_UserGroup);
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

            //add bitmap tergantung judul menu
            newHomeMenu.Icon = ResourceLoader.LoadMenuIcon(this, newHomeMenu.Name);

            homeMenus.add(newHomeMenu);
        }

        //create menu
        homeMenuAdapter = new HomeMenuAdapter(this, homeMenus);

        //set click listener
        homeMenuAdapter.setClickListener(new ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                HandleInput_Home_SelectMenu(position);
            }
        });

        //attach adapter
        int numberOfColumns = 3;
        view_HomeMenu.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        view_HomeMenu.setAdapter(homeMenuAdapter);

        //set username
        String key_UserName = getString(R.string.SharedPreferenceKey_NamaPetugas);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value_UserName = sharedPreferences.getString(key_UserName, "");
        view_UserName.setText(value_UserName);

        //set user ID
        String key_UserID = getString(R.string.SharedPreferenceKey_UserID);
        String value_UserID = sharedPreferences.getString(key_UserID, "");
        view_UserID.setText(value_UserID);

        //set user group
        String key_UserGroup = getString(R.string.SharedPreferenceKey_UserGroup);
        String value_UserGroup = sharedPreferences.getString(key_UserGroup, "");
        view_UserGroup.setText(value_UserGroup);
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
    }

    //handle logout
    public void HandleInput_Home_Logout(View view)
    {
        //Confirm apakah user benar2 mau logout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Home_Alert_Logout_Message);

        //handle input
        builder.setNegativeButton(R.string.Text_Tidak, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing & dismiss the alertdialog
            }
        });
        builder.setPositiveButton(R.string.Text_Ya, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //delete auth token & return to login screen
                LogoutUser();
            }
        });

        //show alert dialog
        AlertDialog confimationPopup = builder.create();
        confimationPopup.show();
    }

    //handle select menu
    private void HandleInput_Home_SelectMenu(int menuPos)
    {
        //get selected menu name
        String selectedMenuName = homeMenuAdapter.getCurrentMenu(menuPos);

        //open corresponding menu
        Intent intent = null;
        switch (selectedMenuName)
        {
            case "Penugasan" :
                intent = new Intent(this, ListDebiturActivity.class);
                break;
            default:break;
        }

        if (intent != null)
            startActivity(intent);

        /*int x = 0;
        int y = x + 1;

        //test langsung buka histori tindakan
        Intent intent = new Intent(this, HistoriTindakanActivity.class);
        startActivity(intent);*/
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulate data
    //----------------------------------------------------------------------------------------------
    //handle logout
    private void LogoutUser()
    {
        //get key to auth token
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);

        //delete auth token
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_AuthToken, "");
        editor.apply();

        //return to login screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
