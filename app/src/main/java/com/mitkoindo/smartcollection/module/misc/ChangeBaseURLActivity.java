package com.mitkoindo.smartcollection.module.misc;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.utilities.GenericAlert;

public class ChangeBaseURLActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //url form
    private EditText form_URL;

    //generic alert dialog
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //base url
    private String baseURL;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_base_url);

        //setup
        GetViews();
        SetupViews();
    }

    //get views
    private void GetViews()
    {
        //get form
        form_URL = findViewById(R.id.ChangeBaseURL_Form);

        //create generic alert
        genericAlert = new GenericAlert(this);

        //setup action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            //set title
            actionBar.setTitle(R.string.ChangeBaseURL_Title);

            //set back button
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //setup
    private void SetupViews()
    {
        //load base url
        baseURL = ResourceLoader.LoadBaseURL(this);

        //set base url to view
        form_URL.setText(baseURL);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home :
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return false;
        }
    }

    //change base url
    public void HandleInput_ChangeURL_Change(View view)
    {
        //get url from textview
        baseURL = form_URL.getText().toString();

        //save base url
        SaveBaseURL();

        //show alert bahwa url berhasil diubah
        String message = getString(R.string.ChangeBaseURL_UrlChanged);
        genericAlert.DisplayAlert(message, "");
    }

    //reset url
    public void HandleInput_ChangeURL_Reset(View view)
    {
        //reload base url
        baseURL = getString(R.string.BaseURL);

        //reset juga viewnya
        form_URL.setText(baseURL);

        //save base url
        SaveBaseURL();

        //show alert bahwa url telah direset
        String message = getString(R.string.ChangeBaseURL_UrlReset);
        genericAlert.DisplayAlert(message, "");
    }

    //----------------------------------------------------------------------------------------------
    //  Handle data
    //----------------------------------------------------------------------------------------------
    //save base url to shared preference
    private void SaveBaseURL()
    {
        //get shared preference
        String key_BaseURL = getString(R.string.SharedPreferenceKey_BaseURL);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //save base url value
        editor.putString(key_BaseURL, baseURL);
        editor.apply();
    }
}
