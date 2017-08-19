package com.mitkoindo.smartcollection.module.debitur.detaildebitur;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.mitkoindo.smartcollection.HistoriTindakanActivity;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityDetailDebiturBinding;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.module.visitform.FormVisitActivity;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;

import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DetailDebiturActivity extends BaseActivity {

    private DetailDebiturViewModel mDetailDebiturViewModel;
    private ActivityDetailDebiturBinding mBinding;
    private PopupMenu mPopUpMenu;

    public static Intent instantiate(Context context) {
        Intent intent = new Intent(context, DetailDebiturActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.DetailDebitur_PageTitle));
        initView();
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

    private void initView() {
        DetailDebitur detailDebitur = new DetailDebitur();
        detailDebitur.setNamaDebitur("Indra Susilo Setiawan");
        detailDebitur.setNoRekening("182319283");
        detailDebitur.setNoCif("12837918273");
        detailDebitur.setTotalTunggakan("5.000.000");
        detailDebitur.setLastPaymentDate("30/04/2017");
        detailDebitur.setDpd("10");
        detailDebitur.setAngsuranPerBulan("1.000.000");
        detailDebitur.setTotalKewajiban("");
        detailDebitur.setKolektabilitas("");
        detailDebitur.setTindakLanjut("surat");
        detailDebitur.setStatus("Ditempati");
        detailDebitur.setPtp("19/8/2017");
        detailDebitur.setBesaranPtp("4.000.000");
        detailDebitur.setAlamatRumah("Jl. Riau no 33 RT 005 / RW 012 Jurangmangu Timur, Pondok Aren, Tangeramg Selatan");
        detailDebitur.setAlamatAgunan("Jl. Riau no 33 RT 005 / RW 012 Jurangmangu Timur, Pondok Aren, Tangeramg Selatan");
        detailDebitur.setAlamatKantor("Jl. Sudirman kav 1");
        detailDebitur.setAlamatSaatIni("Jl. Ceger no 198");

        mBinding.setDetailDebitur(detailDebitur);
    }

    @Optional
    @OnClick(R.id.image_btn_toolbar_more)
    public void onShortcutMenuClick(View view) {
        showShortcutMenu(view);
    }

    private void showShortcutMenu(View anchorView) {
        mPopUpMenu = new PopupMenu(this, anchorView);
        mPopUpMenu.getMenuInflater().inflate(R.menu.popup_menu, mPopUpMenu.getMenu());
        mPopUpMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_menu_call: {
                        startActivity(ListDebiturActivity.instantiate(anchorView.getContext()));
                        break;
                    }
                    case R.id.popup_menu_isi_form_visit: {
                        startActivity(FormVisitActivity.instantiate(anchorView.getContext()));
                        break;
                    }
                    case R.id.popup_menu_lihat_history: {
                        /*startActivity(ListDebiturActivity.instantiate(anchorView.getContext()));*/
                        OpenMenu_HistoriTindakan();
                        break;
                    }
                }
                return true;
            }
        });

        mPopUpMenu.show();
    }

    //----------------------------------------------------------------------------------------------
    //  Open menu
    //----------------------------------------------------------------------------------------------
    //open histori tindakan
    private void OpenMenu_HistoriTindakan()
    {
        Intent intent = new Intent(this, HistoriTindakanActivity.class);
        startActivity(intent);
    }
}
