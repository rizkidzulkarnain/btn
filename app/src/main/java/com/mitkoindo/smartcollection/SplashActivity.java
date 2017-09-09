package com.mitkoindo.smartcollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mitkoindo.smartcollection.module.chat.ChatListActivity;
import com.mitkoindo.smartcollection.module.chat.ChatWindowActivity;


public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //------------------------------------------------------------------------------------------
        //  Real start point
        //------------------------------------------------------------------------------------------
        //create intent
        Intent intent;

        //cek keberadaan auth token
        if (AuthTokenExist()) {
            //jika ada, langsung buka home screen
            intent = new Intent(this, HomeActivity.class);
        } else {
            //jika tidak, buka login screen
            intent = new Intent(this, LoginActivity.class);
        }

        //start activity
        startActivity(intent);
        finish();

        //------------------------------------------------------------------------------------------
        //  Test start point
        //------------------------------------------------------------------------------------------
        /*Intent intent = new Intent(this, ChatWindowActivity.class);
        startActivity(intent);
        finish();*/
    }

    //cek apakah ada auth token atau tidak
    private boolean AuthTokenExist()
    {
        //get key shared preference
        String authTokenKey = getString(R.string.SharedPreferenceKey_AccessToken);

        //get shared preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String authToken = sharedPreferences.getString(authTokenKey, "");

        //cek ada / tidaknya auth token
        return !authToken.isEmpty();
    }
}
