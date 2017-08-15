package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.HomeMenu;

import java.util.ArrayList;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.HomeMenuViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of menu items
    private ArrayList<HomeMenu> homeMenus;

    //context
    private Activity context;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public HomeMenuAdapter(Activity context, ArrayList<HomeMenu> homeMenus)
    {
        this.context = context;
        this.homeMenus = homeMenus;
    }

    @Override
    public HomeMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_home_menu, parent, false);
        return new HomeMenuViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(HomeMenuViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi index
        if (position >= getItemCount())
            return;

        //get object
        HomeMenu homeMenu = homeMenus.get(position);

        //attach data
        holder.Name.setText(homeMenu.Name);

        //cek bitmap
        if (homeMenu.Icon != null)
            holder.Icon.setImageBitmap(homeMenu.Icon);
    }

    @Override
    public int getItemCount()
    {
        return homeMenus.size();
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class HomeMenuViewHolder extends RecyclerView.ViewHolder
    {
        ImageView Icon;
        TextView Name;

        public HomeMenuViewHolder(View itemView)
        {
            super(itemView);

            Icon = itemView.findViewById(R.id.HomeMenuAdapter_Icon);
            Name = itemView.findViewById(R.id.HomeMenuAdapter_Text);

            //add input listener disini
        }
    }
}
