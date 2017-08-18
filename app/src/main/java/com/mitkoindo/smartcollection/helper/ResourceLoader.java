package com.mitkoindo.smartcollection.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mitkoindo.smartcollection.R;

public class ResourceLoader
{
    //load menu icon
    public static Bitmap LoadMenuIcon(Activity context, String menuTitle)
    {
        //inisialisasi variable icon
        Bitmap icon = null;

        //load sesuai dengan menu
        switch (menuTitle)
        {
            case "Dashboard" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_dashboard);
                break;
            case "Berita" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_berita);
                break;
            case "Penugasan" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_penugasan);
                break;
            case "Chat" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_chat);
                break;
            case "PTP Reminder" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_ptpreminder);
                break;
            case "Tambah Kontak" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_tambahkontak);
                break;
            case "Laporan" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_laporan);
                break;
            case "Pusat Notifikasi" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_pusatnotifikasi);
                break;
            case "Account Assignment" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_accountasignment);
                break;
            default:
                break;
        }

        //return iconnya
        return icon;
    }
}
