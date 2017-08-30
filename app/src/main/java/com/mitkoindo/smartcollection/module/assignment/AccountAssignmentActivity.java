package com.mitkoindo.smartcollection.module.assignment;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.AccountAssignmentAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithFlag;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountAssignmentActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //Search button
    private ImageView view_SearchButton;

    //Listview
    private RecyclerView view_DebiturList;

    //Progress bar
    private ProgressBar view_ProgressBar;

    //Error text
    private TextView view_ErrorText;

    //assign button
    private Button button_Assign;

    //alert dialog
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token;
    private String authToken;

    //user id
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter debitur
    private AccountAssignmentAdapter accountAssignmentAdapter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_assignment);

        //setup
        GetViews();
        SetupTransaction();

        //load list debitur
        CreateGetListDebiturRequest();
    }

    //get views
    private void GetViews()
    {
        view_SearchButton = findViewById(R.id.AccountAssignment_SearchButton);
        view_DebiturList = findViewById(R.id.AccountAssignment_ListDebitur);
        view_ErrorText = findViewById(R.id.AccountAssignment_NoData);
        view_ProgressBar = findViewById(R.id.AccountAssignment_Progress);
        button_Assign = findViewById(R.id.AccountAssignmentLayout_AssignButton);

        genericAlert = new GenericAlert(this);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //load user id
        userID = ResourceLoader.LoadUserID(this);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_AccountAssignment_Back(View view)
    {
        finish();
    }

    //handle assign button
    public void HandleInput_AccountAssignment_AssignButton(View view)
    {
        //get jumlah debitur yang diselect
        int selectedDebiturCount = accountAssignmentAdapter.GetSelectedDebiturCount();

        //jika tidak ada yang diselect, show warning
        if (selectedDebiturCount <= 0)
        {
            String message = getString(R.string.AccountAssignment_Alert_NoDebiturSelected);
            genericAlert.DisplayAlert(message, "");
            return;
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get list debitur
    //----------------------------------------------------------------------------------------------
    private void CreateGetListDebiturRequest()
    {
        //hide list & alert
        view_DebiturList.setVisibility(View.GONE);
        view_ErrorText.setVisibility(View.GONE);

        //show progress bar
        view_ProgressBar.setVisibility(View.VISIBLE);

        //send request buat get list debitur
        new SendGetListDebitureRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request buat get list debitur
    private class SendGetListDebitureRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetDebiturRequestObject();

            //execute request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetDebiturResult(s);
        }
    }

    //create request object
    private JSONObject CreateGetDebiturRequestObject()
    {
        //create request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("status", "'PENDING'");

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_LIST");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    //handle result
    private void HandleGetDebiturResult(String result)
    {
        //pastikan result nggak kosong
        if (result == null || result.isEmpty())
        {
            return;
        }

        //pastikan response nggak 404
        if (result.equals("404"))
        {
            return;
        }

        try
        {
            //parse result ke json array
            JSONArray resultArray = new JSONArray(result);

            //initialize array
            ArrayList<DebiturItemWithFlag> debiturItems = new ArrayList<>();

            //add item to debitur
            for (int i = 0; i < resultArray.length(); i++)
            {
                //parse item
                DebiturItemWithFlag debiturItem = new DebiturItemWithFlag();
                debiturItem.ParseData(resultArray.getString(i));

                //add to list
                debiturItems.add(debiturItem);
            }

            //create adapter
            accountAssignmentAdapter = new AccountAssignmentAdapter(this, debiturItems);
            accountAssignmentAdapter.SetAssignButton(button_Assign);
            SetupRecyclerView();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //setup views
    private void SetupRecyclerView()
    {
        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_DebiturList.setLayoutManager(layoutManager);
        view_DebiturList.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_DebiturList.addItemDecoration(dividerItemDecoration);
        view_DebiturList.setAdapter(accountAssignmentAdapter);

        //hide progress bar & error text
        view_ProgressBar.setVisibility(View.GONE);
        view_ErrorText.setVisibility(View.GONE);

        //show list
        view_DebiturList.setVisibility(View.VISIBLE);
    }
}
