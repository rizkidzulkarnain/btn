package com.mitkoindo.smartcollection.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

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
            case "Assignment" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_accountasignment);
                break;
            case "Check-In" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_checkin);
                break;
            case "Agent Tracking" :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.menuicon_agenttracking);
                break;
            default:
                break;
        }

        //return iconnya
        return icon;
    }

    //load base url
    public static String LoadBaseURL(Context context)
    {
        //initialize base url string
        String baseURL = "";

        //cek data base url di shared preference
        String key_BaseURL = context.getString(R.string.SharedPreferenceKey_BaseURL);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        baseURL = sharedPreferences.getString(key_BaseURL, "");

        //cek apakah string base url kosong atau tidak
        if (baseURL.isEmpty())
        {
            //jika kosong, load dari resource
            baseURL = context.getString(R.string.BaseURL);
            return baseURL;
        }

        //jika tidak kosong, return hasil get base url dari shared preference
        return baseURL;
    }

    //load auth token
    public static String LoadAuthToken(Context context)
    {
        //get auth token
        String key_AuthToken = context.getString(R.string.SharedPreferenceKey_AccessToken);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key_AuthToken, "");
    }

    //load user ID
    public static String LoadUserID(Context context)
    {
        String key_UserID = context.getString(R.string.SharedPreferenceKey_UserID);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key_UserID, "");
    }

    //load group ID
    public static String LoadGroupID(Context context)
    {
        String userGroupIDKey = context.getString(R.string.SharedPreferenceKey_UserGroupID);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(userGroupIDKey, "");
    }

    //load user name
    public static String LoadUserName(Context context)
    {
        String key_UserName = context.getString(R.string.SharedPreferenceKey_NamaPetugas);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key_UserName, "");
    }

    //load group name
    public static String LoadGroupName(Context context)
    {
        String key_UserGroup = context.getString(R.string.SharedPreferenceKey_UserGroup);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key_UserGroup, "");
    }
}
