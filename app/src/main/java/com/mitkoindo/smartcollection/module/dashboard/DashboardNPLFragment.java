package com.mitkoindo.smartcollection.module.dashboard;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.objectdata.DashboardNPLData;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardNPLFragment extends Fragment
{
    public DashboardNPLFragment()
    {
        // Required empty public constructor
    }

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of data
    ArrayList<DashboardNPLData> data = new ArrayList<>();

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //data npl
    private TextView view_NPL_Nasional;
    private TextView view_NPL_Kanwil;
    private TextView view_NPL_KC;

    //data saldo pokok
    private TextView view_SaldoPokok_Nasional;
    private TextView view_SaldoPokok_Kanwil;
    private TextView view_SaldoPokok_KC;

    //holder chart
    private View holder_Chart;

    //holder progress bar
    private View holder_Progress;

    //alert
    private TextView view_Alert;

    //refresher
    private SwipeRefreshLayout view_Refresher;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //userID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_dashboard_npl, container, false);;
        GetViews(thisView);
        CreateGetNPLRequest();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        view_NPL_Nasional = thisView.findViewById(R.id.DashboardNPLFragment_NasionalNPL);
        view_NPL_Kanwil = thisView.findViewById(R.id.DashboardNPLFragment_KanwilNPL);
        view_NPL_KC = thisView.findViewById(R.id.DashboardNPLFragment_kcNPL);

        view_SaldoPokok_Nasional = thisView.findViewById(R.id.DashboardNPLFragment_NasionalSaldoPokok);
        view_SaldoPokok_Kanwil = thisView.findViewById(R.id.DashboardNPLFragment_KanwilSaldoPokok);
        view_SaldoPokok_KC = thisView.findViewById(R.id.DashboardNPLFragment_kcSaldoPokok);

        //get holder
        holder_Chart = thisView.findViewById(R.id.DashboardNPLFragment_DataView);
        holder_Progress = thisView.findViewById(R.id.DashboardNPLFragment_ProgressLayout);
        view_Alert = thisView.findViewById(R.id.DashboardNPLFragment_AlertMessage);

        //get refresher
        view_Refresher = thisView.findViewById(R.id.DashboardNPLFragment_Refresher);

        //add refresh request ke refresher
        view_Refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                view_Refresher.setRefreshing(false);
                CreateGetNPLRequest();
            }
        });
    }

    //setup transaction properties
    public void SetupTransactionProperties(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get data NPL
    //----------------------------------------------------------------------------------------------
    private void CreateGetNPLRequest()
    {
        holder_Progress.setVisibility(View.VISIBLE);
        holder_Chart.setVisibility(View.GONE);
        view_Alert.setVisibility(View.GONE);

        new SendGetNPLRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request
    private class SendGetNPLRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //Create request object
            JSONObject requestObject = CreateGetNPLRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetNPLResult(s);
        }
    }

    //create request object
    private JSONObject CreateGetNPLRequestObject()
    {
        JSONObject requestObject = new JSONObject();

        try
        {
            //create spParameterObject
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DASHBOARD_NPL");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    //handle get npl result
    private void HandleGetNPLResult(String resultString)
    {
        if (getActivity() == null)
            return;

        //pastikan response tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show something wrong
            String alertMessage = getString(R.string.Text_SomethingWrong);
            view_Alert.setText(alertMessage);
            view_Alert.setVisibility(View.VISIBLE);
            holder_Progress.setVisibility(View.GONE);
            return;
        }

        //cek response
        if (resultString.equals("Not Found"))
        {
            //no data
            String alertMessage = getString(R.string.Text_NoData);
            view_Alert.setText(alertMessage);
            view_Alert.setVisibility(View.VISIBLE);
            holder_Progress.setVisibility(View.GONE);
            return;
        }

        try
        {
            //parse resultstring
            JSONArray dataArray = new JSONArray(resultString);

            //clear data
            data.clear();

            //parse each data
            for (int i = 0; i < dataArray.length(); i++)
            {
                DashboardNPLData dashboardNPLData = new DashboardNPLData();
                dashboardNPLData.Parse(dataArray.getJSONObject(i));

                //add to data
                data.add(dashboardNPLData);

                //attach data to view
                switch (dashboardNPLData.Kategori)
                {
                    case "nasional" :
                        //attach data di nasional npl
                        view_NPL_Nasional.setText(dashboardNPLData.NPL);
                        view_SaldoPokok_Nasional.setText(dashboardNPLData.SaldoPokokNPL);
                        break;
                    case "kanwil" :
                        //attach data di kanwil npl
                        view_NPL_Kanwil.setText(dashboardNPLData.NPL);
                        view_SaldoPokok_Kanwil.setText(dashboardNPLData.SaldoPokokNPL);
                        break;
                    case "kc" :
                        //attach data di kc npl
                        view_NPL_KC.setText(dashboardNPLData.NPL);
                        view_SaldoPokok_KC.setText(dashboardNPLData.SaldoPokokNPL);
                        break;
                    default:break;
                }
            }

            //show chart
            view_Alert.setVisibility(View.GONE);
            holder_Progress.setVisibility(View.GONE);
            holder_Chart.setVisibility(View.VISIBLE);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show something wrong
            String alertMessage = getString(R.string.Text_SomethingWrong);
            view_Alert.setText(alertMessage);
            view_Alert.setVisibility(View.VISIBLE);
            holder_Progress.setVisibility(View.GONE);
            return;
        }
    }
}
