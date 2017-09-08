package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;

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

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public ChatAdapter(Activity context)
    {
        this.context = context;
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

    }

    @Override
    public int getItemCount()
    {
        return 0;
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
