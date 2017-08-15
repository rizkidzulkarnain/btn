package com.mitkoindo.smartcollection.objectdata;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DrawerMenu
{
    public Bitmap icon;
    public String menuText;
    public boolean showSeparator;

    public DrawerMenu(Activity context, int iconID, String menuText, boolean showSeparator)
    {
        this.icon = BitmapFactory.decodeResource(context.getResources(), iconID);
        this.menuText = menuText;
        this.showSeparator = showSeparator;
    }
}
