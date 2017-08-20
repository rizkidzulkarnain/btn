package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterListDebiturBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ericwijaya on 8/17/17.
 */

public class DebiturItem extends AbstractItem <DebiturItem, DebiturItem.ViewHolder> {
    @SerializedName("No")
    @Expose
    private String no;

    @SerializedName("NoCIF")
    @Expose
    private String noCif;

    @SerializedName("NamaNasabah")
    @Expose
    private String nama;

    @SerializedName("NomorRekening")
    @Expose
    private String noRekening;

    @SerializedName("Tagihan")
    @Expose
    private int tagihan;

    @SerializedName("DPD")
    @Expose
    private int dpd;

    @SerializedName("LastPaymentDate")
    @Expose
    private String lastPayment;

    @SerializedName("UseAssignDate")
    @Expose
    private String useAssignDate;

    public DebiturItem() {
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

    public int getTagihan() {
        return tagihan;
    }

    public void setTagihan(int tagihan) {
        this.tagihan = tagihan;
    }

    public int getDpd() {
        return dpd;
    }

    public void setDpd(int dpd) {
        this.dpd = dpd;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNoCif() {
        return noCif;
    }

    public void setNoCif(String noCif) {
        this.noCif = noCif;
    }

    public String getUseAssignDate() {
        return useAssignDate;
    }

    public void setUseAssignDate(String useAssignDate) {
        this.useAssignDate = useAssignDate;
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



    //parse form JSON
    public void ParseData(String jsonString)
    {
        try
        {
            //converst string to json object
            JSONObject dataObject = new JSONObject(jsonString);

            //get data
            nama = dataObject.getString("NamaNasabah");
            noRekening = dataObject.getString("NomorRekening");
            tagihan = dataObject.optInt("Tagihan", 0);
            dpd = dataObject.optInt("DPD", 0);
            lastPayment = dataObject.getString("LastPaymentDate");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}