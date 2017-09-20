package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterStaffProductivityBinding;

import java.util.List;

/**
 * Created by ericwijaya on 9/17/17.
 */

public class StaffProductivity extends AbstractItem<StaffProductivity, StaffProductivity.ViewHolder> {

    @SerializedName("UserID")
    @Expose
    private String userID;

    @SerializedName("FullName")
    @Expose
    private String fullName;

    @SerializedName("ActionDate")
    @Expose
    private String actionDate;

    @SerializedName("TimeRange1")
    @Expose
    private String timeRange1;

    @SerializedName("TimeRange2")
    @Expose
    private String timeRange2;

    @SerializedName("TimeRange3")
    @Expose
    private String timeRange3;

    @SerializedName("TimeRange4")
    @Expose
    private String timeRange4;

    @SerializedName("TimeRange5")
    @Expose
    private String timeRange5;

    @SerializedName("TimeRange6")
    @Expose
    private String timeRange6;

    @SerializedName("TimeRange7")
    @Expose
    private String timeRange7;

    @SerializedName("Total")
    @Expose
    private String total;

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

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getTimeRange1() {
        return timeRange1;
    }

    public void setTimeRange1(String timeRange1) {
        this.timeRange1 = timeRange1;
    }

    public String getTimeRange2() {
        return timeRange2;
    }

    public void setTimeRange2(String timeRange2) {
        this.timeRange2 = timeRange2;
    }

    public String getTimeRange3() {
        return timeRange3;
    }

    public void setTimeRange3(String timeRange3) {
        this.timeRange3 = timeRange3;
    }

    public String getTimeRange4() {
        return timeRange4;
    }

    public void setTimeRange4(String timeRange4) {
        this.timeRange4 = timeRange4;
    }

    public String getTimeRange5() {
        return timeRange5;
    }

    public void setTimeRange5(String timeRange5) {
        this.timeRange5 = timeRange5;
    }

    public String getTimeRange6() {
        return timeRange6;
    }

    public void setTimeRange6(String timeRange6) {
        this.timeRange6 = timeRange6;
    }

    public String getTimeRange7() {
        return timeRange7;
    }

    public void setTimeRange7(String timeRange7) {
        this.timeRange7 = timeRange7;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    @Override
    public int getType() {
        return this.getClass().hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_staff_productivity;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.binding.setStaffProductivity(this);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AdapterStaffProductivityBinding binding;

        public ViewHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);
        }
    }
}
