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
import com.mitkoindo.smartcollection.objectdata.NotificationData;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 13/09/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //reference ke context
    private Activity context;

    //data
    private ArrayList<NotificationData> notifications = new ArrayList<>();

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //flag untuk bind data
    private boolean onBind;

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

    //----------------------------------------------------------------------------------------------
    //  Input
    //----------------------------------------------------------------------------------------------
    //click listener
    private ItemClickListener itemClickListener;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //progress bar buat initial load / refresh
    private ProgressBar view_ProgressBar;

    //recycler view
    private RecyclerView view_Recycler;

    //alert text
    private TextView view_Alert;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public NotificationAdapter(Activity context)
    {
        this.context = context;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_notifikasi, parent, false);
        return new NotificationViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi item count
        if (position >= getItemCount())
        {
            return;
        }

        //bind views
        onBind = true;

        //get current notification
        NotificationData currentNotification = notifications.get(position);

        //attach data to view
        holder.notificationText.setText(currentNotification.Message);

        //unbind views
        onBind = false;
    }

    @Override
    public int getItemCount()
    {
        return notifications.size();
    }

    //set transaction data
    public void SetTransactionData(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //set view
    public void SetViews(ProgressBar view_ProgressBar, RecyclerView view_Recycler, TextView view_Alert)
    {
        this.view_ProgressBar = view_ProgressBar;
        this.view_Recycler = view_Recycler;
        this.view_Alert = view_Alert;
    }

    //----------------------------------------------------------------------------------------------
    //  get item pada index
    //----------------------------------------------------------------------------------------------
    public NotificationData GetItemAt(int position)
    {
        if (position >= getItemCount())
            return null;

        return notifications.get(position);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get notification data
    //----------------------------------------------------------------------------------------------
    public void CreateGetNotificationRequest()
    {
        //show progress bar, hide everything else
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_Recycler.setVisibility(View.GONE);
        view_Alert.setVisibility(View.GONE);

        //send request
        new SendGetNotificationRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //----------------------------------------------------------------------------------------------
    //  Send request
    //----------------------------------------------------------------------------------------------
    //create request object
    private JSONObject CreateGetNotificationRequestObject()
    {
        //create object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            //isi request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_NOTIFICATION_LIST");
            requestObject.put("SpParameter", spParameterObject);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    private class SendGetNotificationRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetNotificationRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleTransactionResult(s);
        }
    }

    //handle result
    private void HandleTransactionResult(String resultString)
    {
        //hide progress bar
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan result tidak null
        if (resultString == null || resultString.isEmpty())
        {
            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
            return;
        }

        //pastikan juga balikan tidak 404
        if (resultString.equals("Not Found"))
        {
            view_Alert.setText(R.string.Text_NoData);
            view_Alert.setVisibility(View.VISIBLE);
            return;
        }

        try
        {
            //extract data
            JSONArray dataArray = new JSONArray(resultString);

            //pastikan jumlah data tidak nol
            if (dataArray.length() <= 0)
            {
                view_Alert.setText(R.string.Text_NoData);
                view_Alert.setVisibility(View.VISIBLE);
                return;
            }

            //create new list
            ArrayList<NotificationData> newData = new ArrayList<>();

            //extract
            for (int i = 0; i < dataArray.length(); i++)
            {
                NotificationData notificationData = new NotificationData();
                notificationData.ParseData(dataArray.getJSONObject(i));
                newData.add(notificationData);
            }

            //add new data
            AddDataToAdapter(newData);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
        }
    }

    //add data ke adapter
    private void AddDataToAdapter(ArrayList<NotificationData> newData)
    {
        //initialize list
        notifications = new ArrayList<>();

        //add all data
        for (int i = 0; i < newData.size(); i++)
        {
            notifications.add(newData.get(i));
        }

        //bind data to adapter
        if (!onBind)
            notifyDataSetChanged();

        //show item
        view_Recycler.setVisibility(View.VISIBLE);
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView notificationText;

        public NotificationViewHolder(View itemView)
        {
            super(itemView);

            notificationText = itemView.findViewById(R.id.NotifikasiAdapter_Text);

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
