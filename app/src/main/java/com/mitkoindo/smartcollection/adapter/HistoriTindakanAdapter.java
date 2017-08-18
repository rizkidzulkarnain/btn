package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.HistoriTindakan;

import java.util.ArrayList;

public class HistoriTindakanAdapter extends RecyclerView.Adapter<HistoriTindakanAdapter.HistoriTindakanViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //data histori tindakan
    private ArrayList<HistoriTindakan> all_HistoriTindakan;

    //reference ke context
    private Activity context;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public HistoriTindakanAdapter(Activity context, ArrayList<HistoriTindakan> all_HistoriTindakan)
    {
        this.context = context;
        this.all_HistoriTindakan = all_HistoriTindakan;
    }

    @Override
    public HistoriTindakanViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_histori_tindakan, parent, false);
        return new HistoriTindakanViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(HistoriTindakanViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi size data
        if (position >= getItemCount())
            return;

        //get current history data
        HistoriTindakan currentData = all_HistoriTindakan.get(position);

        //attach data to views
        holder.DeskripsiTindakan.setText(currentData.DeskripsiTindakan);
        holder.TanggalTindakan.setText(currentData.TanggelTindakan_View);
        holder.Pelaku.setText(currentData.YangMelakukan);
        holder.AlasanTindakan.setText(currentData.AlasanTindakan);
    }

    @Override
    public int getItemCount()
    {
        return all_HistoriTindakan.size();
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class HistoriTindakanViewHolder extends RecyclerView.ViewHolder
    {
        TextView DeskripsiTindakan;
        TextView TanggalTindakan;
        TextView Pelaku;
        TextView AlasanTindakan;

        public HistoriTindakanViewHolder(View itemView)
        {
            super(itemView);

            DeskripsiTindakan = itemView.findViewById(R.id.HistoriTindakanAdapter_DeskripsiTindakan);
            TanggalTindakan = itemView.findViewById(R.id.HistoriTindakanAdapter_TanggalTindakan);
            Pelaku = itemView.findViewById(R.id.HistoriTindakanAdapter_Pelaku);
            AlasanTindakan = itemView.findViewById(R.id.HistoriTindakanAdapter_AlasanTindakan);
        }
    }
}
