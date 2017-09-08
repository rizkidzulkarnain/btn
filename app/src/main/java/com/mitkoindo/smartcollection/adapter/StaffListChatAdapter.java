package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.objectdata.Staff;

import java.util.ArrayList;

/**
 * Created by W8 on 08/09/2017.
 */

public class StaffListChatAdapter extends RecyclerView.Adapter<StaffListChatAdapter.StaffListChatViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //reference ke context
    private Activity context;

    //list of staff
    private ArrayList<Staff> staffList;

    //----------------------------------------------------------------------------------------------
    //  Input
    //----------------------------------------------------------------------------------------------
    //click listener
    private ItemClickListener itemClickListener;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //construtcor
    public StaffListChatAdapter(Activity context, ArrayList<Staff> staffList)
    {
        this.context = context;
        this.staffList = staffList;
    }

    @Override
    public StaffListChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_chatlist, parent, false);
        return new StaffListChatViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(StaffListChatViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi count
        if (position >= getItemCount())
            return;

        //get current staff
        Staff currentStaff = staffList.get(position);

        //attach nama staff
        holder.staffName.setText(currentStaff.FULL_NAME);
    }

    @Override
    public int getItemCount()
    {
        return staffList.size();
    }

    //----------------------------------------------------------------------------------------------
    //  Handle Input
    //----------------------------------------------------------------------------------------------
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    //get selected staff
    public Staff GetSelectedStaff(int index)
    {
        //pastikan index tidak melebihi list size
        if (index >= getItemCount())
            return null;

        //return staff pada index
        return staffList.get(index);
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class StaffListChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //staff name
        TextView staffName;

        public StaffListChatViewHolder(View itemView)
        {
            super(itemView);

            staffName = itemView.findViewById(R.id.ChatListAdapter_Staffname);

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
