package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.MainActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.DrawerMenu;

import java.util.ArrayList;

public class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuAdapter.DrawerMenuViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of menu items
    private ArrayList<DrawerMenu> drawerMenus;

    //app context
    private MainActivity context;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public DrawerMenuAdapter(MainActivity context, ArrayList<DrawerMenu> drawerMenus)
    {
        this.context = context;
        this.drawerMenus = drawerMenus;
    }

    @Override
    public DrawerMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_drawer_menu, parent, false);
        return new DrawerMenuViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(DrawerMenuViewHolder holder, int position)
    {
        //pastikan index tidak melebih jumlah menu
        if (position >= drawerMenus.size())
            return;

        //get current menu
        DrawerMenu currentMenu = drawerMenus.get(position);

        //attach data
        holder.Icon.setImageBitmap(currentMenu.icon);
        holder.MenuText.setText(currentMenu.menuText);

        //hide / show separator
        if (currentMenu.showSeparator)
            holder.Separator.setVisibility(View.VISIBLE);
        else
            holder.Separator.setVisibility(View.INVISIBLE);

        //add listener to view
        final int currentPos = position;
        holder.ClickArea.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HandleInput_SelectMenu(currentPos);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return drawerMenus.size();
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    private void HandleInput_SelectMenu(int index)
    {
        //open selected menu
        context.HandleInput_OpenSelectedMenu(index);
    }

    //----------------------------------------------------------------------------------------------
    //  View holders
    //----------------------------------------------------------------------------------------------
    class DrawerMenuViewHolder extends RecyclerView.ViewHolder
    {
        //View items
        View ClickArea;
        ImageView Icon;
        TextView MenuText;
        View Separator;

        public DrawerMenuViewHolder(View itemView)
        {
            super(itemView);

            //get views
            ClickArea = itemView.findViewById(R.id.DrawerMenu_ClickArea);
            Icon = itemView.findViewById(R.id.DrawerMenu_Icon);
            MenuText = itemView.findViewById(R.id.DrawerMenu_Text);
            Separator = itemView.findViewById(R.id.DrawerMenu_Separator);
        }
    }
}
