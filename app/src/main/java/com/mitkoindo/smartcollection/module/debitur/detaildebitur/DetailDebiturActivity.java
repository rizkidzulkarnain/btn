package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mitkoindo.smartcollection.HistoriTindakanActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityDetailDebiturBinding;
import com.mitkoindo.smartcollection.dialog.DialogSimpleSpinnerAdapter;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.formcall.FormCallActivity;
import com.mitkoindo.smartcollection.module.formvisit.FormVisitActivity;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DetailDebiturActivity extends BaseActivity {
    public static final String EXTRA_NO_REKENING = "extra_no_rekening";

    private DetailDebiturViewModel mDetailDebiturViewModel;
    private ActivityDetailDebiturBinding mBinding;
    private PopupMenu mPopUpMenu;
    private Dialog mListPhoneNumberDialog;
    private ArrayList<String> mListNomorTelepon = new ArrayList<>();
    private String mNoRekening = "";

    public static Intent instantiate(Context context, String noRekening) {
        Intent intent = new Intent(context, DetailDebiturActivity.class);
        intent.putExtra(EXTRA_NO_REKENING, noRekening);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.DetailDebitur_PageTitle));
        getExtra();
        initView();

        SetupTransaction();
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_detail_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        mDetailDebiturViewModel = new DetailDebiturViewModel();
        addViewModel(mDetailDebiturViewModel);
        mBinding = DataBindingUtil.bind(contentView);
        mBinding.setDetailDebiturViewModel(mDetailDebiturViewModel);
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            mNoRekening = getIntent().getExtras().getString(EXTRA_NO_REKENING);
        }
    }

    private void initView() {
//        DetailDebitur detailDebitur = new DetailDebitur();
//        detailDebitur.setNamaDebitur("Indra Susilo Setiawan");
//        detailDebitur.setNoRekening("182319283");
//        detailDebitur.setNoCif("12837918273");
//        detailDebitur.setTotalTunggakan(5000000);
//        detailDebitur.setLastPaymentDate("30/04/2017");
//        detailDebitur.setDpd(10);
//        detailDebitur.setAngsuranPerBulan(1000000);
//        detailDebitur.setTotalKewajiban(0);
//        detailDebitur.setKolektabilitas("");
//        detailDebitur.setTindakLanjut("surat");
//        detailDebitur.setStatus("Ditempati");
//        detailDebitur.setPtp("19/8/2017");
//        detailDebitur.setBesaranPtp("4.000.000");
//        detailDebitur.setAlamatRumah("Jl. Riau no 33 RT 005 / RW 012 Jurangmangu Timur, Pondok Aren, Tangerang Selatan");
//        detailDebitur.setAlamatAgunan("Jl. Riau no 33 RT 005 / RW 012 Jurangmangu Timur, Pondok Aren, Tangerang Selatan");
//        detailDebitur.setAlamatKantor("Jl. Sudirman kav 1");
//        detailDebitur.setAlamatSaatIni("Jl. Ceger no 198");
//
//        mBinding.setDetailDebitur(detailDebitur);

        mListNomorTelepon.add("081325765051");
        mListNomorTelepon.add("081325765052");
        mListNomorTelepon.add("081325765555");
    }

    @Optional
    @OnClick(R.id.image_btn_toolbar_more)
    public void onShortcutMenuClick(View view) {
        showShortcutMenu(view);
    }

    private static final int LIST_PHONE = 123;
    private void showShortcutMenu(View anchorView) {
        mPopUpMenu = new PopupMenu(this, anchorView);
        mPopUpMenu.getMenuInflater().inflate(R.menu.popup_menu, mPopUpMenu.getMenu());
        mPopUpMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_menu_call: {
                        showInstallmentDialogSimpleSpinner(mListNomorTelepon, getString(R.string.DetailDebitur_PilihNomorTelepon), LIST_PHONE);
                        break;
                    }
                    case R.id.popup_menu_check_in: {

                        break;
                    }
                    case R.id.popup_menu_isi_form_visit: {
                        startActivity(FormVisitActivity.instantiate(DetailDebiturActivity.this));
                        break;
                    }
                    case R.id.popup_menu_lihat_history: {
                        Intent intent = new Intent(DetailDebiturActivity.this, HistoriTindakanActivity.class);
                        intent.putExtra(EXTRA_NO_REKENING, mNoRekening);
                        startActivity(intent);
                        /*startActivity(new Intent(DetailDebiturActivity.this, HistoriTindakanActivity.class));*/
                        break;
                    }
                }
                return true;
            }
        });

        mPopUpMenu.show();
    }

    private void showInstallmentDialogSimpleSpinner(List<String> nameList, String dialogTitle, int viewId) {
        if (mListPhoneNumberDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder
                    .setTitle(null)
                    .setMessage(null)
                    .setCancelable(true)
                    .setView(R.layout.dialog_simple_spinner);
            mListPhoneNumberDialog = dialogBuilder.create();
        }
        if (nameList.size() > 0) {
            mListPhoneNumberDialog.show();
            RecyclerView recyclerView = (RecyclerView) mListPhoneNumberDialog.findViewById(R.id.rv_dialog_simple_spinner);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            DialogSimpleSpinnerAdapter adapter = new DialogSimpleSpinnerAdapter(nameList, viewId);
            recyclerView.setAdapter(adapter);
            TextView title = (TextView) mListPhoneNumberDialog.findViewById(R.id.tv_dialog_simple_spinner_title);
            title.setText(dialogTitle);
        } else {
            ToastUtils.toastShort(this, getString(R.string.TidakAdaDataPilihan));
        }
    }

    @OnClick(R.id.fab_map)
    public void onFabMapClick(View view) {
        Uri gmmIntentUri = Uri.parse("geo:-6.1671626,106.8175127");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Subscribe
    public void onDialogSimpleSpinnerSelected(EventDialogSimpleSpinnerSelected event) {
        if (event.getViewId() ==  LIST_PHONE) {
            if (mListPhoneNumberDialog != null && mListPhoneNumberDialog.isShowing()) {
                mListPhoneNumberDialog.dismiss();
                String phoneNumber = event.getName();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));

                startActivities(new Intent[] {
                        FormCallActivity.instantiate(DetailDebiturActivity.this),
                        intent});
            }
        }
    }




    private String baseURL;
    private String url_Data_StoreProcedure;
    private String authToken;
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

        //show loading alert & create request
        showLoadingDialog();
        new DetailDebiturActivity.SendGetDetailDebiturRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    private class SendGetDetailDebiturRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create request object
            JSONObject requestObject = CreateGetDetailDebiturRequestObject();

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
            HandleGetDetailDebiturTindakanResult(s);
        }
    }

    private JSONObject CreateGetDetailDebiturRequestObject()
    {
        JSONObject requestObject = new JSONObject();

        try
        {
            //populate object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_DETAIL");

            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("nomorRekening", mNoRekening);

            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestObject;
    }

    private void HandleGetDetailDebiturTindakanResult(String jsonString)
    {
        //dismiss alert
        hideLoadingDialog();

        try
        {
            //test parse jsonString
            JSONArray dataArray = new JSONArray(jsonString);

            //extract data
            if (dataArray.length() > 0)
            {
                DetailDebitur detailDebitur = new DetailDebitur();
                detailDebitur.ParseData(dataArray.getString(0));
                mBinding.setDetailDebitur(detailDebitur);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}
