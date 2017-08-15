package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.News;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of news
    private ArrayList<News> allNews;

    //context
    private Activity context;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public NewsAdapter(Activity context, ArrayList<News> allNews)
    {
        this.context = context;
        this.allNews = allNews;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_news, parent, false);
        return new NewsViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position)
    {
        //pastikan posisi tidak keluar dari index
        if (position >= getItemCount())
            return;

        //get current news
        News currentNews = allNews.get(position);

        //attach data
        holder.Title.setText(currentNews.Title);
        holder.Sender.setText(currentNews.Sender);
        holder.SendTime.setText(currentNews.SendTime);
        holder.Message.setText(currentNews.Message);
    }

    @Override
    public int getItemCount()
    {
        return allNews.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title;
        TextView Sender;
        TextView SendTime;
        TextView Message;

        public NewsViewHolder(View itemView)
        {
            super(itemView);

            Title = itemView.findViewById(R.id.newsAdapter_Title);
            Sender = itemView.findViewById(R.id.newsAdapter_Sender);
            SendTime = itemView.findViewById(R.id.newsAdapter_SendTime);
            Message = itemView.findViewById(R.id.newsAdapter_Message);
        }
    }
}
