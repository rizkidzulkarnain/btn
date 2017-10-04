package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 31/08/2017.
 */

public class DebiturItemWithPTP extends DebiturItem
{
    //data PTP
    public String PTPHint;
    public String Tagihan;
    public String PTPAmount;
    public String KurangSetor;

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
            PTPHint = dataObject.getString("PTPHint");
            Tagihan = dataObject.getString("Tagihan");
            KurangSetor = dataObject.getString("KurangSetor");
            PTPAmount = dataObject.getString("JumlahJanjiBayar");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
