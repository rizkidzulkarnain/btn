package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 23/08/2017.
 */

public class GlobalNews
{
    public int RowNumber;
    public String NEWSID;
    public String USERID;
    public String CreatedDate;
    public String Subject;
    public String Content;
    public String Publish;
    public String TanggalBerita;

    //parse data
    public void ParseData(JSONObject dataObject)
    {
        try
        {
            RowNumber = dataObject.getInt("RowNumber");
            NEWSID = dataObject.getString("NEWSID");
            USERID = dataObject.getString("USERID");
            CreatedDate = dataObject.getString("CreatedDate");
            Subject = dataObject.getString("Subject");
            Content = dataObject.getString("Content");
            Publish = dataObject.getString("Publish");
            TanggalBerita = dataObject.getString("TanggalBerita");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
