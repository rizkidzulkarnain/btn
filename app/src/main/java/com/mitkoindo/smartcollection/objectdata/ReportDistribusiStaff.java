package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterListReportDistribusiStaffBinding;

import java.util.List;

/**
 * Created by ericwijaya on 9/15/17.
 */

public class ReportDistribusiStaff extends AbstractItem<ReportDistribusiStaff, ReportDistribusiStaff.ViewHolder> {

    @SerializedName("UserID")
    @Expose
    private String userID;

    @SerializedName("FullName")
    @Expose
    private String fullName;

    @SerializedName("GroupID")
    @Expose
    private String groupID;

    @SerializedName("JumlahPenugasan")
    @Expose
    private String jumlahPenugasan;

    @SerializedName("JumlahOutstanding")
    @Expose
    private String jumlahOutstanding;

    @SerializedName("JumlahSudahKunjungan")
    @Expose
    private String jumlahSudahKunjungan;

    @SerializedName("JumlahBelumKunjungan")
    @Expose
    private String jumlahBelumKunjungan;


    public ReportDistribusiStaff() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getJumlahPenugasan() {
        return jumlahPenugasan;
    }

    public void setJumlahPenugasan(String jumlahPenugasan) {
        this.jumlahPenugasan = jumlahPenugasan;
    }

    public String getJumlahOutstanding() {
        return jumlahOutstanding;
    }

    public void setJumlahOutstanding(String jumlahOutstanding) {
        this.jumlahOutstanding = jumlahOutstanding;
    }

    public String getJumlahSudahKunjungan() {
        return jumlahSudahKunjungan;
    }

    public void setJumlahSudahKunjungan(String jumlahSudahKunjungan) {
        this.jumlahSudahKunjungan = jumlahSudahKunjungan;
    }

    public String getJumlahBelumKunjungan() {
        return jumlahBelumKunjungan;
    }

    public void setJumlahBelumKunjungan(String jumlahBelumKunjungan) {
        this.jumlahBelumKunjungan = jumlahBelumKunjungan;
    }


    @Override
    public int getType() {
        return this.getClass().hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_list_report_distribusi_staff;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.binding.setReportDistribusiStaff(this);
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
        private AdapterListReportDistribusiStaffBinding binding;

        public ViewHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);
        }
    }
}
