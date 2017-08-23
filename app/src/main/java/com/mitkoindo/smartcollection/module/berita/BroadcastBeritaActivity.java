package com.mitkoindo.smartcollection.module.berita;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mitkoindo.smartcollection.R;

public class BroadcastBeritaActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_berita);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //open broadcast popup
    public void HandleInput_BroadcastBerita_OpenBroadcastPopup(View view)
    {
        //get inflater
        LayoutInflater inflater = getLayoutInflater();

        //inflate broadcast popup
        View broadcastPopup = inflater.inflate(R.layout.popup_broadcastberita, null, false);

        //setup dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set views
        builder.setView(broadcastPopup);

        //set properties
        builder.setCancelable(false);

        //create alertdialog
        AlertDialog alertDialog = builder.create();

        //show dialog
        alertDialog.show();
    }
}
