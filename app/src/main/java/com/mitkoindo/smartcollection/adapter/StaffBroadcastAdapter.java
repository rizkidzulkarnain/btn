package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.Staff;

import java.util.ArrayList;

/**
 * Created by W8 on 26/08/2017.
 */

public class StaffBroadcastAdapter extends RecyclerView.Adapter<StaffBroadcastAdapter.StaffBroadcastViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of staff
    private ArrayList<Staff> staffList;

    //reference ke context
    private Activity context;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public StaffBroadcastAdapter(Activity context, ArrayList<Staff> staffList)
    {
        this.context = context;
        this.staffList = staffList;
    }

    @Override
    public StaffBroadcastViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_stafflist_broadcastberita, parent, false);
        return new StaffBroadcastViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(StaffBroadcastViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi index
        if (position >= getItemCount())
            return;

        //get current item
        Staff currentStaff = staffList.get(position);

        //attach di holder
        holder.checkBox.setText(currentStaff.FULL_NAME);
        if (currentStaff.FLAG_CHECKED)
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);

        //set listener
        final int currentPos = position;
        final boolean checkBoxState = holder.checkBox.isChecked();
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                ChangeStaffState(currentPos, checkBoxState);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return staffList.size();
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulasi data
    //----------------------------------------------------------------------------------------------
    //get list staff yang diselect
    public ArrayList<String> GetSelectedUserID()
    {
        //create user list
        ArrayList<String> selectedUserID = new ArrayList<>();

        //populate user ID
        for (int i = 0; i < staffList.size(); i++)
        {
            if (staffList.get(i).FLAG_CHECKED)
                selectedUserID.add(staffList.get(i).USERID);
        }

        //return user list
        return selectedUserID;
    }

    //cek apakah semua user diselect
    public boolean IsAllUserSelected()
    {
        //count jumlah user yang diselect
        int selectedCount = 0;
        for (int i = 0; i < staffList.size(); i++)
        {
            if (staffList.get(i).FLAG_CHECKED)
                selectedCount++;
        }

        return selectedCount == getItemCount();
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //change staff state
    private void ChangeStaffState(int position, boolean checked)
    {
        //pastikan posisi nggak melebihi index
        if (position >= getItemCount())
            return;

        //ganti state staff
        staffList.get(position).FLAG_CHECKED = checked;
    }

    //handle select all input
    public void HandleInput_BroadcastAdapter_SelectAllTrigger(CheckBox checkBox)
    {
        //set all user sesuai state checkbox
        for (int i = 0; i < staffList.size(); i++)
        {
            staffList.get(i).FLAG_CHECKED = checkBox.isChecked();
        }

        //update view
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class StaffBroadcastViewHolder extends RecyclerView.ViewHolder
    {
        //checkbox
        CheckBox checkBox;

        public StaffBroadcastViewHolder(View itemView)
        {
            super(itemView);

            //get checkbox
            checkBox = itemView.findViewById(R.id.AdapterStaffBroadcast_StaffName);
        }
    }
}
