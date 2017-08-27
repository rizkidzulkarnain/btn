package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 27/08/2017.
 */

public class DashboardData
{
    public String Kategori;
    public int Target;
    public int Realisasi;
    public double RealisasiProsentase;
    public int Outstanding;
    public double OutstandingProsentase;

    //parse data
    public void Parse(JSONObject dataObject)
    {
        try
        {
            Kategori = dataObject.getString("Kategori");
            Target = dataObject.getInt("Target");
            Realisasi = dataObject.getInt("Realisasi");
            RealisasiProsentase = dataObject.getDouble("RealisasiProsentase");
            Outstanding = dataObject.getInt("Outstanding");
            OutstandingProsentase = dataObject.getDouble("OutstandingProsentase");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
