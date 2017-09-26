package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class HistoriTindakan
{
    public String DeskripsiTindakan;
    public Date TanggalTindakan;
    public String TanggelTindakan_View;
    public String YangMelakukan;
    public String AlasanTindakan;

    //parse form JSON
    public void ParseData(String jsonString)
    {
        try
        {
            //converst string to json object
            JSONObject dataObject = new JSONObject(jsonString);

            //get data
            DeskripsiTindakan = dataObject.getString("DeskripsiTindakan");
            YangMelakukan = dataObject.getString("YangMelakukan");
            AlasanTindakan = dataObject.getString("AlasanTindakan");

            //parse date
            //Hold dulu, karena nanti disediakan sama API
            TanggelTindakan_View = dataObject.getString("TanggalTindakan");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
