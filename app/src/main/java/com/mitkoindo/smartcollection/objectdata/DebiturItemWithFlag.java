package com.mitkoindo.smartcollection.objectdata;

/**
 * Created by W8 on 30/08/2017.
 */

//debitur item used in account assignment, have a boolean field to accomodate textbox
public class DebiturItemWithFlag extends DebiturItem
{
    public boolean Checked;

    public DebiturItemWithFlag()
    {
        Checked = false;
    }
}
