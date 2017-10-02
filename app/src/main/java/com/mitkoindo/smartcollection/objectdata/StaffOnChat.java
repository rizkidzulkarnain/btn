package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 02/10/2017.
 */

public class StaffOnChat extends Staff
{
    public String totalChat;

    //extract data
    //parse data
    public void Parse(JSONObject dataObject)
    {
        try
        {
            LEVEL = dataObject.getString("LEVEL");
            USERID = dataObject.getString("USERID");
            FULL_NAME = dataObject.getString("FULL_NAME");
            UPLINER = dataObject.getString("UPLINER");
            BRANCH_CODE = dataObject.getString("BRANCH_CODE");
            ACTIVE = dataObject.getBoolean("ACTIVE");
            GROUP = dataObject.getString("GROUP");
            GROUP_NAME = dataObject.getString("GROUP_NAME");
            AO_CODE = dataObject.getString("AO_CODE");
            SU_LIMIT = dataObject.getInt("SU_LIMIT");

            //set default flag check ke true
            FLAG_CHECKED = true;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
