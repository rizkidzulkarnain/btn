package com.mitkoindo.smartcollection.module.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.ChatAdapter;
import com.mitkoindo.smartcollection.backgroundservice.ChatUpdaterService;
import com.mitkoindo.smartcollection.helper.RecyclerViewHelper;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.ChatItem;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.os.Handler;

public class ChatWindowActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter
    private ChatAdapter chatAdapter;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //staff name
    private TextView view_StaffName;

    //holder chat control
    private View holder_ChatControl;

    //chat form
    private EditText view_ChatForm;

    //chat list
    private RecyclerView view_ChatList;

    //progress bar
    private ProgressBar view_ProgressBar;

    //alert text
    private TextView view_AlertText;

    //chat load indicator
    private ProgressBar view_PageLoadIndicator;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url buat get berita
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //user ID yang jadi lawan bicara
    private String userID_ChatPartner;

    //----------------------------------------------------------------------------------------------
    //  Update chat window
    //----------------------------------------------------------------------------------------------
    private android.os.Handler chatUpdateHandler;
    private Runnable chatUpdateRunnable;

    //delay before updating chat
    private final int delay_chat_update = 5;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //setup
        GetViews();
        GetBundles();
        SetupTransaction();
        AttachChatAdapter();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //create handler & runnable to update chat
        chatUpdateHandler = new Handler();
        chatUpdateRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                chatAdapter.CreateUpdateChatRequest();
                chatUpdateHandler.postDelayed(chatUpdateRunnable, delay_chat_update * 1000);
            }
        };

        chatUpdateHandler.postDelayed(chatUpdateRunnable, delay_chat_update * 1000);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        //remove runnable from handler
        if (chatUpdateHandler != null && chatUpdateRunnable != null)
            chatUpdateHandler.removeCallbacks(chatUpdateRunnable);

        super.onDestroy();
    }

    //get views
    private void GetViews()
    {
        view_StaffName = findViewById(R.id.ChatWindow_Staffname);
        view_ChatForm = findViewById(R.id.ChatWindow_ChatForm);
        view_ChatList = findViewById(R.id.ChatWindow_RecyclerView);
        view_ProgressBar = findViewById(R.id.ChatWindow_ProgressBar);
        view_AlertText = findViewById(R.id.ChatWindow_AlertText);
        view_PageLoadIndicator = findViewById(R.id.ChatWindow_PageLoadIndicator);
        holder_ChatControl = findViewById(R.id.ChatWindow_ChatControl);

        //add listener pada chat form
        view_ChatForm.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    //send chat message
                    chatAdapter.CreateSendMessageRequest();

                    return true;
                }

                return false;
            }
        });

        //add listener pada recyclerview
        view_ChatList.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                //pastikan adapter tidak null
                if (chatAdapter == null)
                    return;

                //load new page
                if (RecyclerViewHelper.isFirstItemDisplaying(view_ChatList))
                    chatAdapter.CreateLoadNewPageRequest();
            }
        });
    }

    //set transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);
    }

    //get bundles
    private void GetBundles()
    {
        //get bundle
        Bundle bundle = getIntent().getExtras();

        //pastikan bundle tidak null
        if (bundle == null)
            return;

        //get user id
        userID_ChatPartner = bundle.getString("PartnerID");

        //get partner name
        String partnerName = bundle.getString("PartnerName");
        view_StaffName.setText(partnerName);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_ChatWindow_BackButton(View view)
    {
        finish();
    }

    //handle send button
    public void HandleInput_ChatWindow_SendButton(View view)
    {
        chatAdapter.CreateSendMessageRequest();
    }

    //----------------------------------------------------------------------------------------------
    //  Setup view
    //----------------------------------------------------------------------------------------------
    //attach chat adapter
    private void AttachChatAdapter()
    {
        //set property ke adapter
        chatAdapter = new ChatAdapter(this);
        chatAdapter.SetTransaction(baseURL, url_DataSP, authToken, userID, userID_ChatPartner);
        chatAdapter.SetViews(view_ChatList, view_ProgressBar, view_AlertText, view_ChatForm, view_PageLoadIndicator);

        //setup recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_ChatList.setLayoutManager(layoutManager);
        view_ChatList.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_ChatList.addItemDecoration(dividerItemDecoration);
        view_ChatList.setAdapter(chatAdapter);

        //create request buat get chat data
        chatAdapter.CreateGetChatRequest();
    }
}
