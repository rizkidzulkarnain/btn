package com.mitkoindo.smartcollection.module.berita;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.fragments.DatePickerFragment;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.FormBroadcastBerita;
import com.mitkoindo.smartcollection.objectdata.Staff;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class BroadcastBeritaActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //start date & expired date text
    private TextView view_StartDate;
    private TextView view_ExpiredDate;

    //filepath
    private TextView view_Filepath;

    //generic alert
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //filepath
    private ArrayList<String> data_Filepaths;

    //data form broadcast
    private FormBroadcastBerita formBroadcastBerita;

    //data staff list
    private ArrayList<Staff> staffs;

    //----------------------------------------------------------------------------------------------
    //  Utilities
    //----------------------------------------------------------------------------------------------
    //date picker
    private DatePickerFragment datePickerFragment;

    //flag date yang jadi target date picker, start atau expired date
    private boolean flag_DatePicker_StartDate;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url lainnya
    private String baseURL;
    private String url_GetStaffList;
    private String url_UploadFile;

    //auth token
    private String authToken;

    //user id
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_berita);

        //setup
        GetViews();
        SetupTransaction();

        //get staff list
        CreateGetStaffListRequest();
    }

    //get views
    private void GetViews()
    {
        //create datepicker
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.SetCallerActivity(this);

        //get start & expired date
        view_StartDate = findViewById(R.id.BroadcastBerita_StartDate);
        view_ExpiredDate = findViewById(R.id.BroadcastBerita_ExpiredDate);

        //get filepath holder
        view_Filepath = findViewById(R.id.BroadcastBerita_FileName);

        //create form
        formBroadcastBerita = new FormBroadcastBerita();

        //create generic alert
        genericAlert = new GenericAlert(this);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_GetStaffList = getString(R.string.URL_Data_StoreProcedure);
        url_UploadFile = getString(R.string.URL_UploadFile);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //open broadcast popup
    public void HandleInput_BroadcastBerita_OpenBroadcastPopup(View view)
    {
        /*//get inflater
        LayoutInflater inflater = getLayoutInflater();

        //inflate broadcast popup
        View broadcastPopup = inflater.inflate(R.layout.popup_broadcastberita, null, false);

        //setup dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set views
        builder.setView(broadcastPopup);

        //set properties
        builder.setCancelable(false);

        //create alertdialog
        AlertDialog alertDialog = builder.create();

        //show dialog
        alertDialog.show();*/

        //test upload file
        CreateSendFileRequest();
    }

    //open datepicker fragment, select start date
    public void HandleInput_BroadcastBerita_SelectStartDate(View view)
    {
        //show date picker
        datePickerFragment.show(getFragmentManager(), "datePicker");

        //set flag
        flag_DatePicker_StartDate = true;
    }

    //open datepicker fragment, select expired date
    public void HandleInput_BroadcastBerita_SelectExpiredDate(View view)
    {
        //show date picker
        datePickerFragment.show(getFragmentManager(), "datePicker");

        //set flag
        flag_DatePicker_StartDate = false;
    }

    //open filepicker
    public void HandleInput_BroadcastBerita_SelectFile(View view)
    {
        //create filepicker
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setActivityTheme(R.style.AppTheme)
                .pickFile(this);
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulasi data
    //----------------------------------------------------------------------------------------------
    //set date
    public void SetDate(Date dateRaw, String dateString)
    {
        //set date tergantung flag
        if (flag_DatePicker_StartDate)
        {
            //pastikan start date tidak melebihi expired date
            if (formBroadcastBerita.ExpiredDate != null)
            {
                if (dateRaw.after(formBroadcastBerita.ExpiredDate))
                {
                    //show alert bahwa start date tidak boleh setelah end date
                    String title = getString(R.string.Text_MohonMaaf);
                    String message = getString(R.string.Berita_Broadcast_Alert_StartDateAfterEndDate);
                    genericAlert.DisplayAlert(message, title);
                    return;
                }
            }

            //set start date
            view_StartDate.setText(dateString);

            //set data di form
            formBroadcastBerita.StartDate = dateRaw;
            formBroadcastBerita.StartDate_Formatted = dateString;
        }
        else
        {
            //pastikan expired date tidak dibawah start date
            if (formBroadcastBerita.StartDate != null)
            {
                if (dateRaw.before(formBroadcastBerita.StartDate))
                {
                    //show alert bahwa end date tidak boleh sebelum start date
                    String title = getString(R.string.Text_MohonMaaf);
                    String message = getString(R.string.Berita_Broadcast_Alert_EndDateBeforeStartDate);
                    genericAlert.DisplayAlert(message, title);
                    return;
                }
            }

            //set expired date
            view_ExpiredDate.setText(dateString);

            //set data di form
            formBroadcastBerita.ExpiredDate = dateRaw;
            formBroadcastBerita.ExpiredDate_Formatted = dateString;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    data_Filepaths = new ArrayList<>();
                    data_Filepaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

                    //set filepath
                    if (data_Filepaths.size() > 0)
                    {
                        view_Filepath.setText(data_Filepaths.get(0));
                        formBroadcastBerita.Filepath = data_Filepaths.get(0);
                    }
                }
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Handle transaksi buat get staff list
    //----------------------------------------------------------------------------------------------
    //create request
    private void CreateGetStaffListRequest()
    {
        //show loading alert
        genericAlert.ShowLoadingAlert();

        //send request buat get staff list
        new SendGetStaffListRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send requestnya
    private class SendGetStaffListRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //setup url
            String usedURL = baseURL + url_GetStaffList;

            //create request object
            JSONObject requestObject = CreateGetStaffListRequestObject();

            //execute transaction
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetStaffListRequest(s);
        }
    }

    //create request object
    private JSONObject CreateGetStaffListRequestObject()
    {
        //create requestobject
        JSONObject requestObject = new JSONObject();

        try
        {
            //create SpParameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_STAFF_LIST");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return requestobject
        return requestObject;
    }

    //handle transaction result
    private void HandleGetStaffListRequest(String resultString)
    {
        //dismiss loading alert
        genericAlert.Dismiss();

        try
        {
            //create staff arraylist
            staffs = new ArrayList<>();

            //konversi ke jsonArray
            JSONArray dataArray = new JSONArray(resultString);

            //loop seluruh data di array
            for (int i = 0; i < dataArray.length(); i++)
            {
                //create new staff data dan parse data dari JSON Object
                Staff newStaff = new Staff();
                newStaff.Parse(dataArray.getJSONObject(i));

                //add data ke list
                staffs.add(newStaff);
            }

            int x = 0;
            int y = x + 1;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Handle transaksi buat send file
    //----------------------------------------------------------------------------------------------
    //create request
    private void CreateSendFileRequest()
    {
        //pastikan filepath tidak kosong
        if (formBroadcastBerita.Filepath == null || formBroadcastBerita.Filepath.isEmpty())
            return;

        new ExecuteSendFileRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send request buat kirim file
    private class ExecuteSendFileRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create file
            File file = new File(formBroadcastBerita.Filepath);

            //setup url
            String usedURL = baseURL + url_UploadFile;

            //send file menggunakan ion
            /*Ion.with(BroadcastBeritaActivity.this)
                    .load(usedURL)
                    .setHeader("Content-Type", "multipart/form-data")
                    .setMultipartFile("file", file)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>()
                    {
                        @Override
                        public void onCompleted(Exception e, JsonObject result)
                        {
                            HandleUploadFileResult(e, result);
                        }
                    });*/
            Ion.with(BroadcastBeritaActivity.this)
                    .load(usedURL)
                    .setHeader("Content-Type", "multipart/form-data")
                    .setHeader("Authorization", "Bearer" + authToken)
                    .setMultipartFile("file", file)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result)
                        {
                            int x = 0;
                            int y = x + 1;
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }

    //handle response
    private void HandleUploadFileResult(Exception e, JsonObject result)
    {
        //text buat alert kalo gagal upload file
        String title = getString(R.string.Text_MohonMaaf);
        String message = getString(R.string.Text_UploadFailed);

        //pastikan result tidak null
        if (result == null)
        {
            //show alert bahwa upload gagal
            genericAlert.DisplayAlert(message, title);
            return;
        }

        //get string dari response server
        String resultString = result.toString();

        try
        {
            //parse result dalam bentuk JSONObject
            JSONObject resultObject = new JSONObject(resultString);

            //simpan relativepath
            formBroadcastBerita.UploadedFilepath = resultObject.getString("RelativePath");

            //ToDo :  langsung lanjut dengan upload form?
        }
        catch (JSONException e1)
        {
            e1.printStackTrace();

            //show alert bahwa upload gagal
            genericAlert.DisplayAlert(message, title);
        }
    }
}
