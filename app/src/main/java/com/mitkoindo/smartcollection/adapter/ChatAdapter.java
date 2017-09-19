package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.module.chat.ChatWindowActivity;
import com.mitkoindo.smartcollection.objectdata.ChatItem;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 08/09/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //reference ke activity
    private Activity context;

    //chat items
    private ArrayList<ChatItem> chatItems = new ArrayList<>();

    //flag binding
    private boolean onBind;

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

    //flag transaksi, apakah transaksi ini untuk update chat, atau load initial data
    private boolean flag_UpdateChat;

    //flag allow load page
    private boolean flag_AllowLoadPage;

    //----------------------------------------------------------------------------------------------
    //  View
    //----------------------------------------------------------------------------------------------
    //chat list
    private RecyclerView view_ChatList;

    //progress bar
    private ProgressBar view_ProgressBar;

    //alert text
    private TextView view_AlertText;

    //chat form
    private EditText view_ChatForm;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public ChatAdapter(Activity context)
    {
        this.context = context;
        flag_AllowLoadPage = true;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_chat, parent, false);
        return new ChatViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi item count
        if (position >= getItemCount())
            return;

        //set flag onbind ke true
        onBind = true;

        //get current chat
        ChatItem currentChatItem = chatItems.get(position);

        //cek apakah sender id sama dengan user ini
        if (currentChatItem.FromUserID.equals(userID))
        {
            //jika iya, maka gunakan adapter sentmessage karena message ini dikirim oleh user
            holder.holder_SentMessage.setVisibility(View.VISIBLE);
            holder.holder_ReceivedMessage.setVisibility(View.GONE);

            //set text
            holder.sentMessage.setText(currentChatItem.Message);
        }
        else
        {
            //jika tidak, maka gunakan adapter received message karena message ini dikirim oleh partner
            holder.holder_SentMessage.setVisibility(View.GONE);
            holder.holder_ReceivedMessage.setVisibility(View.VISIBLE);

            //set text
            holder.receivedMessage.setText(currentChatItem.Message);
        }

        //set flag onbind ke false
        onBind = false;
    }

    @Override
    public int getItemCount()
    {
        return chatItems.size();
    }

    //set transaksi
    public void SetTransaction(String baseURL, String url_DataSP, String authToken, String userID, String userID_ChatPartner)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
        this.userID_ChatPartner = userID_ChatPartner;
    }

    //set views
    public void SetViews(RecyclerView view_ChatList, ProgressBar view_ProgressBar, TextView view_AlertText, EditText view_ChatForm)
    {
        this.view_ChatList = view_ChatList;
        this.view_ProgressBar = view_ProgressBar;
        this.view_AlertText = view_AlertText;
        this.view_ChatForm = view_ChatForm;
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulasi data
    //----------------------------------------------------------------------------------------------
    public void AddChatMessage(ChatItem newChatItem)
    {
        chatItems.add(newChatItem);

        //update list
        if (!onBind)
            notifyDataSetChanged();

        //scroll to bottom
        ScrollChatToBottom();
    }

    //scroll to bottom
    private void ScrollChatToBottom()
    {
        view_ChatList.smoothScrollToPosition(getItemCount());
        /*view_ChatList.scrollToPosition(getItemCount());*/
    }

    //----------------------------------------------------------------------------------------------
    //  Request chat data
    //----------------------------------------------------------------------------------------------
    public void CreateGetChatRequest()
    {
        //show progress bar dan hide list & alert
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_ChatList.setVisibility(View.GONE);
        view_AlertText.setVisibility(View.GONE);

        //set flag transaksi ke load initial data
        flag_UpdateChat = false;

        //send request
        new SendGetChatRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create request buat update chat
    public void CreateUpdateChatRequest()
    {
        //set flag transaksi ke update chat
        flag_UpdateChat = true;

        //send request
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
            spParameterObject.put("limit", 9999);

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
            ArrayList<ChatItem> newChatItems = new ArrayList<>();

            //parse data
            for (int i = 0; i < dataArray.length(); i++)
            {
                ChatItem newChatItem = new ChatItem();
                newChatItem.ParseData(dataArray.getJSONObject(i));
                newChatItems.add(newChatItem);
            }

            if (!flag_UpdateChat)
                SetChatItem(newChatItems);
            else
                UpdateChatItem(newChatItems);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show alert
            view_AlertText.setText(R.string.Text_SomethingWrong);
            view_AlertText.setVisibility(View.VISIBLE);
        }
    }

    //Set chat item to list
    private void SetChatItem(ArrayList<ChatItem> newChatItems)
    {
        chatItems = new ArrayList<>();

        for (int i = 0; i < newChatItems.size(); i++)
        {
            chatItems.add(newChatItems.get(i));
        }

        if (!onBind)
            notifyDataSetChanged();

        view_ChatList.setVisibility(View.VISIBLE);
        view_ProgressBar.setVisibility(View.GONE);
        view_AlertText.setVisibility(View.GONE);

        //scroll to bottom
        ScrollChatToBottom();
        ScrollChatToBottom();
        ScrollChatToBottom();
    }

    //Update chat item
    private void UpdateChatItem(ArrayList<ChatItem> newChatItems)
    {
        //cek apakah chat item sudah ada isi apa belum
        if (chatItems == null || chatItems.size() <= 0)
        {
            //langsung add data baru
            SetChatItem(newChatItems);
            return;
        }

        //bandingkan element terakhir chatlist dengan new chat item
        ChatItem currentLastElement = chatItems.get(chatItems.size() - 1);
        ChatItem newLastElement = newChatItems.get(newChatItems.size() - 1);

        if (currentLastElement.ID == newLastElement.ID)
            //maka chatnya up-to date, nggak perlu melakukan apa2
            return;

        //kalo beda, add new chat ke current chat list
        boolean allowAddData = false;
        for (int i = 0; i < newChatItems.size(); i++)
        {
            if (allowAddData)
                chatItems.add(newChatItems.get(i));

            if (!allowAddData && currentLastElement.ID == newChatItems.get(i).ID)
            {
                allowAddData = true;
            }
        }

        //update view
        if (!onBind)
            notifyDataSetChanged();

        //scroll to bottom
        ScrollChatToBottom();
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat send new message
    //----------------------------------------------------------------------------------------------
    public void CreateSendMessageRequest()
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
        AddChatMessage(chatItem);

        //clear text di chat form
        view_ChatForm.setText("");
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class ChatViewHolder extends RecyclerView.ViewHolder
    {
        TextView receivedMessage;
        TextView sentMessage;

        View holder_ReceivedMessage;
        View holder_SentMessage;

        public ChatViewHolder(View itemView)
        {
            super(itemView);

            receivedMessage = itemView.findViewById(R.id.ChatAdapter_ReceivedMessage);
            sentMessage = itemView.findViewById(R.id.ChatAdapter_SentMessage);

            holder_ReceivedMessage = itemView.findViewById(R.id.ChatAdapter_Holder_ReceivedMessage);
            holder_SentMessage = itemView.findViewById(R.id.ChatAdapter_Holder_SentMessage);
        }
    }
}
