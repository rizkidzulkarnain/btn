package com.mitkoindo.smartcollection.backgroundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by W8 on 15/09/2017.
 */

public class ChatUpdaterService extends Service
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    private final IBinder mBinder = new ChatUpdaterBinder();

    //----------------------------------------------------------------------------------------------
    //  Method
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    //create binder
    public class ChatUpdaterBinder extends Binder
    {
        public  ChatUpdaterService GetService()
        {
            return ChatUpdaterService.this;
        }
    }
}
