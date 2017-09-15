package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 15/09/2017.
 */

public class NotificationData
{
    public int ID;
    public String UserID;
    public String Message;
    public String PageType;
    public String PageID;
    public String Arguments;
    public String ReadDate;
    public String SendDate;

    //parse data
    public void ParseData(JSONObject dataObject)
    {
        try
        {
            ID = dataObject.getInt("ID");
            UserID = dataObject.getString("UserID");
            Message = dataObject.getString("Message");
            PageType = dataObject.getString("PageType");
            PageID = dataObject.getString("PageID");
            Arguments = dataObject.getString("Arguments");
            ReadDate = dataObject.getString("ReadDate");
            SendDate = dataObject.getString("SendDate");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
