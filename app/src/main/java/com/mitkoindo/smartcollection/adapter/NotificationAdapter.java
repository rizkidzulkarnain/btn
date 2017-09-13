package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;

/**
 * Created by W8 on 13/09/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //reference ke context
    private Activity context;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public NotificationAdapter(Activity context)
    {
        this.context = context;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_notifikasi, parent, false);
        return new NotificationViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position)
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
    class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        TextView notificationText;

        public NotificationViewHolder(View itemView)
        {
            super(itemView);

            notificationText = itemView.findViewById(R.id.NotifikasiAdapter_Text);
        }
    }
}
