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
    //  Views
    //----------------------------------------------------------------------------------------------
    private boolean onBind;

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

        onBind = true;

        //get current item
        Staff currentStaff = staffList.get(position);

        //set listener
        final int currentPos = position;

        //cek apakah data selain group null atau tidak
        if (currentStaff.FULL_NAME == null || currentStaff.FULL_NAME.isEmpty())
        {
            //show groupCheckbox & hide staffcheckbox
            holder.groupCheckBox.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);

            //set value
            holder.groupCheckBox.setText(currentStaff.GROUP_NAME);
            if (currentStaff.FLAG_CHECKED)
                holder.groupCheckBox.setChecked(true);
            else
                holder.groupCheckBox.setChecked(false);

            /*//add listener pada checkbox
            holder.groupCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    ChangeStaffState(currentPos, compoundButton.isChecked());
                }
            });*/

            onBind = false;
            return;
        }

        //hide group checkbox dan show name checkbox
        holder.groupCheckBox.setVisibility(View.GONE);
        holder.checkBox.setVisibility(View.VISIBLE);

        //attach di holder
        holder.checkBox.setText(currentStaff.FULL_NAME);
        if (currentStaff.FLAG_CHECKED)
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);

        /*holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                ChangeStaffState(currentPos, compoundButton.isChecked());
            }
        });*/

        onBind = false;
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
            Staff currentStaff = staffList.get(i);
            if (currentStaff.USERID != null && !currentStaff.USERID.isEmpty())
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
        int allStaffCount = 0;
        for (int i = 0; i < staffList.size(); i++)
        {
            Staff currentStaff = staffList.get(i);

            if (currentStaff.USERID != null && !currentStaff.USERID.isEmpty())
            {
                if (currentStaff.FLAG_CHECKED)
                    selectedCount++;

                allStaffCount++;
            }

        }

        return selectedCount == allStaffCount;
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

        //cek apakah entry ini group atau bukan
        if (staffList.get(position).FULL_NAME == null || staffList.get(position).FULL_NAME.isEmpty())
        {
            //get group name
            String groupName = staffList.get(position).GROUP_NAME;

            //change state seluruh item yang ada di dalam group ini
            for (int i = 0; i < staffList.size(); i++)
            {
                //cek apakah groupnya sama atau bukan
                if (groupName.equals(staffList.get(i).GROUP_NAME))
                {
                    //set flag statenya biar sama
                    staffList.get(i).FLAG_CHECKED = checked;
                }
            }
        }

        if (!onBind)
            notifyDataSetChanged();
    }

    //handle select all input
    public void HandleInput_BroadcastAdapter_SelectAllTrigger(CheckBox checkBox)
    {
        boolean selectedFlag = checkBox.isChecked();

        //set all user sesuai state checkbox
        for (int i = 0; i < staffList.size(); i++)
        {
            staffList.get(i).FLAG_CHECKED = selectedFlag;
        }

        //update view
        if (!onBind)
            notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class StaffBroadcastViewHolder extends RecyclerView.ViewHolder implements CheckBox.OnCheckedChangeListener
    {
        //checkbox
        CheckBox checkBox;
        CheckBox groupCheckBox;

        public StaffBroadcastViewHolder(View itemView)
        {
            super(itemView);

            //get checkbox
            checkBox = itemView.findViewById(R.id.AdapterStaffBroadcast_StaffName);
            groupCheckBox = itemView.findViewById(R.id.AdapterStaffBroadcast_GroupName);

            checkBox.setOnCheckedChangeListener(this);
            groupCheckBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            ChangeStaffState(getAdapterPosition(), b);
        }
    }
}
