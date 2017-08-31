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
            setNama(dataObject.getString("NamaNasabah"));
            setNoRekening(dataObject.getString("NomorRekening"));
            setTagihan(dataObject.getString("Tagihan"));
            setDpd(dataObject.getString("DPD"));
            setLastPayment(dataObject.getString("LastPaymentDate"));

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
