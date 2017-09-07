package com.mitkoindo.smartcollection.helper;

/**
 * Created by W8 on 07/09/2017.
 */

public class StringHelper
{
    //remove last element of string
    public static String RemoveLastElement(String item)
    {
        //pastikan item tidak null atau kosong
        if (item == null || item.isEmpty())
            return "";

        //remove last element
        return item.substring(0, item.length() - 1);
    }
}
