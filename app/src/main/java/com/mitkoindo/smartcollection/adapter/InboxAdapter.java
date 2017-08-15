package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.InboxItem;

import java.util.ArrayList;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of inbox item
    private ArrayList<InboxItem> inboxItems;

    //context
    private Activity context;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public InboxAdapter()
    {

    }

    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(InboxViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    class InboxViewHolder extends RecyclerView.ViewHolder
    {
        TextView PersonName;
        TextView NoRekening;
        TextView Tagihan;
        TextView DPD;
        TextView LastPayment;

        public InboxViewHolder(View itemView)
        {
            super(itemView);

            PersonName = itemView.findViewById(R.id.inboxAdapter_PersonName);
            NoRekening = itemView.findViewById(R.id.inboxAdapter_AccountNo);
            Tagihan = itemView.findViewById(R.id.inboxAdapter_Tagihan);
            DPD = itemView.findViewById(R.id.inboxAdapter_DPD);
            LastPayment = itemView.findViewById(R.id.inboxAdapter_LastPayment);
        }
    }
}
