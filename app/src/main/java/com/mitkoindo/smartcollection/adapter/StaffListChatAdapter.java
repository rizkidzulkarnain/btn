package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.module.chat.ChatListActivity;
import com.mitkoindo.smartcollection.objectdata.Staff;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ArrayList<Staff> staffList = new ArrayList<>();

    //----------------------------------------------------------------------------------------------
    //  Input
    //----------------------------------------------------------------------------------------------
    //click listener
    private ItemClickListener itemClickListener;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //recycler view
    private RecyclerView view_Recycler;

    //progress bar
    private ProgressBar view_ProgressBar;

    //alert
    private TextView view_AlertText;

    //flag binding data
    private boolean onBind;

    //search query
    private String searchQuery;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //construtcor
    public StaffListChatAdapter(Activity context)
    {
        this.context = context;
    }

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

        //set bind data
        onBind = true;

        //get current staff
        Staff currentStaff = staffList.get(position);

        //attach nama staff
        holder.staffName.setText(currentStaff.FULL_NAME);

        //attach level staff
        holder.staffLevel.setText(currentStaff.LEVEL);

        //release bind
        onBind = false;
    }

    @Override
    public int getItemCount()
    {
        return staffList.size();
    }

    //----------------------------------------------------------------------------------------------
    //  Setup property
    //----------------------------------------------------------------------------------------------
    //set transaksi
    public void SetTransaction(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //set views
    public void SetViews(RecyclerView view_Recycler, ProgressBar view_ProgressBar, TextView view_AlertText)
    {
        this.view_Recycler = view_Recycler;
        this.view_ProgressBar = view_ProgressBar;
        this.view_AlertText = view_AlertText;
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
    //  Create request
    //----------------------------------------------------------------------------------------------
    //create request buat get staff list
    public void CreateGetStaffRequest()
    {
        //show progress bar, hide list & alert
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_Recycler.setVisibility(View.GONE);
        view_AlertText.setVisibility(View.GONE);

        //send request
        new SendGetStaffRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create search request
    public void CreateSearchRequest(String searchQuery)
    {
        this.searchQuery = searchQuery;

        //create request
        CreateGetStaffRequest();
    }

    //----------------------------------------------------------------------------------------------
    //  Send request
    //----------------------------------------------------------------------------------------------
    //create request object
    private JSONObject CreateGetStaffRequestObject()
    {
        JSONObject requestObject = new JSONObject();

        try
        {
            //create SpParameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("keyword", searchQuery);
            spParameterObject.put("top", 9999);
            /*spParameterObject.put("userID", "BTN0013887");*/

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_STAFF_LIST");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestObject;
    }

    private class SendGetStaffRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedUrl = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetStaffRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedUrl);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetStaffRequest(s);
        }
    }

    //handle result transaksi
    private void HandleGetStaffRequest(String resultString)
    {
        //hide progress bar
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan result tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show alert
            view_AlertText.setText(R.string.Text_SomethingWrong);
            view_AlertText.setVisibility(View.VISIBLE);
            return;
        }

        if (resultString.equals("Not Found"))
        {
            //show alert
            view_AlertText.setText(R.string.Text_NoData);
            view_AlertText.setVisibility(View.VISIBLE);
            return;
        }

        try
        {
            //create staff arraylist
            ArrayList<Staff> staffs = new ArrayList<>();

            //konversi ke jsonArray
            JSONArray dataArray = new JSONArray(resultString);

            //loop seluruh data di array
            for (int i = 0; i < dataArray.length(); i++)
            {
                //create new staff data dan parse data dari JSON Object
                Staff newStaff = new Staff();
                newStaff.Parse(dataArray.getJSONObject(i));

                //add data ke list
                staffs.add(newStaff);
            }

            CreateInitualStaffData(staffs);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //show list
        view_Recycler.setVisibility(View.VISIBLE);
    }

    //Create initial staff data
    private void CreateInitualStaffData(ArrayList<Staff> newStaffs)
    {
        //clear staff list
        staffList = new ArrayList<>();

        //add data to stafflist
        for (int i = 0; i < newStaffs.size(); i++)
        {
            staffList.add(newStaffs.get(i));
        }

        //update view
        if (!onBind)
            notifyDataSetChanged();

        //show list
        view_Recycler.setVisibility(View.VISIBLE);
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class StaffListChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //staff name
        TextView staffName;
        TextView staffLevel;

        public StaffListChatViewHolder(View itemView)
        {
            super(itemView);

            staffName = itemView.findViewById(R.id.ChatListAdapter_Staffname);
            staffLevel = itemView.findViewById(R.id.ChatListAdapter_StaffLevel);

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
