package com.mitkoindo.smartcollection.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DetailDebitur;
import com.mitkoindo.smartcollection.objectdata.DropDownAction;
import com.mitkoindo.smartcollection.objectdata.DropDownAddress;
import com.mitkoindo.smartcollection.objectdata.DropDownPurpose;
import com.mitkoindo.smartcollection.objectdata.DropDownReason;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.objectdata.DropDownResult;
import com.mitkoindo.smartcollection.objectdata.DropDownStatusAgunan;
import com.mitkoindo.smartcollection.objectdata.PhoneNumber;

import java.util.List;

/**
 * Created by ericwijaya on 9/9/17.
 */

public class OfflineBundleResponse {

    @SerializedName("DebiturList")
    @Expose
    private List<DebiturItem> debiturList;

    @SerializedName("DebiturDetailList")
    @Expose
    private List<DetailDebitur> debiturDetailList;

    @SerializedName("DebiturPhoneList")
    @Expose
    private List<PhoneNumber> debiturPhoneList;

    @SerializedName("DebiturAddressList")
    @Expose
    private List<DropDownAddress> debiturAddressList;

    @SerializedName("RFPURPOSE")
    @Expose
    private List<DropDownPurpose> rfPurpose;

    @SerializedName("RFRELATIONSHIP")
    @Expose
    private List<DropDownRelationship> rfRelationship;

    @SerializedName("RFRESULTMASTER")
    @Expose
    private List<DropDownResult> rfResultMaster;

    @SerializedName("RFREASONMASTER")
    @Expose
    private List<DropDownReason> rfReasonMaster;

    @SerializedName("RFACTIONMASTER")
    @Expose
    private List<DropDownAction> rfActionMaster;

    @SerializedName("RFCOLSTATUS")
    @Expose
    private List<DropDownStatusAgunan> rfColStatus;


    public List<DebiturItem> getDebiturList() {
        return debiturList;
    }

    public void setDebiturList(List<DebiturItem> debiturList) {
        this.debiturList = debiturList;
    }

    public List<DetailDebitur> getDebiturDetailList() {
        return debiturDetailList;
    }

    public void setDebiturDetailList(List<DetailDebitur> debiturDetailList) {
        this.debiturDetailList = debiturDetailList;
    }

    public List<PhoneNumber> getDebiturPhoneList() {
        return debiturPhoneList;
    }

    public void setDebiturPhoneList(List<PhoneNumber> debiturPhoneList) {
        this.debiturPhoneList = debiturPhoneList;
    }

    public List<DropDownAddress> getDebiturAddressList() {
        return debiturAddressList;
    }

    public void setDebiturAddressList(List<DropDownAddress> debiturAddressList) {
        this.debiturAddressList = debiturAddressList;
    }

    public List<DropDownPurpose> getRfPurpose() {
        return rfPurpose;
    }

    public void setRfPurpose(List<DropDownPurpose> rfPurpose) {
        this.rfPurpose = rfPurpose;
    }

    public List<DropDownRelationship> getRfRelationship() {
        return rfRelationship;
    }

    public void setRfRelationship(List<DropDownRelationship> rfRelationship) {
        this.rfRelationship = rfRelationship;
    }

    public List<DropDownResult> getRfResultMaster() {
        return rfResultMaster;
    }

    public void setRfResultMaster(List<DropDownResult> rfResultMaster) {
        this.rfResultMaster = rfResultMaster;
    }

    public List<DropDownReason> getRfReasonMaster() {
        return rfReasonMaster;
    }

    public void setRfReasonMaster(List<DropDownReason> rfReasonMaster) {
        this.rfReasonMaster = rfReasonMaster;
    }

    public List<DropDownAction> getRfActionMaster() {
        return rfActionMaster;
    }

    public void setRfActionMaster(List<DropDownAction> rfActionMaster) {
        this.rfActionMaster = rfActionMaster;
    }

    public List<DropDownStatusAgunan> getRfColStatus() {
        return rfColStatus;
    }

    public void setRfColStatus(List<DropDownStatusAgunan> rfColStatus) {
        this.rfColStatus = rfColStatus;
    }
}
