package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 15/09/2017.
 */

public class HomeMenuBadge
{
    public String PageType;
    public int Total;

    //extract
    public void Parse(JSONObject dataObject)
    {
        try
        {
            PageType = dataObject.getString("PageType");
            Total = dataObject.getInt("Total");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
