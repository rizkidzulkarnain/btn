package com.mitkoindo.smartcollection.module.assignment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.AccountAssignmentAdapter;
import com.mitkoindo.smartcollection.adapter.StaffAssignmentAdapter;
import com.mitkoindo.smartcollection.fragments.DatePickerFragment;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.helper.RecyclerViewHelper;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithFlag;
import com.mitkoindo.smartcollection.objectdata.FormAccountAssignment;
import com.mitkoindo.smartcollection.objectdata.Staff;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

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

    //popup buat assign debitur ke staff
    private AlertDialog popup_Assignment;

    //generic alert
    private GenericAlert genericAlert;

    //popup views
    private TextView view_ExpiredDate;
    private EditText view_SearchPetugasForm;
    private TextView view_AlertNoPetugas;
    private ImageView view_Popup_SearchButton;
    private ImageView view_Popup_ClearButton;

    //popup buat additional infor
    private AlertDialog popup_AdditionalInfo;
    private EditText view_Popup_AdditionalInfoForm;

    //----------------------------------------------------------------------------------------------
    //  Utilities
    //----------------------------------------------------------------------------------------------
    //date picker
    private DatePickerFragment datePickerFragment;

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

    //adapter staff list
    private StaffAssignmentAdapter staffAssignmentAdapter;

    //form assignment
    private FormAccountAssignment formAccountAssignment;

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

        //set date picker
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.SetCallerFragment(this);

        //create generic alert
        genericAlert = new GenericAlert(getActivity());
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

        //add listener pada assign button
        view_AssignButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CreatePopup_AssignToPetugas();
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
            /*accountAssignmentAdapter = new AccountAssignmentAdapter(getActivity(), debiturItems);*/
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

    //----------------------------------------------------------------------------------------------
    //  Create popup buat assign debitur ke petugas
    //----------------------------------------------------------------------------------------------
    private void CreatePopup_AssignToPetugas()
    {
        //Create form buat assign debitur
        CreateAssignDebiturForm();

        //setup dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        //set view
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_accountassignment_stafflist, null, false);
        builder.setView(popupView);

        //populate views
        SetupStaffList(popupView.findViewById(R.id.AccountAssignmentPopup_RecyclerView));

        //setup listener
        SetupStaffAssignmentListener(popupView);

        //show popup
        popup_Assignment = builder.create();
        popup_Assignment.show();
    }

    //setup list
    private void SetupStaffList(RecyclerView recyclerView)
    {
        //get staff list
        if (getActivity() instanceof AccountAssignmentActivity)
        {
            AccountAssignmentActivity thisActivity = (AccountAssignmentActivity)getActivity();
            ArrayList<Staff> staffs = thisActivity.GetClearedStaffList();

            //create adapter
            staffAssignmentAdapter = new StaffAssignmentAdapter(getActivity(), staffs);

            //set click listener pada adapter
            staffAssignmentAdapter.setClickListener(new ItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    HandleSelectStaffListener(position);
                }
            });

            //setup recyclerview
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_vertical_10dp);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(dividerDrawable);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(staffAssignmentAdapter);
        }
    }

    //handle select staff listener
    private void HandleSelectStaffListener(int position)
    {
        staffAssignmentAdapter.ChangeStaffState(position);
    }

    //setup listener pada popup
    private void SetupStaffAssignmentListener(View popupView)
    {
        //get views
        view_ExpiredDate = popupView.findViewById(R.id.Popup_Assignment_ExpiredDate);
        TextView view_AssignButton = popupView.findViewById(R.id.AccountAssignmentButton_AssignButton);
        TextView view_CancelButton = popupView.findViewById(R.id.AccountAssignmentButton_CancelButton);
        view_SearchPetugasForm = popupView.findViewById(R.id.AccountAssignmentPopup_SearchForm);
        view_AlertNoPetugas = popupView.findViewById(R.id.AccountAssignmentPopup_NoDataAlert);
        view_Popup_SearchButton = popupView.findViewById(R.id.AccountAssignmentPopup_SearchButton);
        view_Popup_ClearButton = popupView.findViewById(R.id.AccountAssignmentPopup_ClearButton);

        //set visibility
        view_AlertNoPetugas.setVisibility(View.GONE);
        view_Popup_ClearButton.setVisibility(View.GONE);

        //set listener to cancel button
        view_CancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //pastkan popup tidak null & sedang showing
                if (popup_Assignment!= null && popup_Assignment.isShowing())
                    popup_Assignment.dismiss();
            }
        });

        //set listener to expired date text
        view_ExpiredDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //show date picker
                datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        //add listener to search form
        view_SearchPetugasForm.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    SearchStaffByName();

                    return true;
                }
                return false;
            }
        });

        //add listener to search button
        view_Popup_SearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SearchStaffByName();
            }
        });

        //add listener to clear button
        view_Popup_ClearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ClearStaffSearch();
            }
        });

        //add listener to assign button
        view_AssignButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ValidateAssignmentForm();
            }
        });
    }

    //validate form
    private void ValidateAssignmentForm()
    {
        //get value of expired date
        String value_ExpiredDate = view_ExpiredDate.getText().toString();

        //cek valuenya
        if (value_ExpiredDate.equals(getString(R.string.AccountAssignment_Popup_SetExpired)))
        {
            //show alert bahwa data belum diisi
            String title = getString(R.string.Text_MohonMaaf);
            String message = getString(R.string.AccountAssignment_Alert_NoExpiredDate);
            genericAlert.DisplayAlert(message, title);
            return;
        }

        //get value petugas
        Staff selectedStaff = staffAssignmentAdapter.GetSelectedStaff();

        //cek apakah staffnya null atau tidak
        if (selectedStaff == null)
        {
            //show alert bahwa belum ada staff yang dipilih
            String title = getString(R.string.Text_MohonMaaf);
            String message = getString(R.string.AccountAssignmeny_Alert_NoPetugas);
            genericAlert.DisplayAlert(message, title);
            return;
        }

        //set selected staff
        formAccountAssignment.USERID = selectedStaff.USERID;

        //cek berapa jumlah debitur yang diselect, kalo cuma 1, tampilkan popup additional info,
        //kalo lebih dari 1, langsung kirim requestnya
        if (formAccountAssignment.count_selectedDebitur == 1)
            CreatePopup_AdditionalInfo();
        else
            CreateAccountAssignmentRequest();
    }

    //----------------------------------------------------------------------------------------------
    //  Create popup buat add additional info
    //----------------------------------------------------------------------------------------------
    //inflate popup
    private void CreatePopup_AdditionalInfo()
    {
        //setup dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        //set view
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_accountassignment_additionalinfo, null, false);
        builder.setView(popupView);

        //add listener
        AddListenerToAdditionalInfo(popupView);

        //show popup
        popup_AdditionalInfo = builder.create();
        popup_AdditionalInfo.show();
    }

    //add listener pada popup
    private void AddListenerToAdditionalInfo(View popupView)
    {
        //get views
        view_Popup_AdditionalInfoForm = popupView.findViewById(R.id.AccountAssignment_Popup_InformasiTambahanContent);
        TextView view_AssignButton = popupView.findViewById(R.id.AccountAssignment_Popup_InformasiTambahan_AssignButton);
        TextView view_CancelButton = popupView.findViewById(R.id.AccountAssignment_Popup_InformasiTambahan_CancelButton);

        //add listener to assign button
        view_AssignButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //get data note, dan send assign form
                formAccountAssignment.NOTE = view_Popup_AdditionalInfoForm.getText().toString();
                CreateAccountAssignmentRequest();
            }
        });

        //add listener to cancel button
        view_CancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //dismiss popup additional info
                if (popup_AdditionalInfo != null && popup_AdditionalInfo.isShowing())
                    popup_AdditionalInfo.dismiss();
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulasi views
    //----------------------------------------------------------------------------------------------
    //set date
    public void SetDate(Date date, String formattedDate)
    {
        //pastikan expired date tidak kosong
        if (view_ExpiredDate == null)
            return;

        //set expired date
        view_ExpiredDate.setText(formattedDate);
        formAccountAssignment.DATE_MOVE_TO = formattedDate;
    }

    //search staff by name
    private void SearchStaffByName()
    {
        //hide keyboard
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);

        //hide search button & show clear button
        view_Popup_SearchButton.setVisibility(View.GONE);
        view_Popup_ClearButton.setVisibility(View.VISIBLE);

        //get query
        String searchQuery = view_SearchPetugasForm.getText().toString();

        //search by query
        staffAssignmentAdapter.SearchStaff(searchQuery);

        //cek apakah ada data petugas atau tidak
        if (staffAssignmentAdapter.getItemCount() <= 0)
            view_AlertNoPetugas.setVisibility(View.VISIBLE);
        else
            view_AlertNoPetugas.setVisibility(View.GONE);
    }

    //clear search
    private void ClearStaffSearch()
    {
        //cek apakah ada query atau tidak
        String searchQuery = view_SearchPetugasForm.getText().toString();
        if (searchQuery.isEmpty())
        {
            //clear search
            SearchStaffByName();

            //show search button & hide clear button
            view_Popup_SearchButton.setVisibility(View.VISIBLE);
            view_Popup_ClearButton.setVisibility(View.GONE);

            //hide keyboard
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, 0);
        }
        else
        {
            //clear query
            view_SearchPetugasForm.setText("");
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulasi data
    //----------------------------------------------------------------------------------------------
    //create form buat assign debitur
    private void CreateAssignDebiturForm()
    {
        //initialize form
        formAccountAssignment = new FormAccountAssignment();

        //get data debitur yang diselect
        formAccountAssignment.count_selectedDebitur = accountAssignmentAdapter.GetSelectedDebiturCount();
        formAccountAssignment.ACCOUNT = accountAssignmentAdapter.GetSelectedDebiturAccount();
        formAccountAssignment.MOVER_USER = userID;
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat send account assignment data
    //----------------------------------------------------------------------------------------------
    private void CreateAccountAssignmentRequest()
    {
        //show alert
        genericAlert.ShowLoadingAlert();

        //send request
        new SendAccountAssignmentRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request
    private class SendAccountAssignmentRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateAccountAssignmentRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleAssignAccountResult(s);
        }
    }

    //create request object
    private JSONObject CreateAccountAssignmentRequestObject()
    {
        //set object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("USERID", formAccountAssignment.USERID);
            spParameterObject.put("ACCOUNT", formAccountAssignment.ACCOUNT);
            spParameterObject.put("MOVER_USER", formAccountAssignment.MOVER_USER);
            spParameterObject.put("DATE_MOVE_TO", formAccountAssignment.DATE_MOVE_TO);
            spParameterObject.put("NOTE", formAccountAssignment.NOTE);

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", formAccountAssignment.SPName);
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return object
        return requestObject;
    }

    //handle result
    private void HandleAssignAccountResult(String resultString)
    {
        //dismiss alert
        genericAlert.Dismiss();

        //pastikan response nggak kosong atau null
        if (resultString == null || resultString.isEmpty())
        {
            //show alert bahwa something wrong
            String title = getString(R.string.Text_MohonMaaf);
            String message = getString(R.string.Text_SomethingWrong);
            genericAlert.DisplayAlert(message, title);
            return;
        }

        //cek bad request
        if (resultString.equals("Bad Request"))
        {
            //show alert bahwa something wrong
            String title = getString(R.string.Text_MohonMaaf);
            String message = getString(R.string.Text_SomethingWrong);
            genericAlert.DisplayAlert(message, title);
            return;
        }

        try
        {
            //cek apakah response sukses atau tidak
            JSONArray responseArray = new JSONArray(resultString);

            //cek lengthnya response
            if (responseArray.length() <= 0)
            {
                //show alert bahwa something wrong
                String title = getString(R.string.Text_MohonMaaf);
                String message = getString(R.string.Text_SomethingWrong);
                genericAlert.DisplayAlert(message, title);
                return;
            }

            //get first item
            JSONObject responseObject = responseArray.getJSONObject(0);
            int responseCode = responseObject.getInt("ResponseCode");

            //cek apakah response codenya 200
            if (responseCode == 200)
            {
                //Sukses, dismiss assignment popup
                if (popup_AdditionalInfo != null && popup_AdditionalInfo.isShowing())
                    popup_AdditionalInfo.dismiss();
                if (popup_Assignment != null && popup_Assignment.isShowing())
                    popup_Assignment.dismiss();

                //show alert bahwa transaksi berhasil
                String title = getString(R.string.Text_Success);
                String message = getString(R.string.AccountAssignment_Alert_AssignmentSukses);
                genericAlert.DisplayAlert(message, title);
            }
            else
            {
                //show alert bahwa something wrong
                String title = getString(R.string.Text_MohonMaaf);
                String message = getString(R.string.Text_SomethingWrong);
                genericAlert.DisplayAlert(message, title);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show alert bahwa something wrong
            String title = getString(R.string.Text_MohonMaaf);
            String message = getString(R.string.Text_SomethingWrong);
            genericAlert.DisplayAlert(message, title);
        }
    }
}
