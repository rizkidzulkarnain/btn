package com.mitkoindo.smartcollection;

import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mitkoindo.smartcollection.adapter.DrawerMenuAdapter;
import com.mitkoindo.smartcollection.fragments.NewsFragment;
import com.mitkoindo.smartcollection.objectdata.DrawerMenu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //menu view
    private RecyclerView view_DrawerMenuList;

    //drawer layout
    private DrawerLayout drawerLayout;

    //current menu index
    private int currentMenuIndex;

    //menu index
    private static final int MENU_HOME = 0;
    private static final int MENU_DASHBOARD = 1;
    private static final int MENU_NEWS = 2;
    private static final int MENU_INBOX = 3;
    private static final int MENU_CHAT = 4;
    private static final int MENU_PROCESS = 5;
    private static final int MENU_ARCHIVE = 6;
    private static final int MENU_GALLERY = 7;
    private static final int MENU_LOGOUT = 8;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup
        GetViews();
        SetupViews();
    }

    //get views
    private void GetViews()
    {
        //set current menu index
        currentMenuIndex = 0;

        view_DrawerMenuList = findViewById(R.id.MainActivity_MenuList);
        drawerLayout = findViewById(R.id.activity_Main);
    }

    //setup view
    private void SetupViews()
    {
        //create menu list
        ArrayList<DrawerMenu> drawerMenus = new ArrayList<>();
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_home_black_36dp, "Home", true));
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_dashboard_black_36dp, "Dashboard", false));
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_today_black_36dp, "Berita", false));
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_assignment_black_36dp, "Inbox", false));
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_chat_black_36dp, "Chat", false));
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_local_post_office_black_36dp, "Proses Kirim", false));
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_archive_black_36dp, "Arsip", false));
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_insert_photo_black_36dp, "Galeri Photo", true));
        drawerMenus.add(new DrawerMenu(this, R.drawable.ic_power_settings_new_black_36dp, "Logout", false));

        //create adapter
        DrawerMenuAdapter drawerMenuAdapter = new DrawerMenuAdapter(this, drawerMenus);

        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_DrawerMenuList.setLayoutManager(layoutManager);
        view_DrawerMenuList.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawermenu);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_DrawerMenuList.addItemDecoration(dividerItemDecoration);
        view_DrawerMenuList.setAdapter(drawerMenuAdapter);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //open drawer
    public void HandleInput_OpenDrawer(View view)
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    //open menu
    public void HandleInput_OpenSelectedMenu(int menuIndex)
    {
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);

        //cek apakah index menu sekarang sama dengan index yang baru dipilih
        if (currentMenuIndex == menuIndex)
            return;

        //assign index yang dipilih
        currentMenuIndex = menuIndex;

        //create fragment baru tergantung menu yang dipilih
        Fragment newFragment = null;
        switch (currentMenuIndex)
        {
            case MENU_HOME :
                break;
            case MENU_DASHBOARD :
                break;
            case MENU_NEWS :
                newFragment = new NewsFragment();
                break;
            case MENU_INBOX :
                break;
            case MENU_CHAT :
                break;
            case MENU_PROCESS :
                break;
            case MENU_ARCHIVE :
                break;
            case MENU_GALLERY :
                break;
            case MENU_LOGOUT :
                break;
            default:break;
        }

        //pastikan fragment baru tidak null
        if (newFragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.MainActivity_Content, newFragment).commit();
    }
}
