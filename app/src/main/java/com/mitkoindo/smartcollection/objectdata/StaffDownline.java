package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterStaffDownlineBinding;

import java.util.List;

/**
 * Created by ericwijaya on 9/19/17.
 */

public class StaffDownline extends AbstractItem<StaffDownline, StaffDownline.ViewHolder> {

    @SerializedName("UserID")
    @Expose
    private String userID;

    @SerializedName("FullName")
    @Expose
    private String fullName;

    @SerializedName("GroupID")
    @Expose
    private String groupID;

    @SerializedName("GroupName")
    @Expose
    private String groupName;


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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int getType() {
        return this.getClass().hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_staff_downline;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.binding.setStaffDownline(this);
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
        private AdapterStaffDownlineBinding binding;

        public ViewHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);
        }
    }
}
