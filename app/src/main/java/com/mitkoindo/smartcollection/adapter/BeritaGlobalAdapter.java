package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.GlobalNews;

import java.util.ArrayList;

/**
 * Created by W8 on 23/08/2017.
 */

public class BeritaGlobalAdapter extends RecyclerView.Adapter<BeritaGlobalAdapter.BeritaGlobalViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //context
    private Activity context;

    //list of berita
    private ArrayList<GlobalNews> globalNews;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public BeritaGlobalAdapter(Activity context, ArrayList<GlobalNews> globalNews)
    {
        this.context = context;
        this.globalNews = globalNews;
    }

    @Override
    public BeritaGlobalViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater inflater = context.getLayoutInflater();
        View thisView = inflater.inflate(R.layout.adapter_berita_global, parent, false);
        return new BeritaGlobalViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(BeritaGlobalViewHolder holder, int position)
    {
        //pastikan index tidak melebihi posisi
        if (position >= getItemCount())
            return;

        //get current news
        GlobalNews currentNews = globalNews.get(position);

        //attach data to view
        holder.Title.setText(currentNews.Subject);
        holder.SendTime.setText(currentNews.TanggalBerita);
        holder.Content.setText(Html.fromHtml(currentNews.Content));
    }

    @Override
    public int getItemCount()
    {
        return globalNews.size();
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class BeritaGlobalViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title;
        TextView SendTime;
        TextView Content;

        public BeritaGlobalViewHolder(View itemView)
        {
            super(itemView);

            Title = itemView.findViewById(R.id.AdapterBeritaGlobal_Title);
            SendTime = itemView.findViewById(R.id.AdapterBeritaGlobal_SendTime);
            Content = itemView.findViewById(R.id.AdapterBeritaGlobal_Content);
        }
    }
}
