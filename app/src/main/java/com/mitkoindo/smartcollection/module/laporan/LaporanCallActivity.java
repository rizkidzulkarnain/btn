package com.mitkoindo.smartcollection.module.laporan;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.objectdata.LaporanCall;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class LaporanCallActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    private TextView view_Tujuan;
    private TextView view_BerbicaraDengan;
    private TextView view_Hubungan;
    private TextView view_Hasil;
    private TextView view_TanggalHasil;
    private TextView view_JumlahSetoran;
    private TextView view_AlasanTidakBayar;
    private TextView view_TindakLanjut;
    private TextView view_TanggalTindakLanjut;
    private TextView view_Catatan;

    private TextView title_TanggalHasilPanggilan;
    private View holder_TanggalHasilPanggilan;

    private View title_JumlahSetoran;
    private View holder_JumlahSetoran;

    private TextView label_TanggalJanji;

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

    //id visit
    private String callID;

    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //data form call
    private LaporanCall laporanCall;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_call);

        //setup
        GetViews();
        SetupTransaction();
        GetBundles();

        //send request buat get form call
        CreateGetFormCallRequest();
    }

    //get views
    private void GetViews()
    {
        view_Tujuan = findViewById(R.id.LaporanCall_Tujuan);
        view_BerbicaraDengan = findViewById(R.id.LaporanCall_BerbicaraDengan);
        view_Hubungan = findViewById(R.id.LaporanCall_Hubungan);
        view_Hasil = findViewById(R.id.LaporanCall_Hasil);
        view_TanggalHasil = findViewById(R.id.LaporanCall_TanggalHasil);
        view_JumlahSetoran = findViewById(R.id.LaporanCall_JumlahSetoran);
        view_AlasanTidakBayar = findViewById(R.id.LaporanCall_AlasanTidakBayar);
        view_TindakLanjut = findViewById(R.id.LaporanCall_TindakLanjut);
        view_TanggalTindakLanjut = findViewById(R.id.LaporanCall_TanggalTindakLanjut);
        view_Catatan = findViewById(R.id.LaporanCall_Catatan);

        title_TanggalHasilPanggilan = findViewById(R.id.text_view_tanggal_hasil_panggilan);
        holder_TanggalHasilPanggilan = findViewById(R.id.card_view_tanggal_hasil_panggilan);

        title_JumlahSetoran = findViewById(R.id.text_view_jumlah_yang_akan_disetor);
        holder_JumlahSetoran = findViewById(R.id.card_view_jumlah_yang_akan_disetor);


        genericAlert = new GenericAlert(this);

        View btnBack = findViewById(R.id.image_btn_toolbar_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(view -> onBackPressed());
        }

        TextView view_Title = findViewById(R.id.text_view_toolbar_title);
        view_Title.setText(R.string.Laporan_Title_FormCall);
    }

    //setup transaksi
    private void SetupTransaction()
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(this);
        url_DataSP = getString(R.string.URL_Data_StoreProcedure);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(this);
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
        callID = bundle.getString("CallID");
        Timber.i("___" + callID);
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get data form call
    //----------------------------------------------------------------------------------------------
    private void CreateGetFormCallRequest()
    {
        //show alert
        genericAlert.ShowLoadingAlert();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("idCall", callID);

            JSONObject requestObject = new JSONObject();
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_CALL_VIEW");
            requestObject.put("SpParameter", spParameterObject);

            //send request
            new SendGetFormCallRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //send request
    private class SendGetFormCallRequest extends AsyncTask<JSONObject, Void, String>
    {
        @Override
        protected String doInBackground(JSONObject... jsonObjects)
        {
            //get request object
            JSONObject requestObject = jsonObjects[0];

            //set url
            String usedURL = baseURL + url_DataSP;

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            HandleGetFormCallResult(s);
        }
    }

    //handle resultnya
    private void HandleGetFormCallResult(String resultString)
    {
        //pastikan response tidak null atau kosong
        if (resultString == null || resultString.isEmpty())
        {
            String alertmessage = getString(R.string.Text_SomethingWrong);
            genericAlert.DisplayAlert(alertmessage, "");
            return;
        }

        //pastikan result tidak 404
        if (resultString.equals("Not Found"))
        {
            String alertMessage = getString(R.string.Text_NoData);
            genericAlert.DisplayAlert(alertMessage, "");
            return;
        }

        try
        {
            //extract data
            JSONArray dataArray = new JSONArray(resultString);

            //pastikan length object tidak kosong
            if (dataArray.length() <= 0)
            {
                String alertMessage = getString(R.string.Text_NoData);
                genericAlert.DisplayAlert(alertMessage, "");
                return;
            }

            //create object
            laporanCall = new LaporanCall();

            //ekstrak data
            laporanCall.ParseData(dataArray.getJSONObject(0));
            AttachData();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //attach data ke views
    private void AttachData()
    {
        view_Tujuan.setText(laporanCall.TujuanVisitDesc);
        view_BerbicaraDengan.setText(laporanCall.OrangYangDikunjungi);
        view_Hubungan.setText(laporanCall.HubunganYangDikunjungiDesc);
        view_Hasil.setText(laporanCall.HasilVisitDesc);
        view_AlasanTidakBayar.setText(laporanCall.AlasanTidakBayarDesc);
        view_TindakLanjut.setText(laporanCall.TindakLanjutDesc);
        view_TanggalTindakLanjut.setText(laporanCall.TanggalTindakLanjut);
        view_Catatan.setText(laporanCall.Catatan);

        //cek apakah tanggal hasil dan jumlah setoran perlu dihide atau tidak
        if (laporanCall.HasilVisit.equals(RestConstants.RESULT_ID_AKAN_SETOR_TANGGAL_VALUE))
        {
            //maka show tanggal hasil dan jumlah setoran
            view_TanggalHasil.setText(laporanCall.TanggalJanjiBayar);
            view_JumlahSetoran.setText(laporanCall.NominalJanjiBayar);
        }
        else if (laporanCall.HasilVisit.equals(RestConstants.RESULT_ID_AKAN_DATANG_KE_BTN_TANGGAL_VALUE) ||
                laporanCall.HasilVisit.equals(RestConstants.RESULT_ID_MINTA_DIHUBUNGI_TANGGAL_VALUE))
        {
            title_TanggalHasilPanggilan.setText(R.string.FormVisit_TanggalRealisasiJanji);

            //hanya show tanggal hasil saja
            view_TanggalHasil.setText(laporanCall.TanggalJanjiBayar);

            //hide field setoran
            title_JumlahSetoran.setVisibility(View.GONE);
            holder_JumlahSetoran.setVisibility(View.GONE);
        }
        else
        {
            //hide everything
            title_TanggalHasilPanggilan.setVisibility(View.GONE);
            holder_TanggalHasilPanggilan.setVisibility(View.GONE);
            title_JumlahSetoran.setVisibility(View.GONE);
            holder_JumlahSetoran.setVisibility(View.GONE);
        }

        //dismiss alert
        genericAlert.Dismiss();
    }
}
