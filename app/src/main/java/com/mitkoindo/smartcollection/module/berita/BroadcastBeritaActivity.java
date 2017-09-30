package com.mitkoindo.smartcollection.module.berita;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.response.MultipartResponse;
import com.mitkoindo.smartcollection.objectdata.FormBroadcastBerita;
import com.mitkoindo.smartcollection.objectdata.Staff;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.HttpsTrustManager;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;
import com.mitkoindo.smartcollection.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;


import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.observers.DisposableLambdaObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BroadcastBeritaActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //form
    private TextView view_Title;
    private TextView view_Content;
    private TextView view_Summary;

    //start date & expired date text
    private TextView view_StartDate;
    private TextView view_ExpiredDate;

    //filepath
    private TextView view_Filepath;

    //generic alert
    private GenericAlert genericAlert;

    //popup buat milih petugas yang akan dikirimi berita
    private AlertDialog sendPopup;

    //dialog buat milih file type
    private AlertDialog popup_FileTypeSelector;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //filepath
    private ArrayList<String> data_Filepaths;

    //data form broadcast
    private FormBroadcastBerita formBroadcastBerita;

    //data staff list
    private ArrayList<Staff> staffs;

    //data staff + groupnya
    private ArrayList<Staff> staffAndGroups;

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

    //request code buat minta permission storage
    private final int REQUEST_PERMISSION_STORAGE = 736;

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
        view_Summary = findViewById(R.id.BroadcastBerita_Summary);

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
        formBroadcastBerita.Summary = view_Summary.getText().toString();

        //pastikan judul, isi, dan start / end date sudah diisi
        if (formBroadcastBerita.Judul.isEmpty() ||
                formBroadcastBerita.Summary.isEmpty() ||
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
        //cek permission telebih dahulu
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (IsPermissionAllowed_Storage())
            {
                CreateFileTypeSelectorPopup();
            }
            else
            {
                //request permission
                RequestPermission_Storage();
            }

            return;
        }

        CreateFileTypeSelectorPopup();
    }

    //cek permission buat akses storage
    private boolean IsPermissionAllowed_Storage()
    {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //request permission untuk access storage
    private void RequestPermission_Storage()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            //show alert bahwa permission storage dibutuhkan untuk upload file
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);

            //set message & title
            builder.setTitle(R.string.PermissionRequest_Alert);
            builder.setMessage(R.string.PermissionRequest_Storage_Denied);

            //set listener
            builder.setPositiveButton(R.string.PermissionRequest_Give, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    //open settings menu
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.PermissionRequest_Cancel, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {

                }
            });

            //show alert
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    //create popup untuk pilih filetype
    private void CreateFileTypeSelectorPopup()
    {
        //create popup untuk pilih jenis file
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //inflate layout
        LayoutInflater layoutInflater = getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.popup_broadcastberita_selectfiletype, null, false);
        builder.setView(thisView);

        //add listener pada view
        View button_AttachDoc = thisView.findViewById(R.id.BeritaBroadcast_SelectFilePopup_AttachDocument);
        View button_AttachImg = thisView.findViewById(R.id.BeritaBroadcast_SelectFilePopup_AttachPictures);
        button_AttachDoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CreatePickPDFIntent();
            }
        });
        button_AttachImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CreatePickImgFile();
            }
        });

        //show alert
        popup_FileTypeSelector = builder.create();
        popup_FileTypeSelector.show();
    }

    //create intent buat pick pdf file
    private void CreatePickPDFIntent()
    {
        //batasi filetype ke pdf
        String[] filetypes = {".pdf"};

        //create filepicker
        FilePickerBuilder.getInstance().setMaxCount(1)
                .enableDocSupport(false)
                .enableImagePicker(false)
                .enableVideoPicker(false)
                .enableCameraSupport(false)
                .addFileSupport("PDF", filetypes)
                .setActivityTheme(R.style.AppTheme)
                .pickFile(this);

        //dismiss
        popup_FileTypeSelector.dismiss();
    }

    //create intent buat pick img file
    private void CreatePickImgFile()
    {
        //create filepicker
        FilePickerBuilder.getInstance().setMaxCount(1)
                .enableImagePicker(true)
                .enableVideoPicker(false)
                .enableCameraSupport(false)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(this);

        //dismiss
        popup_FileTypeSelector.dismiss();
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

    //handle back button
    public void HandleInput_BroadcastBerita_Back(View view)
    {
        finish();
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
        staffBroadcastAdapter = new StaffBroadcastAdapter(this, staffAndGroups);

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
                        formBroadcastBerita.Filepath = data_Filepaths.get(0);
                        view_Filepath.setText(formBroadcastBerita.Filepath);
                    }
                }
                break;
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    data_Filepaths = new ArrayList<>();
                    data_Filepaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

                    //set filepath
                    if (data_Filepaths.size() > 0)
                    {
                        formBroadcastBerita.Filepath = data_Filepaths.get(0);
                        /*
                        int textLength = formBroadcastBerita.Filepath.length();
                        String displayedString;
                        if (textLength > 30)
                            displayedString = "..." + formBroadcastBerita.Filepath.substring(30);
                        else
                            displayedString = formBroadcastBerita.Filepath;*/
                        view_Filepath.setText(formBroadcastBerita.Filepath);
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
            spParameterObject.put("userID", userID);
            spParameterObject.put("keyword", null);
            spParameterObject.put("top", 9999);
            /*spParameterObject.put("userID", "BTN0013887");*/

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

            //get all group
            ArrayList<String> groupList = new ArrayList<>();
            for (int i = 0; i < staffs.size(); i++)
            {
                String currentGroup = staffs.get(i).GROUP_NAME;

                //cek apakah groupnya sudah ada di grouplist atau belum
                boolean groupExist = false;
                for (int j = 0; j < groupList.size(); j++)
                {
                    if (currentGroup.equals(groupList.get(j)))
                    {
                        groupExist = true;
                        break;
                    }
                }

                if (!groupExist)
                {
                    groupList.add(currentGroup);
                }
            }

            //save staff & group list
            staffAndGroups = new ArrayList<>();
            for (int i = 0; i < groupList.size(); i++)
            {
                //get current group
                String currentGroup = groupList.get(i);

                //create new staff data
                Staff groupHolder = new Staff();
                groupHolder.FLAG_CHECKED = true;
                groupHolder.GROUP_NAME = groupList.get(i);
                staffAndGroups.add(groupHolder);

                //add all staff yang memiliki group yang sama
                for (int j = 0; j < staffs.size(); j++)
                {
                    if (currentGroup.equals(staffs.get(j).GROUP_NAME))
                    {
                        staffAndGroups.add(staffs.get(j));
                    }
                }
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
        genericAlert.ShowLoadingAlert();
        AlternativeSendFile();
        /*new ExecuteSendFileRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");*/
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
            Ion.with(BroadcastBeritaActivity.this)
                    .load(usedURL)
                    .setHeader("Content-Type", "multipart/form-data")
                    .setMultipartFile("file", file)
                    .asString()
                    .setCallback(new FutureCallback<String>()
                    {
                        @Override
                        public void onCompleted(Exception e, String result)
                        {
                            HandleUploadFileResult(e, result);
                        }
                    });
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

            /*Ion.with(BroadcastBeritaActivity.this)
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
        genericAlert.ShowLoadingAlert();

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
            spParameterObject.put("AuthorID", userID);
            /*spParameterObject.put("AuthorID", "BTN0013887");*/
            spParameterObject.put("Title", formBroadcastBerita.Judul);
            spParameterObject.put("Summary", formBroadcastBerita.Summary);
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
            /*alertTitle = getString(R.string.Text_Success);
            alertMessage = getString(R.string.Berita_Broadcast_Alert_BroadcastSuccess);
            genericAlert.DisplayAlert(alertMessage, alertTitle);*/

            //dismiss sendpopup
            if (sendPopup.isShowing())
                sendPopup.dismiss();

            ShowSendSuccessAlert();
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

    //----------------------------------------------------------------------------------------------
    //  Return to list berita setelah berhasil kirim berita
    //----------------------------------------------------------------------------------------------
    private void ShowSendSuccessAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Text_Success);
        builder.setMessage(R.string.Berita_Broadcast_Alert_BroadcastSuccess);

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

    //----------------------------------------------------------------------------------------------
    //  Cek hasil permission
    //----------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case REQUEST_PERMISSION_STORAGE :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //allow access ke file picker
                    CreateFileTypeSelectorPopup();
                }
                break;
            default:break;
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Pake method Eric untuk upload file
    //----------------------------------------------------------------------------------------------
    private CompositeDisposable composites = new CompositeDisposable();

    private void AlternativeSendFile()
    {
        File fileAttachment = new File(formBroadcastBerita.Filepath);
        Uri uriAttachment = Uri.fromFile(fileAttachment);
        RequestBody requestFileAttachment = RequestBody.create(MediaType.parse(FileUtils.getMimeType(uriAttachment)), fileAttachment);
        MultipartBody.Part bodyAttachment = MultipartBody.Part.createFormData("file", fileAttachment.getName(), requestFileAttachment);
        Disposable disposable = ApiUtils.getMultipartServices(authToken).uploadFile(bodyAttachment)
                .flatMap(new Function<MultipartResponse, ObservableSource<MultipartResponse>>() {
                    @Override
                    public ObservableSource<MultipartResponse> apply(@NonNull MultipartResponse multipartResponse) throws Exception
                    {
                        /*spParameter.setPhotoDebitur(multipartResponse.getRelativePath());*/
                        return ApiUtils.getMultipartServices(authToken).uploadFile(bodyAttachment);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribeWith(new DisposableObserver<MultipartResponse>()
                {

                    @Override
                    public void onNext(@NonNull MultipartResponse multipartResponse)
                    {
                        //simpan relativepath
                        formBroadcastBerita.UploadedFilepath = multipartResponse.getRelativePath();
                        CreateSendBroadcastRequest();
                    }

                    @Override
                    public void onError(@NonNull Throwable e)
                    {
                        //text buat alert kalo gagal upload file
                        String title = getString(R.string.Text_MohonMaaf);
                        String message = getString(R.string.Text_UploadFailed);

                        //show alert bahwa upload gagal
                        genericAlert.DisplayAlert(message, title);
                    }

                    @Override
                    public void onComplete()
                    {
                        int x = 0;
                        int z = x + 1;
                    }
                });
        composites.add(disposable);
    }
}
