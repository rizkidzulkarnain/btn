package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithPTP;

import java.util.ArrayList;

/**
 * Created by W8 on 31/08/2017.
 */

public class PTPReminderAdapter extends RecyclerView.Adapter<PTPReminderAdapter.PTPReminderViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //List debitur with PTP
    private ArrayList<DebiturItemWithPTP> debiturItems;

    //context
    private Activity context;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public PTPReminderAdapter(Activity context, ArrayList<DebiturItemWithPTP> debiturItems)
    {
        this.context = context;
        this.debiturItems = debiturItems;
    }

    @Override
    public PTPReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_ptp_reminder, parent, false);
        return new PTPReminderViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(PTPReminderViewHolder holder, int position)
    {
        //pastikan index tidak keluar dari size
        if (position >= getItemCount())
            return;

        //get currentData
        DebiturItemWithPTP currentItem = debiturItems.get(position);

        //attach data
        holder.Name.setText(currentItem.getNama());
        holder.NoRekening.setText(currentItem.getNoRekening());
        holder.Tagihan.setText(currentItem.getTagihan());
        holder.DPD.setText(currentItem.getDpd());
        holder.LastPayment.setText(currentItem.getLastPayment());
        holder.PTP.setText(currentItem.PTPHint);
    }

    @Override
    public int getItemCount()
    {
        return debiturItems.size();
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class PTPReminderViewHolder extends RecyclerView.ViewHolder
    {
        TextView Name;
        TextView NoRekening;
        TextView Tagihan;
        TextView DPD;
        TextView LastPayment;
        TextView PTP;

        public PTPReminderViewHolder(View itemView)
        {
            super(itemView);

            Name = itemView.findViewById(R.id.text_view_nama_debitur);
            NoRekening = itemView.findViewById(R.id.text_view_no_rekening_value);
            Tagihan = itemView.findViewById(R.id.text_view_tagihan_value);
            DPD = itemView.findViewById(R.id.text_view_dpd_value);
            LastPayment = itemView.findViewById(R.id.text_view_last_payment_value);
            PTP = itemView.findViewById(R.id.text_view_PTP_value);
        }
    }
}
