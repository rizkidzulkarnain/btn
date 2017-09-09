package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.ChatItem;

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
    private ArrayList<ChatItem> chatItems;

    //ID pengguna
    private String currentUserID;

    //flag binding
    private boolean onBind;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public ChatAdapter(Activity context, ArrayList<ChatItem> chatItems, String currentUserID)
    {
        this.context = context;
        this.chatItems = chatItems;
        this.currentUserID = currentUserID;
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
        if (currentChatItem.FromUserID.equals(currentUserID))
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
            holder.holder_ReceivedMessage.setVisibility(View.GONE);

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

    //----------------------------------------------------------------------------------------------
    //  Manipulasi data
    //----------------------------------------------------------------------------------------------
    public void AddChatMessage(ChatItem newChatItem)
    {
        chatItems.add(newChatItem);

        //update list
        if (!onBind)
            notifyDataSetChanged();
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
