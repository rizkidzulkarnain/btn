package com.mitkoindo.smartcollection.module.ptp_reminder;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.PTPReminderAdapter;
import com.mitkoindo.smartcollection.helper.RecyclerViewHelper;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithPTP;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PTPReminderActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //ptp list
    private RecyclerView view_List;

    //alert
    private TextView view_Alert;

    //progress bar
    private ProgressBar view_ProgressBar;

    //indicator buat load page baru
    private ProgressBar view_PageLoadIndicator;

    //layout yang nyimpen summary
    private View summaryLayout;

    //summary jumlah debitur
    private TextView view_SummaryDebitur;

    //summary jumlah outstanding
    private TextView view_SummaryOutstanding;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSp;

    //auth token
    private String authToken;

    //user id
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter PTP
    private PTPReminderAdapter ptpReminderAdapter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptpreminder);

        //setup
        GetViews();
        SetupTransaction();

        //get PTP data
        SetupAdapter();

        //get summary data
        CreateGetSummaryRequest();
    }

    //get views
    private void GetViews()
    {
        view_List = findViewById(R.id.PTP_Reminder_List);
        view_Alert = findViewById(R.id.PTP_Reminder_Alert);
        view_ProgressBar = findViewById(R.id.PTP_Reminder_ProgressBar);
        view_PageLoadIndicator = findViewById(R.id.PTP_Reminder_PageLoadIndicator);

        //get included layout
        summaryLayout = findViewById(R.id.PTP_Reminder_Summary);

        //get view
        view_SummaryDebitur = summaryLayout.findViewById(R.id.PTP_Summary_JumlahDebitur);
        view_SummaryOutstanding = summaryLayout.findViewById(R.id.PTP_Summary_JumlahOutstanding);

        //add listener pada recyclerview
        //add scroll listener to list
        view_List.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                //pastikan adapter tidak null
                if (ptpReminderAdapter == null)
                    return;

                //cek apakah sudah sampai item terakhir
                if (RecyclerViewHelper.isLastItemDisplaying(view_List))
                {
                    //jika iya, load new page
                    ptpReminderAdapter.CreateLoadNewPageRequest();
                }
            }
        });
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSp = getString(R.string.URL_Data_StoreProcedure);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //load user id
        userID = ResourceLoader.LoadUserID(this);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    public void HandleInput_PTPReminder_Back(View view)
    {
        finish();
    }

    //----------------------------------------------------------------------------------------------
    //  Setup adapter
    //----------------------------------------------------------------------------------------------
    //setup adapter
    private void SetupAdapter()
    {
        //create adapter & execute request
        ptpReminderAdapter = new PTPReminderAdapter(this);
        ptpReminderAdapter.SetTransactionData(baseURL, url_DataSp, authToken, userID);
        ptpReminderAdapter.SetViews(view_ProgressBar, view_PageLoadIndicator, view_List, view_Alert);
        ptpReminderAdapter.LoadInitialPTPData();

        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_List.setLayoutManager(layoutManager);
        view_List.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_List.addItemDecoration(dividerItemDecoration);
        view_List.setAdapter(ptpReminderAdapter);

        //hide progress bar & error text
        view_ProgressBar.setVisibility(View.GONE);
        view_Alert.setVisibility(View.GONE);

        //show list
        view_List.setVisibility(View.VISIBLE);
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get ptp summary
    //----------------------------------------------------------------------------------------------
    private void CreateGetSummaryRequest()
    {
        //hide summary layout
        summaryLayout.setVisibility(View.GONE);

        try
        {
            //create SP Parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            //create request object
            JSONObject requestObject = new JSONObject();
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_PTP_SUMMARY");
            requestObject.put("SpParameter", spParameterObject);

            new SendGetSummaryRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //send requestnya
    private class SendGetSummaryRequest extends AsyncTask<JSONObject, Void, String>
    {
        @Override
        protected String doInBackground(JSONObject... jsonObjects)
        {
            //set url
            String usedUrl = baseURL + url_DataSp;

            //get request object
            JSONObject requestObject = jsonObjects[0];

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedUrl);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetSummaryResult(s);
        }
    }

    //handle result
    private void HandleGetSummaryResult(String resultString)
    {
        try
        {
            //extract data
            JSONArray dataArray = new JSONArray(resultString);

            //get first object
            if (dataArray.length() <= 0)
                return;

            JSONObject dataObject = dataArray.getJSONObject(0);

            //extract
            int data_SummaryDebitur = dataObject.getInt("JumlahDebitur");
            String data_SummaryOutstanding = dataObject.getString("JumlahOutstanding");

            if (data_SummaryDebitur <= 0)
                return;

            //set value
            view_SummaryDebitur.setText("" + data_SummaryDebitur);
            view_SummaryOutstanding.setText(data_SummaryOutstanding);
            summaryLayout.setVisibility(View.VISIBLE);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get list ptp
    //----------------------------------------------------------------------------------------------
    /*private void CreateGetPTPListRequest()
    {
        //show progress, hide list & alert
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_Alert.setVisibility(View.GONE);
        view_List.setVisibility(View.GONE);

        //send request
        new SendGetPTPListRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request
    private class SendGetPTPListRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create url
            String usedURL = baseURL + url_DataSp;

            //create request object
            JSONObject requestObject = CreateGetPTPRequestObject();

            //execute request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetPTPResult(s);
        }
    }

    //create request object buat get PTP list
    private JSONObject CreateGetPTPRequestObject()
    {
        //initialize
        JSONObject requestObject = new JSONObject();

        try
        {
            //create spParameter Object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_PTP_LIST");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    //handle result
    private void HandleGetPTPResult(String resultString)
    {
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan result tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show alert
            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
            return;
        }

        //pastikan balasan tidak 404
        if (resultString.equals("Not Found"))
        {
            //show alert
            view_Alert.setText(R.string.Text_NoData);
            view_Alert.setVisibility(View.VISIBLE);
            return;
        }

        try
        {
            //extract data
            JSONArray dataArray = new JSONArray(resultString);

            //initialize data
            ArrayList<DebiturItemWithPTP> debiturItems = new ArrayList<>();

            //populate data
            for (int i = 0; i < dataArray.length(); i++)
            {
                DebiturItemWithPTP currentItem = new DebiturItemWithPTP();
                currentItem.ParseData(dataArray.getString(i));

                debiturItems.add(currentItem);
            }

            //create adapter dari item
            ptpReminderAdapter = new PTPReminderAdapter(this, debiturItems);
            SetupPTPList();

            view_List.setVisibility(View.VISIBLE);
            view_Alert.setVisibility(View.GONE);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
        }
    }*/
}
