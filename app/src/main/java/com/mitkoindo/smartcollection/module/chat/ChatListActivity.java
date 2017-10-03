package com.mitkoindo.smartcollection.module.chat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

    //search form
    private EditText form_Search;

    //search button & clear button
    private ImageView button_Search;
    private ImageView button_Clear;

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
        /*CreateGetStaffRequest();*/
        AttachStaffAdapterToList();

        //add listener
        AddListenerToViews();
    }

    //get views
    private void GetViews()
    {
        view_Recycler = findViewById(R.id.ChatList_StaffList);
        view_ProgressBar = findViewById(R.id.ChatList_ProgressBar);
        view_AlertText = findViewById(R.id.ChatList_AlertText);
        form_Search = findViewById(R.id.ChatList_SearchForm);
        button_Search = findViewById(R.id.ChatList_SearchButton);
        button_Clear = findViewById(R.id.ChatList_ClearButton);
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

    //add listener to views
    private void AddListenerToViews()
    {
        //add search listener
        form_Search.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    CreateSearchRequest();
                    return true;
                }

                return false;
            }
        });
    }

    //attach adapter to list
    private void AttachStaffAdapterToList()
    {
        //set properties ke adapter
        staffListChatAdapter = new StaffListChatAdapter(this);
        staffListChatAdapter.SetTransaction(baseURL, url_DataSP, authToken, userID);
        staffListChatAdapter.SetViews(view_Recycler, view_ProgressBar, view_AlertText);
        staffListChatAdapter.CreateGetStaffRequest();

        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_Recycler.setLayoutManager(layoutManager);
        view_Recycler.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_line1dp);
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

        //set total chat ke null
        staffListChatAdapter.SetTotalChatToNull(index);

        //pastikan item nggak null
        if (selectedStaff == null)
            return;

        //open chat window untuk staff ini
        Intent intent = new Intent(this, ChatWindowActivity.class);
        intent.putExtra("PartnerID", selectedStaff.USERID);
        intent.putExtra("PartnerName", selectedStaff.FULL_NAME);
        startActivity(intent);
    }

    //create search request
    private void CreateSearchRequest()
    {
        //get query
        String searchQuery = form_Search.getText().toString();

        //show clear button, hide search button
        button_Clear.setVisibility(View.VISIBLE);
        button_Search.setVisibility(View.GONE);

        //execute request
        staffListChatAdapter.CreateSearchRequest(searchQuery);
    }

    //handle input dari search button
    public void HandleInput_ChatList_Search(View view)
    {
        CreateSearchRequest();
    }

    //handle input dari clear button
    public void HandleInput_ChatList_Clear(View view)
    {
        //cek query
        String searchQuery = form_Search.getText().toString();

        //jika query kosong, reset search form
        if (searchQuery.isEmpty())
        {
            //show search button, hide clear button
            button_Search.setVisibility(View.VISIBLE);
            button_Clear.setVisibility(View.GONE);

            //create request
            staffListChatAdapter.CreateSearchRequest("");
        }
        else
        {
            //clear query
            form_Search.setText("");
        }
    }
}
