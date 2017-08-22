package com.mitkoindo.smartcollection.module.berita;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.BeritaAdapter;

public class BeritaActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //list of berita
    private RecyclerView view_ListBerita;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter for berita
    private BeritaAdapter beritaAdapter;

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
    }

    //get reference ke view
    private void GetViews()
    {
        //get list berita
        view_ListBerita = findViewById(R.id.BeritaActivity_ListBerita);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_Berita_Back(View view)
    {
        finish();
    }
}
