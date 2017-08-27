package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 27/08/2017.
 */

public class DashboardNPLData
{
    public String Kategori;
    public String NPL;
    public String SaldoPokokNPL;

    //parse data
    public void Parse(JSONObject dataObject)
    {
        try
        {
            Kategori = dataObject.getString("Kategori");
            NPL = dataObject.getString("NPL");
            SaldoPokokNPL = dataObject.getString("SaldoPokokNPL");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
