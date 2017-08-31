package com.mitkoindo.smartcollection.module.berita;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.adapter.StaffBroadcastAdapter;
import com.mitkoindo.smartcollection.fragments.DatePickerFragment;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.objectdata.FormBroadcastBerita;
import com.mitkoindo.smartcollection.objectdata.Staff;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.HttpsTrustManager;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;


import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class BroadcastBeritaActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //form
    private TextView view_Title;
    private TextView view_Content;

    //start date & expired date text
    private TextView view_StartDate;
    private TextView view_ExpiredDate;

    //filepath
    private TextView view_Filepath;

    //generic alert
    private GenericAlert genericAlert;

    //popup buat milih petugas yang akan dikirimi berita
    private AlertDialog sendPopup;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //filepath
    private ArrayList<String> data_Filepaths;

    //data form broadcast
    private FormBroadcastBerita formBroadcastBerita;

    //data staff list
    private ArrayList<Staff> staffs;

    //adapter staff list
    private StaffBroadcastAdapter staffBroadcastAdapter;

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
    private String url_DataSP;
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
        //get title & content
        view_Title = findViewById(R.id.BroadcastBerita_Title);
        view_Content = findViewById(R.id.BroadcastBerita_Isi);

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
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);
        url_UploadFile = getString(R.string.URL_UploadFile);

        //load auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);

        Ion.getDefault(this).getConscryptMiddleware().enable(false);
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    //open broadcast popup
    public void HandleInput_BroadcastBerita_OpenBroadcastPopup(View view)
    {
        //get data dari form
        formBroadcastBerita.Judul = view_Title.getText().toString();
        formBroadcastBerita.Isi = view_Content.getText().toString();

        //pastikan judul, isi, dan start / end date sudah diisi
        if (formBroadcastBerita.Judul.isEmpty() ||
                formBroadcastBerita.Isi.isEmpty() ||
                formBroadcastBerita.StartDate == null ||
                formBroadcastBerita.ExpiredDate == null)
        {
            //show alert bahwa isian belum lengkap
            String alertTitle = getString(R.string.Text_MohonMaaf);
            String alertMessage = getString(R.string.Text_IsianBelumLengkap);
            genericAlert.DisplayAlert(alertMessage, alertTitle);
            return;
        }

        //get inflater
        LayoutInflater inflater = getLayoutInflater();

        //inflate broadcast popup
        View broadcastPopup = inflater.inflate(R.layout.popup_broadcastberita, null, false);

        //attach data staff list
        AttachStaffListData(broadcastPopup);

        //Add listener
        AddListenerToBroadcastPopup(broadcastPopup);

        //setup dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set views
        builder.setView(broadcastPopup);

        //set properties
        builder.setCancelable(false);

        //create alertdialog
        sendPopup = builder.create();

        //show dialog
        sendPopup.show();

        /*//test upload file
        CreateSendFileRequest();*/
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

    //Listener pada send broadcast popup
    private void AddListenerToBroadcastPopup(View broadcastPopup)
    {
        //get cancel button & add listener
        Button cancelButton = broadcastPopup.findViewById(R.id.BroadcastPopup_BatalKirim);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendPopup.dismiss();
            }
        });

        //get send button & add listener
        Button sendButton = broadcastPopup.findViewById(R.id.BroadcastPopup_KirimBerita);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //cek ada attachment atau tidak
                if (formBroadcastBerita.Filepath == null || formBroadcastBerita.Filepath.isEmpty())
                    CreateSendBroadcastRequest();
                else
                    CreateSendFileRequest();
            }
        });

        //add listener ke checkbox
        final CheckBox selectAllButton = broadcastPopup.findViewById(R.id.BroadcastPopup_SelectAll);
        selectAllButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                staffBroadcastAdapter.HandleInput_BroadcastAdapter_SelectAllTrigger(selectAllButton);
            }
        });
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

    //attach staff list data di send popup
    private void AttachStaffListData(View popupView)
    {
        //get recyclerviw
        RecyclerView view_List = popupView.findViewById(R.id.BroadcastPopup_ListPetugas);

        //create list adapter
        staffBroadcastAdapter = new StaffBroadcastAdapter(this, staffs);

        //attach stafflist di popup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_List.setLayoutManager(layoutManager);
        view_List.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_vertical_10dp);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        view_List.addItemDecoration(dividerItemDecoration);
        view_List.setAdapter(staffBroadcastAdapter);
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
            String usedURL = baseURL + url_DataSP;

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
            /*spParameterObject.put("userID", userID);*/
            spParameterObject.put("userID", "BTN0013887");

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

            //--------------------------------------------------------------------------------------
            //  Normal procedure
            //--------------------------------------------------------------------------------------
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
            //--------------------------------------------------------------------------------------

            //--------------------------------------------------------------------------------------
            //  Ignoring certificate
            //--------------------------------------------------------------------------------------
            /*Ion ionClient = Ion.getDefault(BroadcastBeritaActivity.this);
            ionClient.getHttpClient().getSSLSocketMiddleware().setTrustManagers(new TrustManager[] {new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }});

            ionClient.build(BroadcastBeritaActivity.this)
                    .load(usedURL)
                    .setHeader("ContentType", "multipart/form-data")
                    .setHeader("Authorization", "Bearer" + authToken)
                    .setMultipartFile("file", file)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result)
                        {
                            HandleUploadFileResult(e, result);
                        }
                    });*/

            /*Ion.getDefault(BroadcastBeritaActivity.this).getHttpClient().getSSLSocketMiddleware().setTrustManagers(new TrustManager[] {new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {}

                @Override
                public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }});*/

             //ignore certificate
            /*HttpsTrustManager.allowAllSSL();*/

            Ion.with(BroadcastBeritaActivity.this)
                    .load(usedURL)
                    .setHeader("ContentType", "multipart/form-data")
                    .setHeader("Authorization", "Bearer" + authToken)
                    .setMultipartFile("file", file)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result)
                        {
                            HandleUploadFileResult(e, result);
                        }
                    });
            //--------------------------------------------------------------------------------------

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }

    //handle response
    private void HandleUploadFileResult(Exception e, String result)
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

        try
        {
            //parse result dalam bentuk JSONObject
            JSONObject resultObject = new JSONObject(result);

            //simpan relativepath
            formBroadcastBerita.UploadedFilepath = resultObject.getString("RelativePath");
            CreateSendBroadcastRequest();
        }
        catch (JSONException e1)
        {
            e1.printStackTrace();

            //show alert bahwa upload gagal
            genericAlert.DisplayAlert(message, title);
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Handle transaksi buat send berita broadcast
    //----------------------------------------------------------------------------------------------
    private void CreateSendBroadcastRequest()
    {
        new SendBroadcastRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //send broadcast request
    private class SendBroadcastRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateSendBroadcastRequestObject();

            //eksekusi request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleSendBroadcastResult(s);
        }
    }

    //create request object
    private JSONObject CreateSendBroadcastRequestObject()
    {
        //create request object
        JSONObject requestObject = new JSONObject();

        //get list of user yang diselect
        ArrayList<String> selectedUserID = staffBroadcastAdapter.GetSelectedUserID();

        //cek apakah semua user diselect
        boolean allUserSelected = staffBroadcastAdapter.IsAllUserSelected();

        try
        {
            //populate sp parameter request object
            JSONObject spParameterObject = new JSONObject();
            /*spParameterObject.put("AuthorID", userID);*/
            spParameterObject.put("AuthorID", "BTN0013887");
            spParameterObject.put("Title", formBroadcastBerita.Judul);
            spParameterObject.put("Summary", formBroadcastBerita.Isi);
            spParameterObject.put("NewsContent", formBroadcastBerita.Isi);
            spParameterObject.put("StartDate", formBroadcastBerita.StartDate_Formatted);
            spParameterObject.put("EndDate", formBroadcastBerita.ExpiredDate_Formatted);
            if (formBroadcastBerita.UploadedFilepath != null)
                spParameterObject.put("Attachment", formBroadcastBerita.UploadedFilepath);
            else
                spParameterObject.put("Attachment", "");

            if (allUserSelected)
            {
                spParameterObject.put("IsGlobal", 1);
                spParameterObject.put("SendToUserID", "");
            }
            else
            {
                spParameterObject.put("IsGlobal", 0);

                //create list of user
                String selectedUsers = "";
                for (int i = 0; i < selectedUserID.size(); i++)
                {
                    selectedUsers += selectedUserID.get(i);
                    if (i < selectedUserID.size() - 1)
                        selectedUsers += ",";
                }

                spParameterObject.put("SendToUserID", selectedUsers);
            }

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_NEWS_CREATE");
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
    private void HandleSendBroadcastResult(String resultString)
    {
        //komponen alert message
        String alertTitle, alertMessage;

        //pastikan response tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show alert bahwa transaksi gagal
            alertTitle = getString(R.string.Text_MohonMaaf);
            alertMessage = getString(R.string.Text_SomethingWrong);
            genericAlert.DisplayAlert(alertMessage, alertTitle);
            return;
        }

        try
        {
            //konvert jadi json object
            JSONArray resultArray = new JSONArray(resultString);

            if (resultArray.length() <= 0)
            {
                //show error
                alertTitle = getString(R.string.Text_MohonMaaf);
                alertMessage = getString(R.string.Text_SomethingWrong);
                genericAlert.DisplayAlert(alertMessage, alertTitle);
                return;
            }

            //get response object
            JSONObject resultObject = resultArray.getJSONObject(0);

            //get response code
            int responseCode = resultObject.getInt("ResponseCode");

            //cek apakah response codenya 200
            if (responseCode != 200)
            {
                //show error
                alertTitle = getString(R.string.Text_MohonMaaf);
                alertMessage = getString(R.string.Text_SomethingWrong);
                genericAlert.DisplayAlert(alertMessage, alertTitle);
                return;
            }

            //show alert bahwa send broadcast sukses
            alertTitle = getString(R.string.Text_Success);
            alertMessage = getString(R.string.Berita_Broadcast_Alert_BroadcastSuccess);
            genericAlert.DisplayAlert(alertMessage, alertTitle);

            //dismiss sendpopup
            if (sendPopup.isShowing())
                sendPopup.dismiss();
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            //show error
            alertTitle = getString(R.string.Text_MohonMaaf);
            alertMessage = getString(R.string.Text_SomethingWrong);
            genericAlert.DisplayAlert(alertMessage, alertTitle);
        }
    }
}
