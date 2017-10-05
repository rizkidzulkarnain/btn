package com.mitkoindo.smartcollection;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.LoginEncryption;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class UpdatePasswordActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //forms untuk ganti password
    private EditText form_OldPassword;
    private EditText form_NewPassword;
    private EditText form_ConfirmPassword;

    //generic alert
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //value form update password
    private String value_OldPassword;
    private String value_NewPassword;
    private String value_ConfirmPassword;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url untuk update password
    private String baseURL;
    private String url_UpdatePassword;

    //auth token
    private String authToken;

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
        SetupTransactionProperties();
    }

    //get views
    private void GetViews()
    {
        //get reference ke forms
        form_OldPassword = findViewById(R.id.UpdatePasswordActivity_OldPassword);
        form_NewPassword = findViewById(R.id.UpdatePasswordActivity_NewPassword);
        form_ConfirmPassword = findViewById(R.id.UpdatePasswordActivity_ConfirmPassword);

        //create alert
        genericAlert = new GenericAlert(this);
    }

    //setup url & auth token
    private void SetupTransactionProperties()
    {
        //get url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_UpdatePassword = getString(R.string.URL_UpdatePassword);

        //load auth token
        String authTokenKey = getString(R.string.SharedPreferenceKey_AccessToken);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        authToken = sharedPreferences.getString(authTokenKey, "");
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_UpdatePassword_Back(View view)
    {
        finish();
    }

    //handle submit button
    public void HandleInput_UpdatePassword_Submit(View view)
    {
        //get value from input
        value_OldPassword = form_OldPassword.getText().toString();
        value_NewPassword = form_NewPassword.getText().toString();
        value_ConfirmPassword = form_ConfirmPassword.getText().toString();

        //encrypt masing-masing value
        LoginEncryption loginEncryption = new LoginEncryption();
        try
        {
            value_OldPassword = loginEncryption.encryptWithDefaultKey(value_OldPassword);
            value_NewPassword = loginEncryption.encryptWithDefaultKey(value_NewPassword);
            value_ConfirmPassword = loginEncryption.encryptWithDefaultKey(value_ConfirmPassword);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }

        //show loading alert
        genericAlert.ShowLoadingAlert();

        //create request untuk update password
        new SendUpdatePasswordRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //----------------------------------------------------------------------------------------------
    //  Handle transaksi update password
    //----------------------------------------------------------------------------------------------
    private class SendUpdatePasswordRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create request object
            JSONObject requestObject = CreateUpdatePasswordRequestObject();

            //set url
            String usedURL = baseURL + url_UpdatePassword;

            //execute transaction
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPutRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleUpdatePasswordResult(s);
        }
    }

    //create request object
    private JSONObject CreateUpdatePasswordRequestObject()
    {
        //create object
        JSONObject requestObject = new JSONObject();

        try
        {
            //populate object
            requestObject.put("OldPassword", value_OldPassword);
            requestObject.put("NewPassword", value_NewPassword);
            requestObject.put("ConfirmNewPassword", value_ConfirmPassword);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestObject;
    }

    //handle result
    private void HandleUpdatePasswordResult(String resultString)
    {
        //dismiss loading alert
        genericAlert.Dismiss();

        //cek jika response code adalah 204
        if (resultString.equals("204"))
        {
            //jika resultnya 204, maka update password sukses
            //remove auth token dan
            //show alert bahwa password berhasil diganti, dan jika user press ok di alertnya,
            //access token akan dihapus, dan app akan kembali ke screen login
            DeleteAuthToken();
            ShowPasswordChangedAlert();
        }
        else if (resultString.equals("Bad Request"))
        {
            //show alert bahwa password kurang panjang
            String alertTitle = getString(R.string.Text_MohonMaaf);
            String alertMessage = getString(R.string.UpdatePassword_Alert_PanjangPasswordKurang);
            genericAlert.DisplayAlert(alertMessage, alertTitle);
        }

        /*try
        {
            //sementara ini cek dulu stringnya kosong atau nggak
            if (resultString.isEmpty())
            {
                //anggep udah bener
                String alertTitle = getString(R.string.Text_Success);
                String alertMessage = getString(R.string.UpdatePassword_Alert_UpdatePasswordSuccess);

                genericAlert.DisplayAlert(alertMessage, alertTitle);
                return;
            }

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
        }*/
    }

    //Delete auth token
    private void DeleteAuthToken()
    {
        //get preference name & key
        String authTokenKey = getString(R.string.SharedPreferenceKey_AccessToken);

        //save access token
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(authTokenKey, "");
        editor.apply();
    }

    //show password changed alert
    private void ShowPasswordChangedAlert()
    {
        //setup builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        //set title & message
        builder.setTitle(R.string.Text_Success);
        builder.setMessage(R.string.UpdatePassword_Alert_UpdatePasswordSuccess);

        //set listener
        builder.setPositiveButton(R.string.Text_OK, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                ReturnUserToLoginScreen();
            }
        });

        //show alert
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //return user ke login screen
    private void ReturnUserToLoginScreen()
    {
        //start login screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        //remove all previous screen
        finishAffinity();

    }
}
