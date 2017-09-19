package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterListReportDistribusiSummaryBinding;

import java.util.List;

/**
 * Created by ericwijaya on 9/15/17.
 */

public class ReportDistribusiSummary extends AbstractItem<ReportDistribusiSummary, ReportDistribusiSummary.ViewHolder> {

    @SerializedName("TotalSudahKunjungan")
    @Expose
    private String totalSudahKunjungan;

    @SerializedName("TotalBelumKunjungan")
    @Expose
    private String totalBelumKunjungan;


    public ReportDistribusiSummary() {
    }

    public String getTotalSudahKunjungan() {
        return totalSudahKunjungan;
    }

    public void setTotalSudahKunjungan(String totalSudahKunjungan) {
        this.totalSudahKunjungan = totalSudahKunjungan;
    }

    public String getTotalBelumKunjungan() {
        return totalBelumKunjungan;
    }

    public void setTotalBelumKunjungan(String totalBelumKunjungan) {
        this.totalBelumKunjungan = totalBelumKunjungan;
    }


    @Override
    public int getType() {
        return this.getClass().hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_list_report_distribusi_summary;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.binding.setReportDistribusiSummary(this);
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
        private AdapterListReportDistribusiSummaryBinding binding;

        public ViewHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);
        }
    }
}
