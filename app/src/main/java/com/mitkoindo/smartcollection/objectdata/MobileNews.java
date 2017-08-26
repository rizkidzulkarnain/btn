package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 25/08/2017.
 */

public class MobileNews
{
    public int RowNumber;
    public int ID;
    public String AuthorID;
    public String Title;
    public String Summary;
    public String NewsContent;
    public String StartDate;
    public String EndDate;
    public boolean IsGlobal;
    public String Attachment;
    public String CreatedDate;
    public String ModifiedDate;
    public String ToUserID;

    //parse data
    public void ParseData(JSONObject dataObject)
    {
        try
        {
            RowNumber = dataObject.getInt("RowNumber");
            ID = dataObject.getInt("ID");
            AuthorID = dataObject.getString("AuthorID");
            Title = dataObject.getString("Title");
            Summary = dataObject.getString("Summary");
            NewsContent = dataObject.getString("NewsContent");
            StartDate = dataObject.getString("StartDate");
            EndDate = dataObject.getString("EndDate");
            IsGlobal = dataObject.getBoolean("IsGlobal");
            Attachment = dataObject.getString("Attachment");
            CreatedDate = dataObject.getString("CreatedDate");
            ModifiedDate = dataObject.getString("ModifiedDate");
            ToUserID = dataObject.getString("ToUserID");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
