package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.module.dashboard.StaffDashboardActivity;
import com.mitkoindo.smartcollection.objectdata.Staff;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 11/09/2017.
 */

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //reference ke context
    private Activity context;

    //list of debitur
    private ArrayList<Staff> staffs = new ArrayList<>();

    //flag data binding
    private boolean onBind;

    //search query
    private String searchQuery;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url yang digunakan
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //page counter
    private int currentPage;

    //flag boleh load page baru atau tidak
    private boolean flag_AllowLoadNewPage;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //progress bar buat initial load / refresh
    private ProgressBar view_ProgressBar;

    //progress bar buat load page baru
    private ProgressBar view_PageLoadIndicator;

    //recycler view
    private RecyclerView view_Recycler;

    //alert text
    private TextView view_Alert;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public StaffAdapter(Activity context)
    {
        this.context = context;
        currentPage = 1;
        flag_AllowLoadNewPage = false;
    }

    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_staff, parent, false);
        return new StaffViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(StaffViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi index
        if (position >= getItemCount())
            return;

        //bind data
        onBind = true;

        //get current staff
        Staff currentStaff = staffs.get(position);

        //attach data
        holder.staffName.setText(currentStaff.FULL_NAME);
        holder.staffGroup.setText(currentStaff.GROUP);

        //add listener
        final int currentPos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenStaffDashboard(currentPos);
            }
        });

        //un-bind data
        onBind = false;
    }

    @Override
    public int getItemCount()
    {
        return staffs.size();
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //open staff dashboard
    private void OpenStaffDashboard(int position)
    {
        //get current staff
        Staff currentStaff = staffs.get(position);

        //open intent
        Intent intent = new Intent(context, StaffDashboardActivity.class);
        intent.putExtra("StaffID", currentStaff.USERID);
        context.startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    //  Set property
    //----------------------------------------------------------------------------------------------
    //set transaction data
    public void SetTransactionData(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //set view
    public void SetViews(ProgressBar view_ProgressBar, ProgressBar view_PageLoadIndicator,
                         RecyclerView view_Recycler, TextView view_Alert)
    {
        this.view_ProgressBar = view_ProgressBar;
        this.view_PageLoadIndicator = view_PageLoadIndicator;
        this.view_Recycler = view_Recycler;
        this.view_Alert = view_Alert;
    }

    //----------------------------------------------------------------------------------------------
    //  Create request
    //----------------------------------------------------------------------------------------------
    public void LoadInitialData()
    {
        view_Recycler.setVisibility(View.GONE);
        view_Alert.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.VISIBLE);

        new SendGetDataRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    public void CreateSearchRequest(String searchQuery)
    {
        this.searchQuery = searchQuery;
        LoadInitialData();
    }
    //----------------------------------------------------------------------------------------------
    //  Execute request
    //----------------------------------------------------------------------------------------------
    //create request object buat get staff
    private JSONObject CreateGetDataRequestObject()
    {
        //inisialisasi request object
        JSONObject requestObject = new JSONObject();

        //populate
        try
        {
            //create SpParameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("keyword", searchQuery);
            spParameterObject.put("top", 100);
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

        //return request object
        return requestObject;
    }

    //create request buat get staff data
    private class SendGetDataRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedUrl = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetDataRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedUrl);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetDataResult(s);
        }
    }

    //handle result get data
    private void HandleGetDataResult(String resultString)
    {
        //hide progress bar
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan result tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show alert
            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
            return;
        }

        try
        {
            //create staff arraylist
            ArrayList<Staff> newStaffs = new ArrayList<>();

            //konversi ke jsonArray
            JSONArray dataArray = new JSONArray(resultString);

            //loop seluruh data di array
            for (int i = 0; i < dataArray.length(); i++)
            {
                //create new staff data dan parse data dari JSON Object
                Staff newStaff = new Staff();
                newStaff.Parse(dataArray.getJSONObject(i));

                //add data ke list
                newStaffs.add(newStaff);
            }

            AttachStaffData(newStaffs);
        }
        catch (JSONException e)
        {
            //show alert
            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    //attach staff data
    private void AttachStaffData(ArrayList<Staff> newStaffs)
    {
        staffs = new ArrayList<>();
        for (int i = 0; i < newStaffs.size(); i++)
        {
            staffs.add(newStaffs.get(i));
        }

        if (!onBind)
            notifyDataSetChanged();

        //show list
        view_Recycler.setVisibility(View.VISIBLE);
        view_Alert.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class StaffViewHolder extends RecyclerView.ViewHolder
    {
        TextView staffName;
        TextView staffGroup;

        public StaffViewHolder(View itemView)
        {
            super(itemView);

            //get views
            staffName = itemView.findViewById(R.id.StaffAdapter_Staffname);
            staffGroup = itemView.findViewById(R.id.StaffAdapter_Group);
        }
    }
}
