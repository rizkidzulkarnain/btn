package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
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
        holder.PTPAmount.setText(currentItem.PTPAmount);

        //add listener
        final int currentPos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenDetailPage(currentPos);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return debiturItems.size();
    }

    //----------------------------------------------------------------------------------------------
    //  Open detail page
    //----------------------------------------------------------------------------------------------
    private void OpenDetailPage(int position)
    {
        //get currentData
        DebiturItemWithPTP currentItem = debiturItems.get(position);

        //open detail page
        Intent intent = DetailDebiturActivity.instantiate(context, currentItem.getNoRekening(),
                "", ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE);
        context.startActivity(intent);
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
        TextView PTPAmount;

        public PTPReminderViewHolder(View itemView)
        {
            super(itemView);

            Name = itemView.findViewById(R.id.text_view_nama_debitur);
            NoRekening = itemView.findViewById(R.id.text_view_no_rekening_value);
            Tagihan = itemView.findViewById(R.id.text_view_tagihan_value);
            DPD = itemView.findViewById(R.id.text_view_dpd_value);
            LastPayment = itemView.findViewById(R.id.text_view_last_payment_value);
            PTP = itemView.findViewById(R.id.text_view_PTP_value);
            PTPAmount = itemView.findViewById(R.id.text_view_PTPAmount_value);
        }
    }
}
