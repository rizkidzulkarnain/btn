package com.mitkoindo.smartcollection.module.assignment;


import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.AccountAssignmentAdapter;
import com.mitkoindo.smartcollection.helper.RecyclerViewHelper;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithFlag;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignedDebiturFragment extends Fragment
{
    public AssignedDebiturFragment()
    {
        // Required empty public constructor
    }

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //refresher
    private SwipeRefreshLayout view_Refresher;

    //recyclerview
    private RecyclerView view_Recycler;

    //progress bar
    private ProgressBar view_ProgressBar;

    //alert text
    private TextView view_Alert;

    //search form
    private EditText view_SearchForm;

    //search button
    private ImageView view_SearchButton;

    //clear button
    private ImageView view_ClearButton;

    //assign button
    private Button view_AssignButton;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token;
    private String authToken;

    //user id
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //adapter
    private AccountAssignmentAdapter accountAssignmentAdapter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_assigned_debitur, container, false);
        GetViews(thisView);
        SetupListener();
        CreateGetListDebiturRequest();
        return thisView;
    }

    //get views
    private void GetViews(View thisView)
    {
        view_Refresher = thisView.findViewById(R.id.AssignedDebiturFragment_Refresher);
        view_Recycler = thisView.findViewById(R.id.AssignedDebiturFragment_List);
        view_ProgressBar = thisView.findViewById(R.id.AssignedDebiturFragment_ProgressBar);
        view_Alert = thisView.findViewById(R.id.AssignedDebiturFragment_AlertView);
        view_SearchForm = thisView.findViewById(R.id.AssignedDebiturFragment_SearchForm);
        view_SearchButton = thisView.findViewById(R.id.AssignedDebiturFragment_SearchButton);
        view_ClearButton = thisView.findViewById(R.id.AssignedDebiturFragment_ClearButton);
        view_AssignButton = thisView.findViewById(R.id.AssignedDebiturFragment_AssignButton);
    }

    //Setup listener
    private void SetupListener()
    {
        //set listener pada swipe refresh
        view_Refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                CreateGetListDebiturRequest();
            }
        });

        //add load new page listener
        view_Recycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                if (accountAssignmentAdapter == null)
                    return;

                //load new data
                if (RecyclerViewHelper.isLastItemDisplaying(view_Recycler))
                {
                    //load new data
                }
            }
        });

        //add listener to search form
        view_SearchForm.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    //Handle search
                    /*CreateSearchRequest();*/

                    return true;
                }

                return false;
            }
        });

        //add listener pada search button
        view_SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleInput_SearchButton();
            }
        });

        //add listener pada clear button
        view_ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleInput_ClearButton();
            }
        });
    }

    //setup transaction property
    public void SetupTransactionProperty(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //Handle input pada search button
    private void HandleInput_SearchButton()
    {
        /*CreateSearchRequest();*/
    }

    //Handle input pada clear button
    private void HandleInput_ClearButton()
    {
        //jika search form tidak kosong, clear textnya
        if (!view_SearchForm.getText().toString().isEmpty())
        {
            view_SearchForm.setText("");
        }
        else
        {
            //jika kosong, show search button & hide clear button
            view_ClearButton.setVisibility(View.GONE);
            view_SearchForm.setVisibility(View.VISIBLE);

            //dan refresh data
            /*CreateSearchRequest();*/
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get list debitur
    //----------------------------------------------------------------------------------------------
    private void CreateGetListDebiturRequest()
    {
        //set views
        view_Refresher.setRefreshing(false);
        view_Alert.setVisibility(View.GONE);
        view_Recycler.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.VISIBLE);

        //send request buat get list debitur
        new SendGetListDebitureRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request buat get list debitur
    private class SendGetListDebitureRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetDebiturRequestObject();

            //execute request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetDebiturResult(s);
        }
    }

    //create request object
    private JSONObject CreateGetDebiturRequestObject()
    {
        //create request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("status", "'PENDING'");

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_LIST");
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
    private void HandleGetDebiturResult(String result)
    {
        //pastikan result nggak kosong
        if (result == null || result.isEmpty())
        {
            return;
        }

        //pastikan response nggak 404
        if (result.equals("404"))
        {
            return;
        }

        try
        {
            //parse result ke json array
            JSONArray resultArray = new JSONArray(result);

            //initialize array
            ArrayList<DebiturItemWithFlag> debiturItems = new ArrayList<>();

            //add item to debitur
            for (int i = 0; i < resultArray.length(); i++)
            {
                //parse item
                DebiturItemWithFlag debiturItem = new DebiturItemWithFlag();
                debiturItem.ParseData(resultArray.getString(i));

                //add to list
                debiturItems.add(debiturItem);
            }

            //create adapter
            accountAssignmentAdapter = new AccountAssignmentAdapter(getActivity(), debiturItems);
            accountAssignmentAdapter.SetAssignButton(view_AssignButton);
            SetupRecyclerView();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //setup views
    private void SetupRecyclerView()
    {
        //attach adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        view_Recycler.setLayoutManager(layoutManager);
        view_Recycler.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_Recycler.addItemDecoration(dividerItemDecoration);
        view_Recycler.setAdapter(accountAssignmentAdapter);

        //hide progress bar & error text
        view_ProgressBar.setVisibility(View.GONE);
        view_Alert.setVisibility(View.GONE);

        //show list
        view_Recycler.setVisibility(View.VISIBLE);
    }
}
