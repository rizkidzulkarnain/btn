package com.mitkoindo.smartcollection.module.chat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.ChatAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;

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

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url buat get berita
    private String baseURL;
    private String url_GetBerita;

    //auth token
    private String authToken;

    //user ID
    private String userID;

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
        SetupTransaction();
        CreateGetChatRequest();
    }

    //get views
    private void GetViews()
    {
        view_StaffName = findViewById(R.id.ChatWindow_Staffname);
        view_ChatForm = findViewById(R.id.ChatWindow_ChatForm);
        view_ChatList = findViewById(R.id.ChatWindow_RecyclerView);
        view_ProgressBar = findViewById(R.id.ChatWindow_ProgressBar);
        view_AlertText = findViewById(R.id.ChatWindow_AlertText);
        holder_ChatControl = findViewById(R.id.ChatWindow_ChatControl);
    }

    //set transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_GetBerita = getString(R.string.URL_Data_View);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_ChatWindow_BackButton(View view)
    {
        finish();
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get chat data
    //----------------------------------------------------------------------------------------------
    private void CreateGetChatRequest()
    {
        //show progress bar dan hide list & alert
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_ChatList.setVisibility(View.GONE);
        view_AlertText.setVisibility(View.GONE);

        new SendGetChatRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request
    private class SendGetChatRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }
}
