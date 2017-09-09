package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 09/09/2017.
 */

public class ChatItem
{
    public int No;
    public int ID;
    public String FromUserID;
    public String ToUserID;
    public String Message;
    public String SendDate;
    public String ReadDate;

    //parse data
    public void ParseData(JSONObject dataObject)
    {
        try
        {
            No = dataObject.getInt("No");
            ID = dataObject.getInt("ID");
            FromUserID = dataObject.getString("FromUserID");
            ToUserID = dataObject.getString("ToUserID");
            Message = dataObject.getString("Message");
            SendDate = dataObject.getString("SendDate");
            ReadDate = dataObject.getString("ReadDate");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
