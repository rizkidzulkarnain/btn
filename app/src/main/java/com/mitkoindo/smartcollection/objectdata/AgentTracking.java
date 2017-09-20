package com.mitkoindo.smartcollection.objectdata;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.AdapterListAgentTrackingBinding;

import java.util.List;

/**
 * Created by ericwijaya on 9/18/17.
 */

public class AgentTracking extends AbstractItem<AgentTracking, AgentTracking.ViewHolder> {

    @SerializedName("No")
    @Expose
    private String no;

    @SerializedName("ID")
    @Expose
    private String id;

    @SerializedName("UserID")
    @Expose
    private String userID;

    @SerializedName("Latitude")
    @Expose
    private double latitude;

    @SerializedName("Longitude")
    @Expose
    private double longitude;

    @SerializedName("Address")
    @Expose
    private String address;

    @SerializedName("AccountNo")
    @Expose
    private String accountNo;

    @SerializedName("IDVisit")
    @Expose
    private String iDVisit;

    @SerializedName("IDCall")
    @Expose
    private String iDCall;

    @SerializedName("IsCheckin")
    @Expose
    private boolean isCheckin;

    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;

    @SerializedName("FullName")
    @Expose
    private String fullName;

    @SerializedName("TotalPenugasanHariIni")
    @Expose
    private String totalPenugasanHariIni;

    @SerializedName("AkmulasiJumlahPenugasan")
    @Expose
    private String akmulasiJumlahPenugasan;

    private String timeFormatted;

    private int iconDrawable;


    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIDVisit() {
        return iDVisit;
    }

    public void setIDVisit(String iDVisit) {
        this.iDVisit = iDVisit;
    }

    public String getIDCall() {
        return iDCall;
    }

    public void setIDCall(String iDCall) {
        this.iDCall = iDCall;
    }

    public boolean getIsCheckin() {
        return isCheckin;
    }

    public void setIsCheckin(boolean isCheckin) {
        this.isCheckin = isCheckin;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTotalPenugasanHariIni() {
        return totalPenugasanHariIni;
    }

    public void setTotalPenugasanHariIni(String totalPenugasanHariIni) {
        this.totalPenugasanHariIni = totalPenugasanHariIni;
    }

    public String getAkmulasiJumlahPenugasan() {
        return akmulasiJumlahPenugasan;
    }

    public void setAkmulasiJumlahPenugasan(String akmulasiJumlahPenugasan) {
        this.akmulasiJumlahPenugasan = akmulasiJumlahPenugasan;
    }

    public String getTimeFormatted() {
        return timeFormatted;
    }

    public void setTimeFormatted(String timeFormatted) {
        this.timeFormatted = timeFormatted;
    }

    public static final int TYPE_VISIT = 1;
    public static final int TYPE_CALL = 2;
    public static final int TYPE_CHECK_IN = 3;
    public static final int TYPE_TRACKING = 4;
    public int getTrackingType() {
        if (iDVisit != null) {
            return TYPE_VISIT;
        } else if (iDCall != null) {
            return TYPE_CALL;
        } else if (isCheckin) {
            return TYPE_CHECK_IN;
        } else {
            return TYPE_TRACKING;
        }
    }
    public int getIconDrawable() {
        int trackingType = getTrackingType();
        switch (trackingType) {
            case AgentTracking.TYPE_VISIT: {
                return R.drawable.ic_home_map;
            }
            case AgentTracking.TYPE_CALL: {
                return R.drawable.ic_phone;
            }
            case AgentTracking.TYPE_CHECK_IN: {
                return R.drawable.ic_map_marker;
            }
            case AgentTracking.TYPE_TRACKING: {
                return R.drawable.ic_dot;
            }
            default: {
                return R.drawable.ic_dot;
            }
        }
    }

    public void setIconDrawable(int iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    @Override
    public int getType() {
        return this.getClass().hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_list_agent_tracking;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.binding.setAgentTracking(this);
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
        private AdapterListAgentTrackingBinding binding;

        public ViewHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);
        }
    }
}
