package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.objectdata.Staff;

import java.util.ArrayList;

public class StaffAssignmentAdapter extends RecyclerView.Adapter<StaffAssignmentAdapter.StaffAssignmentViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of staff
    private ArrayList<Staff> staffs;

    //data staff yang ditampilkan
    private ArrayList<Staff> displayedStaff;

    //reference ke context
    private Activity context;

    //----------------------------------------------------------------------------------------------
    //  Input
    //----------------------------------------------------------------------------------------------
    //click listener
    private ItemClickListener itemClickListener;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    private boolean onBind;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public StaffAssignmentAdapter(Activity context, ArrayList<Staff> staffs)
    {
        this.staffs = staffs;
        this.context = context;

        //copy list staff ke displayed staff
        displayedStaff = new ArrayList<>();
        for (int i = 0; i < staffs.size(); i++)
        {
            displayedStaff.add(staffs.get(i));
        }
    }

    @Override
    public StaffAssignmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_stafflist_assign, parent, false);
        return new StaffAssignmentViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(StaffAssignmentViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi item count
        if (position >= getItemCount())
            return;

        onBind = true;

        //get current staff
        Staff currentStaff = displayedStaff.get(position);

        //set staff name
        holder.staffName_Unselected.setText(currentStaff.FULL_NAME);
        holder.staffName_Selected.setText(currentStaff.FULL_NAME);

        //cek apakah staff selected atau tidak
        if (currentStaff.FLAG_CHECKED)
        {
            //if selected, show selected background
            holder.holder_SelectedStaff.setVisibility(View.VISIBLE);
            holder.holder_UnselectedStaff.setVisibility(View.GONE);
        }
        else
        {
            //hide selected background
            holder.holder_UnselectedStaff.setVisibility(View.VISIBLE);
            holder.holder_SelectedStaff.setVisibility(View.GONE);
        }

        onBind = false;
    }

    @Override
    public int getItemCount()
    {
        return displayedStaff.size();
    }

    //change statenya staff
    public void ChangeStaffState(int position)
    {
        //reset flag semua staff
        for (int i = 0; i < displayedStaff.size(); i++)
        {
            displayedStaff.get(i).FLAG_CHECKED = false;
        }

        //set flag selected staff ke true
        displayedStaff.get(position).FLAG_CHECKED = true;

        if (!onBind)
            notifyDataSetChanged();
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
    //  Manipulasi data
    //----------------------------------------------------------------------------------------------
    //filter staff berdasarkan nama
    public void SearchStaff(String query)
    {
        //clear displayed staff
        displayedStaff.clear();

        //isi dengan data yang namanya contains query
        for (int i = 0; i < staffs.size(); i++)
        {
            //clear flag
            staffs.get(i).FLAG_CHECKED = false;

            //cek apakah query kosong atau tidak
            if (query.isEmpty())
            {
                //kalo empty, masukkan seluruh data staff
                displayedStaff.add(staffs.get(i));
            }
            //cek apakah nama staff contains query
            else if (staffs.get(i).FULL_NAME.toLowerCase().contains(query.toLowerCase()))
            {
                displayedStaff.add(staffs.get(i));
            }
        }

        //refresh list
        if (!onBind)
            notifyDataSetChanged();
    }

    //get selected staff
    public Staff GetSelectedStaff()
    {
        //inisialisasi variable
        Staff selectedStaff = null;

        //cari staff yang diselect
        for (int i = 0; i < displayedStaff.size(); i++)
        {
            if (displayedStaff.get(i).FLAG_CHECKED)
            {
                selectedStaff = displayedStaff.get(i);
                break;
            }
        }

        //return staff
        return selectedStaff;
    }

    //----------------------------------------------------------------------------------------------
    //  view holder
    //----------------------------------------------------------------------------------------------
    class StaffAssignmentViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
    {
        View holder_SelectedStaff;
        View holder_UnselectedStaff;
        TextView staffName_Selected;
        TextView staffName_Unselected;

        public StaffAssignmentViewHolder(View itemView)
        {
            super(itemView);

            holder_SelectedStaff = itemView.findViewById(R.id.AdapterStaffAssign_SelectedStaffHolder);
            holder_UnselectedStaff = itemView.findViewById(R.id.AdapterStaffAssign_UnselectedStaffHolder);
            staffName_Selected = itemView.findViewById(R.id.AdapterStaffAssign_StaffName);
            staffName_Unselected = itemView.findViewById(R.id.AdapterStaffAssign_StaffName_Selected);

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
