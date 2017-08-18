package com.mitkoindo.smartcollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //checkbox buat remember password
    private Switch view_RememberMe;

    //username & password field
    private EditText form_Username;
    private EditText form_Password;

    //text dari form username & password
    private String text_Username;
    private String text_Password;

    //generic alert dialog
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url
    private String baseURL;
    private String url_Login;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GetViews();
        SetupURL();
    }

    //get views
    private void GetViews()
    {
        view_RememberMe = (Switch) findViewById(R.id.LoginActivity_RememberMe);

        //get form username & password
        form_Username = (EditText) findViewById(R.id.LoginActivity_Username);
        form_Password = (EditText) findViewById(R.id.LoginActivity_Password);

        //create alert dialog
        genericAlert = new GenericAlert(this);
    }

    //setup URL
    private void SetupURL()
    {
        baseURL = getString(R.string.BaseURL);
        url_Login = getString(R.string.URL_Login);
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
        /*Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();*/

        //test histori tindakan
        /*Intent intent = new Intent(this, HistoriTindakanActivity.class);
        startActivity(intent);
        finish();*/

        //get valuenya username & password
        text_Username = form_Username.getText().toString();
        text_Password = form_Password.getText().toString();

        //show loading alert
        genericAlert.ShowLoadingAlert();

        //execute request
        new SendLoginRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
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

    //----------------------------------------------------------------------------------------------
    //  Create login request
    //----------------------------------------------------------------------------------------------
    //create request
    private class SendLoginRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create request object
            JSONObject requestObject = CreateLoginRequestObject();

            //set url
            String usedURL = baseURL + url_Login;

            //create network connection
            NetworkConnection networkConnection = new NetworkConnection("", "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleLoginResult(s);
        }
    }

    //create login request
    private JSONObject CreateLoginRequestObject()
    {
        JSONObject requestObject = new JSONObject();
        try
        {
            requestObject.put("UserID", text_Username);
            requestObject.put("Password", text_Password);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestObject;
    }

    //handle login result
    private void HandleLoginResult(String resultString)
    {
        //dismiss loading alert
        genericAlert.Dismiss();

        try
        {
            //get access token
            JSONObject resultObject = new JSONObject(resultString);

            //get token
            String accessToken = resultObject.getString("AccessToken");

            //simpan access token di shared preference
            SaveToken(accessToken);

            //open home activity
            OpenHomeActivity();
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show alert
            genericAlert.DisplayAlert(resultString, "");
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Save token
    //----------------------------------------------------------------------------------------------
    private void SaveToken(String authToken)
    {
        //get preference name & key
        String authTokenKey = getString(R.string.SharedPreferenceKey_AccessToken);

        //save access token
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(authTokenKey, authToken);
        editor.apply();
    }

    //----------------------------------------------------------------------------------------------
    //  Open activity
    //----------------------------------------------------------------------------------------------
    //open home activity
    private void OpenHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
