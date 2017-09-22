package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterStaffItemBinding;

import java.util.List;

/**
 * Created by ericwijaya on 9/19/17.
 */

public class StaffItem extends AbstractItem<StaffItem, StaffItem.ViewHolder> {

    @SerializedName("LEVEL")
    @Expose
    private String level;

    @SerializedName("USERID")
    @Expose
    private String userId;

    @SerializedName("FULL_NAME")
    @Expose
    private String fullName;

    @SerializedName("UPLINER")
    @Expose
    private String upLiner;

    @SerializedName("BRANCH_CODE")
    @Expose
    private String branchCode;

    @SerializedName("ACTIVE")
    @Expose
    private boolean active;

    @SerializedName("GROUP")
    @Expose
    private String group;

    @SerializedName("GROUP_NAME")
    @Expose
    private String groupName;


    @SerializedName("AO_CODE")
    @Expose
    private String aoCode;

    @SerializedName("SU_LIMIT")
    @Expose
    private String suLimit;


    public String getLEVEL() {
        return level;
    }

    public void setLEVEL(String level) {
        this.level = level;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUpliner() {
        return upLiner;
    }

    public void setUpLiner(String upLiner) {
        this.upLiner = upLiner;
    }

    public String getBRANCHCODE() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public boolean getACTIVE() {
        return active;
    }

    public void setACTIVE(boolean active) {
        this.active = active;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAoCode() {
        return aoCode;
    }

    public void setAoCode(String aoCode) {
        this.aoCode = aoCode;
    }

    public String getSuLimit() {
        return suLimit;
    }

    public void setSuLimit(String suLimit) {
        this.suLimit = suLimit;
    }


    @Override
    public int getType() {
        return this.getClass().hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_staff_item;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.binding.setStaffItem(this);
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
        private AdapterStaffItemBinding binding;

        public ViewHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);
        }
    }
}
