package com.mitkoindo.smartcollection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class LoginActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //checkbox buat remember password
    private Switch view_RememberMe;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GetViews();
    }

    //get views
    private void GetViews()
    {
        view_RememberMe = (Switch) findViewById(R.id.LoginActivity_RememberMe);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle input di login button
    public void HandleInput_Login_LoginButton(View view)
    {
        //test start main activity
        /*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*/

        //test start update password
        /*Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
        finish();*/

        //test home screen
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //toggle switch button
    public void HandleInput_Login_ToggleSwitch(View view)
    {
        //get switch state
        if (view_RememberMe.isChecked())
            view_RememberMe.setChecked(false);
        else
            view_RememberMe.setChecked(true);
    }
}
