package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 30/08/2017.
 */

//debitur item used in account assignment, have a boolean field to accomodate textbox
public class DebiturItemWithFlag extends DebiturItem
{
    public boolean Checked;

    public String TanggalKunjungan;

    public String IDVisit;

    public String IDCall;

    public DebiturItemWithFlag()
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

            if (dataObject.has("TanggalKunjungan"))
            {
                TanggalKunjungan = dataObject.getString("TanggalKunjungan");
            }

            if (dataObject.has("IDVisit"))
            {
                IDVisit = dataObject.getString("IDVisit");
            }

            if (dataObject.has("IDCall"))
            {
                IDCall = dataObject.getString("IDCall");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
