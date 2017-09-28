package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterListDebiturBinding;

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
    private String tagihan;

    @SerializedName("DPD")
    @Expose
    private String dpd;

    @SerializedName("LastPaymentDate")
    @Expose
    private String lastPayment;

    @SerializedName("UseAssignDate")
    @Expose
    private String useAssignDate;

    @SerializedName("CustomerReference")
    @Expose
    private String customerReference;

    @SerializedName("RESULT_DATE")
    @Expose
    private String resultDate;

    @SerializedName("LatestIDVisit")
    @Expose
    private String latestIdVisit;

    @SerializedName("Note")
    @Expose
    private String note;

    @SerializedName("NoteFrom")
    @Expose
    private String noteFrom;

    @SerializedName("Aktifitas")
    @Expose
    private String aktifitas;

    @SerializedName("AktifitasID")
    @Expose
    private String aktifitasId;

    private boolean isPenugasan = false;


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

    public String getTagihan() {
        return tagihan;
    }

    public void setTagihan(String tagihan) {
        this.tagihan = tagihan;
    }

    public String getDpd() {
        return dpd;
    }

    public void setDpd(String  dpd) {
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

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public String getLatestIdVisit() {
        return latestIdVisit;
    }

    public void setLatestIdVisit(String latestIdVisit) {
        this.latestIdVisit = latestIdVisit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteFrom() {
        return noteFrom;
    }

    public void setNoteFrom(String noteFrom) {
        this.noteFrom = noteFrom;
    }

    public String getAktifitas() {
        return aktifitas;
    }

    public void setAktifitas(String aktifitas) {
        this.aktifitas = aktifitas;
    }

    public String getAktifitasId() {
        return aktifitasId;
    }

    public void setAktifitasId(String aktifitasId) {
        this.aktifitasId = aktifitasId;
    }

    public boolean isPenugasan() {
        return isPenugasan;
    }

    public void setPenugasan(boolean penugasan) {
        isPenugasan = penugasan;
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