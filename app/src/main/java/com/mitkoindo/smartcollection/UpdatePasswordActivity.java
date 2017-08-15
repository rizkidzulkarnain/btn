package com.mitkoindo.smartcollection;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class UpdatePasswordActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //forms untuk ganti password
    private EditText form_OldPassword;
    private EditText form_NewPassword;
    private EditText form_ConfirmPassword;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        //setup
        GetViews();
    }

    //get views
    private void GetViews()
    {
        //get reference ke forms
        form_OldPassword = (EditText)findViewById(R.id.UpdatePasswordActivity_OldPassword);
        form_NewPassword = (EditText)findViewById(R.id.UpdatePasswordActivity_NewPassword);
        form_ConfirmPassword = (EditText)findViewById(R.id.UpdatePasswordActivity_ConfirmPassword);

        //setup toolbar
        /*ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
        {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(R.string.UpdatePassword_PageTitle);
        }*/
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_UpdatePassword_Back(View view)
    {
        //test open login page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home :
                //test open login page
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
