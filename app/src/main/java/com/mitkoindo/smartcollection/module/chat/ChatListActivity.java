package com.mitkoindo.smartcollection.module.chat;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.StaffListChatAdapter;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.Staff;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //recycler view
    private RecyclerView view_Recycler;

    //progress bar
    private ProgressBar view_ProgressBar;

    //alert
    private TextView view_AlertText;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter staff list
    private StaffListChatAdapter staffListChatAdapter;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

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
        setContentView(R.layout.activity_chat_list);

        //setup
        GetViews();
        SetupTransaction();

        //create get staff request
        CreateGetStaffRequest();
    }

    //get views
    private void GetViews()
    {
        view_Recycler = findViewById(R.id.ChatList_StaffList);
        view_ProgressBar = findViewById(R.id.ChatList_ProgressBar);
        view_AlertText = findViewById(R.id.ChatList_AlertText);
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
    public void HandleInput_ChatList_Back(View view)
    {
        finish();
    }

    //open chat window
    private void OpenChatWindow(int index)
    {
        //get selected staff
        Staff selectedStaff = staffListChatAdapter.GetSelectedStaff(index);

        //pastikan item nggak null
        if (selectedStaff == null)
            return;

        //else, open chat window untuk staff ini
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get data staff
    //----------------------------------------------------------------------------------------------
    private void CreateGetStaffRequest()
    {
        //show progress bar, hide list & alert
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_Recycler.setVisibility(View.GONE);
        view_AlertText.setVisibility(View.GONE);

        //send request
        new SendGetStaffRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create request object
    private JSONObject CreateGetStaffRequestObject()
    {
        JSONObject requestObject = new JSONObject();

        try
        {
            //create SpParameter object
            JSONObject spParameterObject = new JSONObject();
            /*spParameterObject.put("userID", userID);*/
            spParameterObject.put("userID", "BTN0013887");

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_STAFF_LIST");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestObject;
    }

    private class SendGetStaffRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedUrl = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetStaffRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedUrl);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetStaffRequest(s);
        }
    }

    //handle result transaksi
    private void HandleGetStaffRequest(String resultString)
    {
        //hide progress bar
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan result tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show alert
            view_AlertText.setText(R.string.Text_SomethingWrong);
            return;
        }

        try
        {
            //create staff arraylist
            ArrayList<Staff> staffs = new ArrayList<>();

            //konversi ke jsonArray
            JSONArray dataArray = new JSONArray(resultString);

            //loop seluruh data di array
            for (int i = 0; i < dataArray.length(); i++)
            {
                //create new staff data dan parse data dari JSON Object
                Staff newStaff = new Staff();
                newStaff.Parse(dataArray.getJSONObject(i));

                //add data ke list
                staffs.add(newStaff);
            }

            //create adapter
            staffListChatAdapter = new StaffListChatAdapter(this, staffs);
            AttachStaffAdapterToList();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //show list
        view_Recycler.setVisibility(View.VISIBLE);
    }

    //attach adapter to list
    private void AttachStaffAdapterToList()
    {
        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_Recycler.setLayoutManager(layoutManager);
        view_Recycler.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_Recycler.addItemDecoration(dividerItemDecoration);
        view_Recycler.setAdapter(staffListChatAdapter);

        //add listener to adapter
        staffListChatAdapter.setClickListener(new ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                OpenChatWindow(position);
            }
        });

        //show list
        view_Recycler.setVisibility(View.VISIBLE);
        view_AlertText.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);
    }
}
