package com.mitkoindo.smartcollection.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.berita.DetailBeritaGroupActivity;
import com.mitkoindo.smartcollection.module.chat.ChatWindowActivity;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.NotificationData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 16/09/2017.
 */

public class NotificationChecker extends BroadcastReceiver
{
    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url buat get berita
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //id notifikasi yang dibaca
    private int readNotificationID;

    //reference ke context
    private Context context;

    private NotificationManager mNotificationManager;

    //----------------------------------------------------------------------------------------------
    //  Broadcast receiver
    //----------------------------------------------------------------------------------------------
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //simpan reference ke context;
        this.context = context;

        //Load data transaksi
        LoadTransactionData(context);

        //create request buat get info notifikasi
        CreateGetNotificationInfoRequest();
    }

    //load data transaksi
    private void LoadTransactionData(Context context)
    {
        //load url
        baseURL = ResourceLoader.LoadBaseURL(context);
        url_DataSP = context.getString(R.string.URL_Data_StoreProcedure);

        //get auth token
        authToken = ResourceLoader.LoadAuthToken(context);

        //get user ID
        userID = ResourceLoader.LoadUserID(context);
    }

    //----------------------------------------------------------------------------------------------
    //  Create reqeuest buat get data notifikasi
    //----------------------------------------------------------------------------------------------
    //create request buat get info notifikasi
    private void CreateGetNotificationInfoRequest()
    {
        //create request object
        JSONObject requestObject = CreateGetNotificationRequestObject();

        new SendGetNotificationInfoRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestObject);
    }

    //create request object
    private JSONObject CreateGetNotificationRequestObject()
    {
        //create object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            //isi request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_NOTIFICATION_LIST");
            requestObject.put("SpParameter", spParameterObject);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    //send request
    private class SendGetNotificationInfoRequest extends AsyncTask<JSONObject, Void, String>
    {
        @Override
        protected String doInBackground(JSONObject... jsonObjects)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(jsonObjects[0]);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleTransactionResult(s);
        }
    }

    //handle result
    private void HandleTransactionResult(String resultString)
    {
        //pastikan result tidak null
        if (resultString == null || resultString.isEmpty())
            return;

        //pastikan juga balikan tidak 404
        if (resultString.equals("Not Found"))
            return;

        try
        {
            //extract data
            JSONArray dataArray = new JSONArray(resultString);

            //create new list
            ArrayList<NotificationData> newData = new ArrayList<>();

            //extract
            for (int i = 0; i < dataArray.length(); i++)
            {
                NotificationData notificationData = new NotificationData();
                notificationData.ParseData(dataArray.getJSONObject(i));
                newData.add(notificationData);
            }

            //add new data
            if (newData.size() > 0)
                ShowNotification(newData);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulasi views
    //----------------------------------------------------------------------------------------------
    //show notifikasi
    private void ShowNotification(ArrayList<NotificationData> newData)
    {
        //get last data
        NotificationData lastItem = newData.get(newData.size() - 1);

        //get id dari last item sebelumnya
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int previousLastItemID = sharedPreferences.getInt("LastNotificationID", -1);

        /*if (previousLastItemID == lastItem.ID)
            return;*/

        /*ShowAllNotifications(newData);*/

        //create channel notifikasi
        /*CreateNotificationChannel();*/

        //cek apakah id kosong
        if (previousLastItemID == -1)
            //maka tampilkan seluruh notifikasi
            ShowAllNotifications(newData);

        // cek apakah id-nya beda, jika sama, tidak perlu menampilkan notifikasi baru,
        // jika beda, tampilkan notifikasi baru
        else if (previousLastItemID != lastItem.ID)
            ShowNewNotifications(newData, previousLastItemID);

        //simpan id notifikasi terakhir
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("LastNotificationID", newData.get(newData.size() - 1).ID);
        editor.apply();
    }

    //create channel notifikasi
    private void CreateNotificationChannel()
    {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // The id of the channel.
        String id = "smart_collection_channel";

        // The user-visible name of the channel.
        CharSequence name = "SmartCollection Channel";

        // The user-visible description of the channel.
        String description = "SmartCollection Channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);

        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);

        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);
    }

    //show all notification
    private void ShowAllNotifications(ArrayList<NotificationData> newData)
    {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (int i = 0; i < newData.size(); i++)
        {
            //hanya play audio di notifikasi terakhir
            boolean includeAudio = false;
            if (i == newData.size() - 1)
                includeAudio = true;

            //show notifikcation
            DisplayNotification(newData.get(i), includeAudio);
        }
    }

    //show new notification saja
    private void ShowNewNotifications(ArrayList<NotificationData> newData, int previousLastItemID)
    {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        boolean allowShowNotification = false;

        for (int i = 0; i < newData.size(); i++)
        {
            //flag play audio atau tidak
            boolean includeAudio = false;
            if (i == newData.size() - 1)
                includeAudio = true;

            //hanya tampilkan notifikasi yang baru
            if (allowShowNotification)
            {
                DisplayNotification(newData.get(i), includeAudio);
            }
            else
            {
                //cek apakah sudah mencapai notifikasi yang baru atau belum
                if (newData.get(i).ID == previousLastItemID)
                    allowShowNotification = true;
            }
        }

        // jika sampai akhir loop tidak ada notifikasi yang memiliki id yang sama dengan id sebelumnya,
        // berarti list notifikasi sebelumnya sudah diclear, jadi tampilkan semua notifikasi
        if (!allowShowNotification)
        {
            ShowAllNotifications(newData);
        }
    }

    //show notification
    private void DisplayNotification(NotificationData currentNotification, boolean includeAudio)
    {
        //get intent
        Intent intent = DetermineIntent(currentNotification);

        int notificationID = currentNotification.ID;

        //set pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationID,
                intent, PendingIntent.FLAG_ONE_SHOT);

        //build notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(currentNotification.Message))
                        .setContentText(currentNotification.Message)
                        .setAutoCancel(true);

        //set sound untuk notifikasi terakhir
        if (includeAudio)
        {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(defaultSoundUri);
        }

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    private Intent DetermineIntent(NotificationData notificationData)
    {
        //set intent
        Intent intent = null;

        //switch result tergantung item type
        switch (notificationData.PageType)
        {
            case "PagePtp" :
                //open detail debitur
                intent = DetailDebiturActivity.instantiate(context, notificationData.PageID,
                        "", ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE);
                break;
            case "PageChat" :
                //open chat window untuk staff ini
                intent = new Intent(context, ChatWindowActivity.class);
                intent.putExtra("PartnerID", notificationData.PageID);

                //get staffname
                String staffName = notificationData.Message.substring(17);
                intent.putExtra("PartnerName", staffName);
                break;
            case "PageNewsGroup" :
                //open detail berita
                intent = new Intent(context, DetailBeritaGroupActivity.class);
                intent.putExtra("NewsID", notificationData.PageID);
                break;
            case "PageAssignment" :
                //open detail debitur
                intent = DetailDebiturActivity.instantiate(context, notificationData.PageID,
                        "", ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE);
                break;
            default:break;
        }

        return intent;
    }
}
