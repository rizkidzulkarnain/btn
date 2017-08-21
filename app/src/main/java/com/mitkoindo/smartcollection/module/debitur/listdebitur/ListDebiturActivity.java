package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityListDebiturBinding;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class ListDebiturActivity extends BaseActivity {

    private ListDebiturViewModel mListDebiturViewModel;
    private ActivityListDebiturBinding mBinding;
    private FastItemAdapter<IItem> mAdapter = new FastItemAdapter<>();
    private FastItemAdapter mFastAdapter;

    //spinner buat milih status
    //temporary, harusnya ini pake tab
    private Spinner spinner_Status;
    private String selectedStatus = "PENDING";

    //no data text
    private TextView view_NoData;

    public static Intent instantiate(Context context) {
        Intent intent = new Intent(context, ListDebiturActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.ListDebitur_PageTitle));
        setupRecyclerView();

        SetupSpinner();

        SetupTransaction();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        mListDebiturViewModel = new ListDebiturViewModel();
        addViewModel(mListDebiturViewModel);
        mBinding = DataBindingUtil.bind(contentView);
    }

    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter();

//        List<DebiturItem> items = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            DebiturItem item = new DebiturItem();
//            item.setNama("Indra Susilo Setiawan");
//            item.setNoRekening("172371237");
//            item.setTagihan(1500000);
//            item.setDpd(25);
//            item.setLastPayment("2017-08-17");
//
//            items.add(item);
//        }
//        fastAdapter.add(items);

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewDebitur.addItemDecoration(itemDecoration);
        mBinding.recyclerViewDebitur.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerViewDebitur.setAdapter(mFastAdapter);

        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<DebiturItem>() {
            @Override
            public boolean onClick(View v, IAdapter<DebiturItem> adapter, DebiturItem item, int position) {
                startActivity(DetailDebiturActivity.instantiate(ListDebiturActivity.this, item.getNoRekening()));

                return true;
            }
        });
    }

    //set status ke spinner
    private void SetupSpinner()
    {
        view_NoData = findViewById(R.id.ListDebitur_NoData);
        spinner_Status = findViewById(R.id.ListDebitur_Spinner_Status);

        //Pending, Lancar, Reassignment, Matured, InProgress,
        final String[] all_Status = new String[]
                {
                    "PENDING", "LANCAR", "REASSIGNMENT", "MATURED", "INPROGRESS"
                };

        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(this, R.layout.preset_spinneritem, all_Status);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Status.setAdapter(spinnerAdapter);
        spinner_Status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                //set status ke selected status
                selectedStatus = all_Status[i];

                //refresh list data
                showLoadingDialog();
                new ListDebiturActivity.SendGetListDebiturRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private String baseURL;
    private String url_Data_StoreProcedure;
    private String authToken;
    private String userID;
    private void SetupTransaction()
    {
        //get url
        /*baseURL = getString(R.string.BaseURL);*/
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_Data_StoreProcedure = getString(R.string.URL_Data_StoreProcedure);

        //get auth token
        String key_AuthToken = getString(R.string.SharedPreferenceKey_AccessToken);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        authToken = sharedPreferences.getString(key_AuthToken, "");

        //get User id
        String key_UserID = getString(R.string.SharedPreferenceKey_UserID);
        userID = sharedPreferences.getString(key_UserID, "");

        //show loading alert & create request
        showLoadingDialog();
        new ListDebiturActivity.SendGetListDebiturRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    private class SendGetListDebiturRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create request object
            JSONObject requestObject = CreateGetListDebiturRequestObject();

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
            HandleGetListDebiturResult(s);
        }
    }

    private JSONObject CreateGetListDebiturRequestObject()
    {
        JSONObject requestObject = new JSONObject();

        try
        {
            //populate object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_LIST");

            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            /*spParameterObject.put("orderBy", "C.CU_CIF");
            spParameterObject.put("page", 1);
            spParameterObject.put("limit", 10);
            spParameterObject.put("orderDirection", "ASC");*/
            spParameterObject.put("status", selectedStatus);

            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestObject;
    }

    private void HandleGetListDebiturResult(String jsonString)
    {
        //dismiss alert
        hideLoadingDialog();

        mFastAdapter.clear();
        try
        {
            //test parse jsonString
            JSONArray dataArray = new JSONArray(jsonString);

            //cek length data array
            if (dataArray.length() <= 0)
            {
                //show no data text
                view_NoData.setVisibility(View.VISIBLE);
            }

            //hide no data text
            view_NoData.setVisibility(View.GONE);

            //extract data
            ArrayList<DebiturItem> all_Tindakan = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++)
            {
                DebiturItem debiturItem = new DebiturItem();
                debiturItem.ParseData(dataArray.getString(i));
                all_Tindakan.add(debiturItem);
                mFastAdapter.add(debiturItem);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show no data text
            view_NoData.setVisibility(View.VISIBLE);
        }
    }
}
