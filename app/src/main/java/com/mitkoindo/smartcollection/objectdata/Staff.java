package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 25/08/2017.
 */

public class Staff
{
    public String LEVEL;
    public String USERID;
    public String FULL_NAME;
    public String UPLINER;
    public String BRANCH_CODE;
    public boolean ACTIVE;
    public String GROUP;
    public String AO_CODE;
    public int SU_LIMIT;

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
            AO_CODE = dataObject.getString("AO_CODE");
            SU_LIMIT = dataObject.getInt("SU_LIMIT");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
