package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterListDebiturBinding;

import java.util.List;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DebiturItem extends AbstractItem <DebiturItem, DebiturItem.ViewHolder> {
    private String nama;
    private String noRekening;
    private String tagihan;
    private String dpd;
    private String lastPayment;

    public DebiturItem() {
    }

    public DebiturItem(String nama, String noRekening, String tagihan, String dpd, String lastPayment) {
        this.nama = nama;
        this.noRekening = noRekening;
        this.tagihan = tagihan;
        this.dpd = dpd;
        this.lastPayment = lastPayment;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public String getTagihan() {
        return tagihan;
    }

    public void setTagihan(String tagihan) {
        this.tagihan = tagihan;
    }

    public String getDpd() {
        return dpd;
    }

    public void setDpd(String dpd) {
        this.dpd = dpd;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    @Override
    public int getType() {
        return this.getClass().hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_list_debitur;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.binding.setDebiturItem(this);
        holder.binding.executePendingBindings();
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterListDebiturBinding binding;

        public ViewHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);
        }
    }
}