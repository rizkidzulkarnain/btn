package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.module.berita.BeritaGrupFragment;
import com.mitkoindo.smartcollection.objectdata.GlobalNews;
import com.mitkoindo.smartcollection.objectdata.MobileNews;

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
    private ArrayList<MobileNews> news;

    //----------------------------------------------------------------------------------------------
    //  Input
    //----------------------------------------------------------------------------------------------
    //click listener
    private ItemClickListener itemClickListener;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public BeritaAdapter(Activity context, ArrayList<MobileNews> news)
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
        MobileNews currentNews = news.get(position);

        //attach to view
        holder.Title.setText(currentNews.Title);
        holder.Sender.setText(currentNews.AuthorID);
        holder.Content.setText(currentNews.NewsContent);
        holder.SendTime.setText(currentNews.StartDate);

        //cek attachment
        if (currentNews.Attachment == null || currentNews.Attachment.isEmpty())
        {
            //show no attachment text
            holder.Holder_NoFile.setVisibility(View.VISIBLE);
            holder.Holder_FileExist.setVisibility(View.GONE);
        }
        else
        {
            //show attachment
            holder.Holder_FileExist.setVisibility(View.VISIBLE);
            holder.Holder_NoFile.setVisibility(View.GONE);

            //set filename
            holder.FileName.setText(currentNews.Attachment);
        }
    }

    @Override
    public int getItemCount()
    {
        return news.size();
    }

    //get current item
    public MobileNews GetCurrentNews(int index)
    {
        //pastikan index tidak melebihi item size
        if (index >= getItemCount())
            return null;

        //return news item
        return news.get(index);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle Input
    //----------------------------------------------------------------------------------------------
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    //Viewholder
    class BeritaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
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

            //add listener ke file exist holder
            Holder_FileExist.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
