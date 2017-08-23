package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.GlobalNews;

import java.util.ArrayList;

/**
 * Created by W8 on 22/08/2017.
 */

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //reference ke context
    private Activity context;

    //data berita
    private ArrayList<GlobalNews> news;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public BeritaAdapter(Activity context, ArrayList<GlobalNews> news)
    {
        this.context = context;
        this.news = news;
    }

    @Override
    public BeritaViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //get layout inflater
        LayoutInflater inflater = context.getLayoutInflater();

        //inflate layout
        View thisView = inflater.inflate(R.layout.adapter_berita, parent, false);

        //return viewholder
        return new BeritaViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(BeritaViewHolder holder, int position)
    {
        //pastikan posisi nggak melebihi index
        if (position >= getItemCount())
            return;

        //get current item, dan attach ke holder
        GlobalNews currentNews = news.get(position);

        //attach to view
        holder.Title.setText(currentNews.Subject);

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    //Viewholder
    class BeritaViewHolder extends RecyclerView.ViewHolder
    {
        //views
        TextView Title;
        TextView Sender;
        TextView SendTime;
        TextView Content;
        ImageView FileIcon;
        TextView FileName;

        //holder file
        View Holder_NoFile;
        View Holder_FileExist;

        public BeritaViewHolder(View itemView)
        {
            super(itemView);

            Title = itemView.findViewById(R.id.AdapterBerita_Title);
            Sender = itemView.findViewById(R.id.AdapterBerita_Sender);
            SendTime = itemView.findViewById(R.id.AdapterBerita_SendTime);
            Content = itemView.findViewById(R.id.AdapterBerita_Content);
            FileIcon = itemView.findViewById(R.id.AdapterBerita_FileIcon);
            FileName = itemView.findViewById(R.id.AdapterBerita_FileName);

            Holder_NoFile = itemView.findViewById(R.id.AdapterBerita_NoFile);
            Holder_FileExist = itemView.findViewById(R.id.AdapterBerita_FileHolder);
        }
    }
}
