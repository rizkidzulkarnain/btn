package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterListPhoneBinding;

import java.util.List;

/**
 * Created by ericwijaya on 8/25/17.
 */

public class PhoneNumber extends AbstractItem<PhoneNumber, PhoneNumber.ViewHolder> {

    @SerializedName("NomorRekening")
    @Expose
    private String nomorRekening;

    @SerializedName("NamaKontak")
    @Expose
    private String namaKontak;

    @SerializedName("Hubungan")
    @Expose
    private String hubungan;

    @SerializedName("JenisKontak")
    @Expose
    private String jenisKontak;

    @SerializedName("NomorKontak")
    @Expose
    private String nomorKontak;


    public PhoneNumber() {
    }

    public String getNomorRekening() {
        return nomorRekening;
    }

    public void setNomorRekening(String nomorRekening) {
        this.nomorRekening = nomorRekening;
    }

    public String getNamaKontak() {
        return namaKontak;
    }

    public void setNamaKontak(String namaKontak) {
        this.namaKontak = namaKontak;
    }

    public String getHubungan() {
        return hubungan;
    }

    public void setHubungan(String hubungan) {
        this.hubungan = hubungan;
    }

    public String getJenisKontak() {
        return jenisKontak;
    }

    public void setJenisKontak(String jenisKontak) {
        this.jenisKontak = jenisKontak;
    }

    public String getNomorKontak() {
        return nomorKontak;
    }

    public void setNomorKontak(String nomorKontak) {
        this.nomorKontak = nomorKontak;
    }


    @Override
    public int getType() {
        return this.getClass().hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_list_phone;
    }

    @Override
    public void bindView(PhoneNumber.ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.binding.setPhoneNumber(this);
        holder.binding.executePendingBindings();
    }

    @Override
    public void unbindView(PhoneNumber.ViewHolder holder) {
        super.unbindView(holder);
    }

    @Override
    public PhoneNumber.ViewHolder getViewHolder(View v) {
        return new PhoneNumber.ViewHolder(v);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterListPhoneBinding binding;

        public ViewHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);
        }
    }
}
