package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class HistoriTindakan
{
    public String TanggalAktifitas;
    public String HasilVisitDesc;
    public String OrangYangDikunjungi;
    public String HubunganYangDikunjungiDesc;
    public String NomorRekening;

    //parse form JSON
    public void ParseData(String jsonString)
    {
        try
        {
            //converst string to json object
            JSONObject dataObject = new JSONObject(jsonString);

            //get data
            TanggalAktifitas = dataObject.getString("TanggalAktifitas");
            HasilVisitDesc = dataObject.getString("HasilVisitDesc");
            OrangYangDikunjungi = dataObject.getString("OrangYangDikunjungi");
            HubunganYangDikunjungiDesc = dataObject.getString("HubunganYangDikunjungiDesc");
            NomorRekening = dataObject.getString("NomorRekening");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
