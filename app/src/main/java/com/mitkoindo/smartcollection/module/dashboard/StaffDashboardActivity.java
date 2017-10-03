
package com.mitkoindo.smartcollection.module.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.CommonTabAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaffDashboardActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //tablayout
    private TabLayout view_Tablayout;

    //viewpager
    private ViewPager view_ViewPager;

    //option menu
    private ImageView button_OptionMenu;

    //flag untuk show toggle data akumulasi / hari ini
    private boolean flag_HideToggle = false;

    //staff name
    private TextView view_StaffName;

    //generic alert
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;
    private String url_DataView;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Fragments
    //----------------------------------------------------------------------------------------------
    private DashboardKunjunganFragment dashboardKunjunganFragment;
    private DashboardPenyelesaianFragment dashboardPenyelesaianFragment;
    private DashboardPTPFragment dashboardPTPFragment;
    private DashboardNPLFragment dashboardNPLFragment;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        //Setup
        GetViews();
        SetupTransaction();
        CreateGetRemoteConfigRequest();
    }

    //get views
    private void GetViews()
    {
        //get tab component
        view_Tablayout = findViewById(R.id.DashboardActivity_Tab);
        view_ViewPager = findViewById(R.id.DashboardActivity_ViewPager);
        view_StaffName = findViewById(R.id.DashboardStaff_StaffName);
        button_OptionMenu = findViewById(R.id.DashboardActivity_OptionMenu);

        //create generic alert
        genericAlert = new GenericAlert(this);
    }

    //setup views
    private void SetupDashboard()
    {
        //dashboard mode
        String DASHBOARDMODE_CURRENT = getString(R.string.dashboardKunjungan_Mode_Today);
        String DASHBOARDMODE_MONTH = getString(R.string.dashboardKunjungan_Mode_Monthly);
        String dashboardPenyelesaian_Current = getString(R.string.dashboardPenyelesaian_Mode_Today);
        String dashboardPenyelesaian_Month = getString(R.string.dashboardPenyelesaian_Mode_Monthly);
        String dashboardPTP_Current = getString(R.string.dashboardPTP_Mode_Today);
        String dashboardPTP_Month = getString(R.string.dashboardPTP_Mode_Monthly);

        //create fragment titles
        ArrayList<String> fragmentTitles = new ArrayList<>();
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_Kunjungan));
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_Penyelesaian));
        fragmentTitles.add(getString(R.string.Dashboard_FragmentTitle_PTP));

        //create fragments
        dashboardKunjunganFragment = new DashboardKunjunganFragment();
        dashboardKunjunganFragment.SetSpinnerToggle(flag_HideToggle);
        dashboardKunjunganFragment.SetupTransactionProperties(baseURL, url_DataSP, authToken, userID);
        dashboardKunjunganFragment.SetDashboardMode(DASHBOARDMODE_CURRENT, DASHBOARDMODE_MONTH);

        //create penyelesaian fragment
        dashboardPenyelesaianFragment = new DashboardPenyelesaianFragment();
        dashboardPenyelesaianFragment.SetSpinnerToggle(flag_HideToggle);
        dashboardPenyelesaianFragment.SetupTransactionProperties(baseURL, url_DataSP, authToken, userID);
        dashboardPenyelesaianFragment.SetDashboardMode(dashboardPenyelesaian_Current, dashboardPenyelesaian_Month);

        //create ptp fragment
        dashboardPTPFragment = new DashboardPTPFragment();
        dashboardPTPFragment.SetSpinnerToggle(flag_HideToggle);
        dashboardPTPFragment.SetupTransactionProperties(baseURL, url_DataSP, authToken, userID);
        dashboardPTPFragment.SetDashboardMode(dashboardPTP_Current, dashboardPTP_Month);

        //add fragments
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(dashboardKunjunganFragment);
        fragments.add(dashboardPenyelesaianFragment);
        fragments.add(dashboardPTPFragment);

        //create tab adapter
        CommonTabAdapter dashboardTabAdapter = new CommonTabAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

        //set adapter to tab
        view_ViewPager.setAdapter(dashboardTabAdapter);
        view_Tablayout.setupWithViewPager(view_ViewPager);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);
        url_DataView = getString(R.string.URL_Data_View);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        userID = bundle.getString("StaffID");

        String staffName = bundle.getString("StaffName");
        view_StaffName.setText(staffName);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_DashboardStaff_Back(View view)
    {
        finish();
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get remote config
    //----------------------------------------------------------------------------------------------
    private void CreateGetRemoteConfigRequest()
    {
        try
        {
            //cerate sort object
            JSONObject sortObject = new JSONObject();
            sortObject.put("Property", "ID");
            sortObject.put("Direction", "ASC");

            //create sort array, dan insert sort object
            JSONArray sortArray = new JSONArray();
            sortArray.put(sortObject);

            //create dbparam object
            JSONObject dbParamObject = new JSONObject();
            dbParamObject.put("Page", 1);
            dbParamObject.put("Limit", 10);
            dbParamObject.put("Sort", sortArray);

            //create request object
            JSONObject requestObject = new JSONObject();
            requestObject.put("DatabaseID", "db1");
            requestObject.put("ViewName", "MKI_RemoteConfig");
            requestObject.put("DBParam", dbParamObject);

            //show loading alert
            genericAlert.ShowLoadingAlert();

            //send request
            new SendGetRemoteConfigRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //send request buat get remote config
    private class SendGetRemoteConfigRequest extends AsyncTask<JSONObject, Void, String>
    {
        @Override
        protected String doInBackground(JSONObject... jsonObjects)
        {
            //set url
            String usedURL = baseURL + url_DataView;

            //get request object
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
            HandleGetRemoteConfigResult(s);
        }
    }

    //handle resultnya
    private void HandleGetRemoteConfigResult(String resultString)
    {
        //dismiss loading alert
        genericAlert.Dismiss();

        //cek jika data kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show alert
            ShowSomethingWrongAlert();
        }

        try
        {
            //extract data
            JSONArray resultArray = new JSONArray(resultString);

            //pastikan data tidak kosong
            if (resultArray.length() <= 0)
            {
                //show alert
                ShowSomethingWrongAlert();
            }

            //get first object
            JSONObject dataObject = resultArray.getJSONObject(0);

            //get field variable value
            String variableValue = dataObject.getString("VariableValue");

            //jika valuenya true, set flag hide toggle ke true
            if (variableValue.toLowerCase().equals("true"))
            {
                flag_HideToggle = true;
            }
            else
            {
                flag_HideToggle = false;
            }

            //setup dashboard
            SetupDashboard();
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show alert
            ShowSomethingWrongAlert();
        }
    }

    //show alert bahwa something wrong, dan close dashboard
    private void ShowSomethingWrongAlert()
    {
        //create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Text_SomethingWrongWithNetwork);
        builder.setCancelable(false);

        //set listener
        builder.setPositiveButton(R.string.Text_OK, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });

        //show alert
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
