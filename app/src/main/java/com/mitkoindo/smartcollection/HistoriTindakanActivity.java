package com.mitkoindo.smartcollection;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mitkoindo.smartcollection.adapter.HistoriTindakanAdapter;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.HistoriTindakan;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoriTindakanActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //recycler view
    private RecyclerView list_HistoriTindakan;

    //generic alert
    private GenericAlert genericAlert;

    //no data text
    private TextView view_NoData;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url & url buat get histori tindakan
    private String baseURL;
    private String url_Data_StoreProcedure;

    //auth token
    private String authToken;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //data nomor rekening
    private String noRekening;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori_tindakan);

        //setup
        GetViews();
        SetupTransaction();
        GetExtra();
    }

    //get views
    private void GetViews()
    {
        //get recycler view
        list_HistoriTindakan = (RecyclerView)findViewById(R.id.HistoriTindakan_ListTindakan);

        //create alert
        genericAlert = new GenericAlert(this);

        //get no data text
        view_NoData = findViewById(R.id.HistoriTindakan_NoData);
    }

    //setup views
    private void SetupTransaction()
    {
        //get url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_Data_StoreProcedure = getString(R.string.URL_Data_StoreProcedure);

        //get auth token
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        authToken = sharedPreferences.getString(key_AuthToken, "");

        //show loading alert & create request
        genericAlert.ShowLoadingAlert();
        new SendGetHistoriTindakanRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //get extra
    private void GetExtra()
    {
        //get bundle data
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        //get no. rekening
        noRekening = bundle.getString(DetailDebiturActivity.EXTRA_NO_REKENING, "");
    }

    //----------------------------------------------------------------------------------------------
    //  Handle Input
    //----------------------------------------------------------------------------------------------
    //handle back button
    public void HandleInput_HistoriTindakan_Back(View view)
    {
        finish();
    }

    //----------------------------------------------------------------------------------------------
    //  Create request untuk get histori tindakan
    //----------------------------------------------------------------------------------------------
    private class SendGetHistoriTindakanRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create request object
            JSONObject requestObject = CreateGetHistoriTindakanRequestObject();

            //create url
            String usedURL = baseURL + url_Data_StoreProcedure;

            //start transaction
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetHistoriTindakanResult(s);
        }
    }

    //create request object
    private JSONObject CreateGetHistoriTindakanRequestObject()
    {
        JSONObject requestObject = new JSONObject();

        try
        {
            //populate object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_HISTORI_TINDAKAN");

            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("nomorRekening", noRekening);
            /*spParameterObject.put("nomorRekening", "3501010002958");*/

            //put spparameter object
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestObject;
    }

    //handle result
    private void HandleGetHistoriTindakanResult(String jsonString)
    {
        //dismiss alert
        genericAlert.Dismiss();

        try
        {
            view_NoData.setVisibility(View.GONE);
            list_HistoriTindakan.setVisibility(View.VISIBLE);

            //test parse jsonString
            JSONArray dataArray = new JSONArray(jsonString);

            if (dataArray.length() <= 0)
            {
                view_NoData.setVisibility(View.VISIBLE);
                list_HistoriTindakan.setVisibility(View.GONE);
            }

            //extract data
            ArrayList<HistoriTindakan> all_Tindakan = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++)
            {
                HistoriTindakan historiTindakan = new HistoriTindakan();
                historiTindakan.ParseData(dataArray.getString(i));
                all_Tindakan.add(historiTindakan);
            }

            //create adapter
            HistoriTindakanAdapter adapter = new HistoriTindakanAdapter(this, all_Tindakan);

            //attach adapter to recyclerview
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            list_HistoriTindakan.setLayoutManager(layoutManager);
            list_HistoriTindakan.setItemAnimator(new DefaultItemAnimator());
            Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(dividerDrawable);
            list_HistoriTindakan.addItemDecoration(dividerItemDecoration);
            list_HistoriTindakan.setAdapter(adapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            view_NoData.setVisibility(View.VISIBLE);
            list_HistoriTindakan.setVisibility(View.GONE);
        }
    }
}
