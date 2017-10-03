package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.HomeActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.module.chat.ChatListActivity;
import com.mitkoindo.smartcollection.objectdata.Staff;
import com.mitkoindo.smartcollection.objectdata.StaffOnChat;
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
    private ArrayList<StaffOnChat> staffList = new ArrayList<>();

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
        /*this.staffList = staffList;*/
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
        StaffOnChat currentStaff = staffList.get(position);

        //attach nama staff
        holder.staffName.setText(currentStaff.FULL_NAME);

        //attach level staff
        holder.staffLevel.setText(currentStaff.LEVEL);

        if (currentStaff.totalChat == null || currentStaff.totalChat.isEmpty() || currentStaff.totalChat.equals("null"))
        {
            holder.holder_TotalChat.setVisibility(View.GONE);
        }
        else
        {
            holder.totalChat.setText(currentStaff.totalChat);
            holder.holder_TotalChat.setVisibility(View.VISIBLE);
        }

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

    //set total message jadi null
    public void SetTotalChatToNull(int index)
    {
        //prevent out of bounds
        if (index >= getItemCount())
            return;

        //set total chat di selected staff jadi null
        staffList.get(index).totalChat = null;

        //update
        if (!onBind)
            notifyDataSetChanged();
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
        if (resultString == null || resultString.isEmpty()  || resultString.equals("Bad Request"))
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
            ArrayList<StaffOnChat> staffs = new ArrayList<>();

            //konversi ke jsonArray
            JSONArray dataArray = new JSONArray(resultString);

            //loop seluruh data di array
            for (int i = 0; i < dataArray.length(); i++)
            {
                //create new staff data dan parse data dari JSON Object
                StaffOnChat newStaff = new StaffOnChat();
                newStaff.Parse(dataArray.getJSONObject(i));

                //add data ke list
                staffs.add(newStaff);
            }

            CreateInitialStaffData(staffs);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //create request buat get chat badge
        CreateGetChatBadgeRequest();

        //show list
        view_Recycler.setVisibility(View.VISIBLE);
    }

    //Create initial staff data
    private void CreateInitialStaffData(ArrayList<StaffOnChat> newStaffs)
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
        /*view_Recycler.setVisibility(View.VISIBLE);*/
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get chat badge
    //----------------------------------------------------------------------------------------------
    private void CreateGetChatBadgeRequest()
    {
        JSONObject requestObject = new JSONObject();
        try
        {
            //create spParameterObject
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_NOTIFICATION_CHAT");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        new SendGetChatBadgeRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestObject);
    }

    private class SendGetChatBadgeRequest extends AsyncTask<JSONObject, Void, String>
    {
        @Override
        protected String doInBackground(JSONObject... jsonObjects)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //get request object
            JSONObject requestObject = jsonObjects[0];

            //Send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetBadgeResult(s);
        }
    }

    //handle result dari get chat
    private void HandleGetBadgeResult(String resultString)
    {
        try
        {
            //extract badge
            JSONArray dataArray = new JSONArray(resultString);

            //loop all data
            for (int i = 0; i < dataArray.length(); i++)
            {
                //extract user ID dan chat counter
                JSONObject currentData = dataArray.getJSONObject(i);
                String fromUserID = currentData.getString("FromUserID");
                String totalChat = currentData.getString("TotalChat");

                //cari user yang IDnya sama
                for (int j = 0; j < staffList.size(); j++)
                {
                    StaffOnChat currentStaff = staffList.get(j);

                    if (fromUserID.toLowerCase().equals(currentStaff.USERID.toLowerCase()))
                    {
                        currentStaff.totalChat = totalChat;
                        break;
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //update view
        if (!onBind)
            notifyDataSetChanged();

        //show list
        view_Recycler.setVisibility(View.VISIBLE);

        CreateReadNotificationRequest();
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat set notifikasi chat jadi "read"
    //----------------------------------------------------------------------------------------------
    private void CreateReadNotificationRequest()
    {
        //create request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create spParameterObject
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("pageType", "PageChat");

            //setup request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_NOTIFICATION_READ_BY_TYPE");
            requestObject.put("SpParameter", spParameterObject);

            //send request
            new SendReadNotificationRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //send request
    private class SendReadNotificationRequest extends AsyncTask<JSONObject, Void, String>
    {
        @Override
        protected String doInBackground(JSONObject... jsonObjects)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //set requestObject
            JSONObject requestObject = jsonObjects[0];

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class StaffListChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //staff name
        TextView staffName;
        TextView staffLevel;
        TextView totalChat;
        View holder_TotalChat;

        public StaffListChatViewHolder(View itemView)
        {
            super(itemView);

            staffName = itemView.findViewById(R.id.ChatListAdapter_Staffname);
            staffLevel = itemView.findViewById(R.id.ChatListAdapter_StaffLevel);
            totalChat = itemView.findViewById(R.id.ChatListAdapter_BadgeCounter);
            holder_TotalChat = itemView.findViewById(R.id.ChatListAdapter_BadgeHolder);

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
