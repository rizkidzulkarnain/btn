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
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.ChatAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.ChatItem;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //user ID yang jadi lawan bicara
    private String userID_ChatPartner;

    //----------------------------------------------------------------------------------------------
    //  Constructor
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //setup
        GetBundles();
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

        //add listener pada chat form
        view_ChatForm.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    //send chat message
                    CreateSendMessageRequest();

                    return true;
                }

                return false;
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
        CreateSendMessageRequest();
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

    //create request buat get chat list
    private JSONObject CreateGetChatRequestObject()
    {
        //inisialisasi request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID_ChatPartner);

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_CHAT_LIST");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    //send request
    private class SendGetChatRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetChatRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleResult(s);
        }
    }

    //handle result
    private void HandleResult(String resultString)
    {
        //hide progress bar
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan response tidak null atau kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show alert
            view_AlertText.setText(R.string.Text_SomethingWrong);
            view_AlertText.setVisibility(View.VISIBLE);
            return;
        }

        //cek ada / tidak message
        if (resultString.equals("Not Found"))
        {
            //show alert bahwa tidak ada message
            view_AlertText.setText(R.string.ChatWindow_Alert_NoChat);
            view_AlertText.setVisibility(View.VISIBLE);
            return;
        }

        try
        {
            //parse message
            JSONArray dataArray = new JSONArray(resultString);

            //create list of chat
            ArrayList<ChatItem> chatItems = new ArrayList<>();

            //parse data
            for (int i = 0; i < dataArray.length(); i++)
            {
                ChatItem newChatItem = new ChatItem();
                newChatItem.ParseData(dataArray.getJSONObject(i));
                chatItems.add(newChatItem);
            }

            //create adapter
            chatAdapter = new ChatAdapter(this, chatItems, userID);

            //attach adapter
            AttachChatAdapter();
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show alert
            view_AlertText.setText(R.string.Text_SomethingWrong);
            view_AlertText.setVisibility(View.VISIBLE);
        }
    }

    //attach chat adapter
    private void AttachChatAdapter()
    {
        //setup recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_ChatList.setLayoutManager(layoutManager);
        view_ChatList.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_ChatList.addItemDecoration(dividerItemDecoration);
        view_ChatList.setAdapter(chatAdapter);

        //show list
        view_ChatList.setVisibility(View.VISIBLE);
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat send new message
    //----------------------------------------------------------------------------------------------
    private void CreateSendMessageRequest()
    {
        new ExecuteSendMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create send message request object
    private JSONObject CreateSendMessageRequestObject()
    {
        //inisialisasi object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create spParameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("fromUserID", userID);
            spParameterObject.put("toUserID", userID_ChatPartner);
            spParameterObject.put("message", view_ChatForm.getText().toString());

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_CHAT_SEND");
            requestObject.put("SpParameter", spParameterObject);
            requestObject.put("Single", true);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    //send request buat send message
    private class ExecuteSendMessage extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateSendMessageRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleSendMessageResult(s);
        }
    }

    //handle response
    private void HandleSendMessageResult(String resultString)
    {
        //pastikan tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //ToDO : show alert gagal kirim
            return;
        }

        //assume bahwa message kekirim, add message ke adapter
        ChatItem chatItem = new ChatItem();
        chatItem.FromUserID = userID;
        chatItem.ToUserID = userID_ChatPartner;
        chatItem.Message = view_ChatForm.getText().toString();
        chatAdapter.AddChatMessage(chatItem);

        //clear text di chat form
        view_ChatForm.setText("");
    }
}
