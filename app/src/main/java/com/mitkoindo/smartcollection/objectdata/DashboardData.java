package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 27/08/2017.
 */

public class DashboardData
{
    public String Kategori;
    public String Target;
    public String Realisasi;
    public String RealisasiProsentase;
    public String Outstanding;
    public String OutstandingProsentase;
    public double RealisasiProsentaseValue;
    public double OutstandingProsentaseValue;

    //parse data
    public void Parse(JSONObject dataObject)
    {
        try
        {
            Kategori = dataObject.getString("Kategori");
            Target = dataObject.getString("Target");
            Realisasi = dataObject.getString("Realisasi");
            RealisasiProsentase = dataObject.getString("RealisasiProsentase");
            Outstanding = dataObject.getString("Outstanding");
            OutstandingProsentase = dataObject.getString("OutstandingProsentase");
            RealisasiProsentaseValue = dataObject.getDouble("RealisasiProsentaseValue");
            OutstandingProsentaseValue = dataObject.getDouble("OutstandingProsentaseValue");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
