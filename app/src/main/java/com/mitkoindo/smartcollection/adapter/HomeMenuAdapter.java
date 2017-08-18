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
    //  Input
    //----------------------------------------------------------------------------------------------
    //click listener
    private ItemClickListener itemClickListener;

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

    //get current menu
    public String getCurrentMenu(int position)
    {
        //pastikan posisi tidak melebihi index
        if (position >= homeMenus.size())
            return "Out of bounds";

        //return menu name
        return homeMenus.get(position).Name;
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
    class HomeMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView Icon;
        TextView Name;

        public HomeMenuViewHolder(View itemView)
        {
            super(itemView);

            Icon = itemView.findViewById(R.id.HomeMenuAdapter_Icon);
            Name = itemView.findViewById(R.id.HomeMenuAdapter_Text);

            //add input listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
