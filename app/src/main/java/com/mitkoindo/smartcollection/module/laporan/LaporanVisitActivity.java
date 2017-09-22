package com.mitkoindo.smartcollection.module.laporan;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.misc.ImageViewerActivity;
import com.mitkoindo.smartcollection.objectdata.ImageData;
import com.mitkoindo.smartcollection.objectdata.LaporanVisit;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LaporanVisitActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //field
    private TextView view_TujuanKunjungan;
    private TextView view_Alamat;
    private TextView view_YangDitemui;
    private TextView view_Hubungan;
    private TextView view_HasilKunjungan;
    private TextView view_TanggalJanji;
    private TextView view_JumlahSetor;
    private TextView view_StatusAgunan;
    private TextView view_KondisiAgunan;
    private TextView view_AlasanMenunggak;
    private TextView view_TindakLanjut;
    private TextView view_TanggalTindakLanjut;
    private TextView view_Catatan;
    private ImageView view_FotoDebitur;
    private ImageView view_FotoAgunan1;
    private ImageView view_FotoAgunan2;
    private ImageView view_Signature;

    //holder
    private View holder_TanggalJanji;
    private View holder_JumlahSetor;
    private View holder_FotoAgunan2;

    //generic alert
    private GenericAlert genericAlert;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url yang digunakan
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //id visit
    private String visitID;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //data form visit
    private LaporanVisit laporanVisit;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_visit);

        //Setup
        GetViews();
        SetupTransaction();
        GetBundles();

        //create request buat get laporan
        CreateGetLaporanRequest();
    }

    private void GetViews()
    {
        view_TujuanKunjungan = findViewById(R.id.LaporanVisit_TujuanKunjungan);
        view_Alamat = findViewById(R.id.LaporanVisit_Alamat);
        view_YangDitemui = findViewById(R.id.LaporanVisit_YangDitemui);
        view_Hubungan = findViewById(R.id.LaporanVisit_Hubungan);
        view_HasilKunjungan = findViewById(R.id.LaporanVisit_HasilKunjungan);
        view_TanggalJanji = findViewById(R.id.LaporanVisit_TanggalJanji);
        view_JumlahSetor = findViewById(R.id.LaporanVisit_JumlahSetor);
        view_StatusAgunan = findViewById(R.id.LaporanVisit_StatusAgunan);
        view_KondisiAgunan = findViewById(R.id.LaporanVisit_KondisiAgunan);
        view_AlasanMenunggak = findViewById(R.id.LaporanVisit_AlasanMenunggak);
        view_TindakLanjut = findViewById(R.id.LaporanVisit_TindakLanjut);
        view_TanggalTindakLanjut = findViewById(R.id.LaporanVisit_TanggalTindakLanjut);
        view_Catatan = findViewById(R.id.LaporanVisit_Catatan);
        view_FotoDebitur = findViewById(R.id.LaporanVisit_Foto_Debitur);
        view_FotoAgunan1 = findViewById(R.id.LaporanVisit_Foto_Agunan1);
        view_FotoAgunan2 = findViewById(R.id.LaporanVisit_Foto_Agunan2);
        view_Signature = findViewById(R.id.LaporanVisit_Foto_Signature);

        holder_TanggalJanji = findViewById(R.id.LaporanVisit_Holder_TanggalJanji);
        holder_JumlahSetor = findViewById(R.id.LaporanVisit_Holder_JumlahSetor);
        holder_FotoAgunan2 = findViewById(R.id.LaporanVisit_Holder_FotoAgunan2);

        View btnBack = findViewById(R.id.image_btn_toolbar_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(view -> onBackPressed());
        }

        //create generic alert
        genericAlert = new GenericAlert(this);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(this);

        //get user ID
        userID = ResourceLoader.LoadUserID(this);
    }

    //get bundle
    private void GetBundles()
    {
        //get referece ke bundle
        Bundle bundle = getIntent().getExtras();

        //pastikan bundle tidak null
        if (bundle == null)
            return;

        //get data dari bundle
        visitID = bundle.getString("VisitID");
    }

    //----------------------------------------------------------------------------------------------
    //  Create request
    //----------------------------------------------------------------------------------------------
    //create request buat get data laporan
    private void CreateGetLaporanRequest()
    {
        //show loading alert
        genericAlert.ShowLoadingAlert();

        //send request
        new SendGetLaporanRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CreateGetLaporanRequestObject());
    }

    //create request object buat get data laporan
    private JSONObject CreateGetLaporanRequestObject()
    {
        //inisialisasi request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("idVisit", visitID);

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_VISIT_VIEW");
            requestObject.put("SpParameter", spParameterObject);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    private class SendGetLaporanRequest extends AsyncTask<JSONObject, Void, String>
    {
        @Override
        protected String doInBackground(JSONObject... jsonObjects)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(jsonObjects[0]);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetLaporanResult(s);
        }
    }

    //handle result transaksi
    private void HandleGetLaporanResult(String resultString)
    {
        //dismiss alert
        genericAlert.Dismiss();

        //pastikan result tidak null
        if (resultString == null || resultString.isEmpty())
        {
            String title = getString(R.string.Text_MohonMaaf);
            String message = getString(R.string.Text_SomethingWrong);
            genericAlert.DisplayAlert(message, title);
            return;
        }

        //pastikan result tidak 404
        if (resultString.equals("Not Found"))
        {
            String title = getString(R.string.Text_MohonMaaf);
            String message = getString(R.string.Text_NoData);
            genericAlert.DisplayAlert(message, title);
            return;
        }

        try
        {
            //extract data
            JSONArray dataArray = new JSONArray(resultString);

            //pastikan data pertama tidak kosong
            if (dataArray.length() <= 0)
            {
                String title = getString(R.string.Text_MohonMaaf);
                String message = getString(R.string.Text_NoData);
                genericAlert.DisplayAlert(message, title);
                return;
            }

            //extract data pertama
            laporanVisit = new LaporanVisit();
            laporanVisit.ParseData(dataArray.getJSONObject(0));
            AttachDataToViews();
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            String title = getString(R.string.Text_MohonMaaf);
            String message = getString(R.string.Text_SomethingWrong);
            genericAlert.DisplayAlert(message, title);
        }
    }

    //attach data to views
    private void AttachDataToViews()
    {
        view_TujuanKunjungan.setText(laporanVisit.TujuanVisitDesc);
        view_Alamat.setText(laporanVisit.AlamatKunjunganDesc);
        view_YangDitemui.setText(laporanVisit.OrangYangDikunjungi);
        view_Hubungan.setText(laporanVisit.HubunganYangDikunjungiDesc);
        view_HasilKunjungan.setText(laporanVisit.HasilVisitDesc);

        if (laporanVisit.TanggalJanjiBayar == null || laporanVisit.TanggalJanjiBayar.isEmpty())
        {
            holder_TanggalJanji.setVisibility(View.GONE);
        }
        else
        {
            view_TanggalJanji.setText(laporanVisit.TanggalJanjiBayar);
            holder_TanggalJanji.setVisibility(View.VISIBLE);
        }

        if (laporanVisit.NominalJanjiBayar.equals("0.0"))
        {
            holder_JumlahSetor.setVisibility(View.GONE);
        }
        else
        {
            view_JumlahSetor.setText(laporanVisit.NominalJanjiBayar);
            holder_JumlahSetor.setVisibility(View.VISIBLE);
        }

        view_StatusAgunan.setText(laporanVisit.StatusAgunanDesc);
        view_KondisiAgunan.setText(laporanVisit.KondisiAgunan);
        view_AlasanMenunggak.setText(laporanVisit.AlasanTidakBayarDesc);
        view_TindakLanjut.setText(laporanVisit.TindakLanjutDesc);
        view_TanggalTindakLanjut.setText(laporanVisit.TanggalTindakLanjut);
        view_Catatan.setText(laporanVisit.Catatan);

        //load images
        if (laporanVisit.PhotoDebitur != null && !laporanVisit.PhotoDebitur.isEmpty())
        {
            ImageData imageData = new ImageData();
            imageData.LoadImageToImageView(this, baseURL, laporanVisit.PhotoDebitur, view_FotoDebitur);
        }
        else
        {

        }

        if (laporanVisit.PhotoAgunan1 != null && !laporanVisit.PhotoAgunan1.isEmpty())
        {
            ImageData imageData = new ImageData();
            imageData.LoadImageToImageView(this, baseURL, laporanVisit.PhotoAgunan1, view_FotoAgunan1);
        }
        else
        {

        }

        if (laporanVisit.PhotoAgunan2 != null && !laporanVisit.PhotoAgunan2.isEmpty() && !laporanVisit.PhotoAgunan2.equals("null"))
        {
            ImageData imageData = new ImageData();
            imageData.LoadImageToImageView(this, baseURL, laporanVisit.PhotoAgunan2, view_FotoAgunan2);
        }
        else
        {
            holder_FotoAgunan2.setVisibility(View.GONE);
        }

        if (laporanVisit.PhotoSignature != null && !laporanVisit.PhotoSignature.isEmpty())
        {
            ImageData imageData = new ImageData();
            imageData.LoadImageToImageView(this, baseURL, laporanVisit.PhotoSignature, view_Signature);
        }
        else
        {

        }

        /*view_FotoDebitur;
        view_FotoAgunan1;
        view_FotoAgunan2;
        view_Signature;*/
    }

    //----------------------------------------------------------------------------------------------
    //  Handle input
    //----------------------------------------------------------------------------------------------
    public void HandleInput_LaporanVisit_OpenFotoDebitur(View view)
    {
        //set url
        if (laporanVisit.PhotoDebitur != null && !laporanVisit.PhotoDebitur.isEmpty() && !laporanVisit.PhotoDebitur.equals("null"))
        {
            String usedURL = baseURL + laporanVisit.PhotoDebitur.substring(1);
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("ImageUrl", usedURL);
            startActivity(intent);
        }
    }

    public void HandleInput_LaporanVisit_OpenFotoAgunan1(View view)
    {
        //set url
        if (laporanVisit.PhotoAgunan1 != null && !laporanVisit.PhotoAgunan1.isEmpty() && !laporanVisit.PhotoAgunan1.equals("null"))
        {
            String usedURL = baseURL + laporanVisit.PhotoAgunan1.substring(1);
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("ImageUrl", usedURL);
            startActivity(intent);
        }
    }

    public void HandleInput_LaporanVisit_OpenFotoAgunan2(View view)
    {
        //set url
        if (laporanVisit.PhotoAgunan2 != null && !laporanVisit.PhotoAgunan2.isEmpty() && !laporanVisit.PhotoAgunan2.equals("null"))
        {
            String usedURL = baseURL + laporanVisit.PhotoAgunan2.substring(1);
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("ImageUrl", usedURL);
            startActivity(intent);
        }
    }
}
