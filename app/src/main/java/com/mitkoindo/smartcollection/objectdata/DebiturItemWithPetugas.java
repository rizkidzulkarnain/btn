package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 10/09/2017.
 */

public class DebiturItemWithPetugas extends DebiturItem
{
    public boolean Checked;
    public String ExpiredDate;
    public int IsAssign;
    public String NamaPetugas;
    public String GroupPetugas;
    public String UserIdPetugas;
    public String Note;
    public String NoteFrom;

    public DebiturItemWithPetugas()
    {
        Checked = false;
    }

    //parse form JSON
    public void ParseData(String jsonString)
    {
        try
        {
            //converst string to json object
            JSONObject dataObject = new JSONObject(jsonString);

            //get data
            setNo(dataObject.getString("No"));
            setNoCif(dataObject.getString("NoCIF"));
            setNama(dataObject.getString("NamaNasabah"));
            setNoRekening(dataObject.getString("NomorRekening"));
            setTagihan(dataObject.getString("Tagihan"));
            setDpd(dataObject.getString("DPD"));
            setLastPayment(dataObject.getString("LastPaymentDate"));
            setUseAssignDate(dataObject.getString("UserAssignDate"));
            setCustomerReference(dataObject.getString("CustomerReference"));
            ExpiredDate = dataObject.getString("ExpiredDate");
            IsAssign = dataObject.getInt("IsAssign");
            NamaPetugas = dataObject.getString("NamaPetugas");
            GroupPetugas = dataObject.getString("GroupPetugas");
            UserIdPetugas = dataObject.getString("UserIdPetugas");
            Note = dataObject.getString("Note");
            NoteFrom = dataObject.getString("NoteFrom");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
