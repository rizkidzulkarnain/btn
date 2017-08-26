package com.mitkoindo.smartcollection.module.berita;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.BeritaAdapter;
import com.mitkoindo.smartcollection.adapter.BeritaGlobalAdapter;
import com.mitkoindo.smartcollection.adapter.CommonTabAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.GlobalNews;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BeritaActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //list of berita
    /*private RecyclerView view_ListBerita;*/

    //generic alert
    private GenericAlert genericAlert;

    //tablayout & view pager
    private TabLayout view_TabLayout;
    private ViewPager view_ViewPager;

    //fragment yang hold data berita global & berita lokal
    private BeritaGlobalFragment beritaGlobalFragment;
    private BeritaGrupFragment beritaGrupFragment;

    //Button untuk create broadcast
    private View view_CreateBroadcast;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url buat get berita
    private String baseURL;
    private String url_GetBerita;

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
        setContentView(R.layout.activity_berita);

        //setup
        GetViews();
        SetupTransaction();
        SetupViews();
    }

    //get reference ke view
    private void GetViews()
    {
        //get list berita
        /*view_ListBerita = findViewById(R.id.BeritaActivity_ListBerita);*/

        //create generic alert
        genericAlert = new GenericAlert(this);

        //get tablayout & viewpager
        view_TabLayout = findViewById(R.id.BeritaActivity_Tab);
        view_ViewPager = findViewById(R.id.BeritaActivity_ViewPager);

        //get button buat create broadcast, akan dihide kalo user di level petugas
        view_CreateBroadcast = findViewById(R.id.BeritaActivity_CreateBroadcast);
    }

    //set transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_GetBerita = getString(R.string.URL_Data_View);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);
    }

    //setup views
    private void SetupViews()
    {
        //create fragment titles
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragmentTitles.add(getString(R.string.Berita_Fragment_Global_Title));
        fragmentTitles.add(getString(R.string.Berita_Fragment_Group_Title));

        //create fragments
        beritaGlobalFragment = new BeritaGlobalFragment();
        beritaGrupFragment = new BeritaGrupFragment();

        //set transaction property
        beritaGlobalFragment.SetTransactionData(baseURL, url_GetBerita, authToken);
        beritaGrupFragment.SetTransactionData(baseURL, url_GetBerita, authToken, userID);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(beritaGlobalFragment);
        fragments.add(beritaGrupFragment);

        //set fragment ke views
        //create tab adapter
        CommonTabAdapter dashboardTabAdapter = new CommonTabAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

        //set adapter to tab
        view_ViewPager.setAdapter(dashboardTabAdapter);
        view_TabLayout.setupWithViewPager(view_ViewPager);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_Berita_Back(View view)
    {
        finish();
    }

    //open broadcast berita
    public void HandleInput_Berita_CreateBroadcast(View view)
    {
        Intent intent = new Intent(this, BroadcastBeritaActivity.class);
        startActivity(intent);
    }
}
