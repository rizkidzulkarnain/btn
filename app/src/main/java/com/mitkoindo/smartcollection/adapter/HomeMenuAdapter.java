package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ItemClickListener;
import com.mitkoindo.smartcollection.objectdata.HomeMenu;
import com.mitkoindo.smartcollection.objectdata.HomeMenuBadge;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.HomeMenuViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //list of menu items
    private ArrayList<HomeMenu> homeMenus;

    //context
    private Activity context;

    //flag binder
    private boolean onBind;

    //----------------------------------------------------------------------------------------------
    //  Input
    //----------------------------------------------------------------------------------------------
    //click listener
    private ItemClickListener itemClickListener;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    private String baseURL;
    private String url_DataSP;

    private String authToken;
    private String userID;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public HomeMenuAdapter(Activity context, ArrayList<HomeMenu> homeMenus)
    {
        this.context = context;
        this.homeMenus = homeMenus;
    }

    @Override
    public HomeMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_home_menu, parent, false);
        return new HomeMenuViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(HomeMenuViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi index
        if (position >= getItemCount())
            return;

        //bind view
        onBind = true;

        //get object
        HomeMenu homeMenu = homeMenus.get(position);

        //attach data
        holder.Name.setText(homeMenu.Name);

        //cek bitmap
        if (homeMenu.Icon != null)
            holder.Icon.setImageBitmap(homeMenu.Icon);

        //show / hide badge
        if (homeMenu.BadgeCounter <= 0)
        {
            holder.badgeHolder.setVisibility(View.GONE);
        }
        else
        {
            holder.badgeHolder.setVisibility(View.VISIBLE);
            holder.badgeValue.setText(Integer.toString(homeMenu.BadgeCounter));
        }

        //release bind
        onBind = false;
    }

    @Override
    public int getItemCount()
    {
        return homeMenus.size();
    }

    //get current menu
    public String getCurrentMenu(int position)
    {
        //pastikan posisi tidak melebihi index
        if (position >= homeMenus.size())
            return "Out of bounds";

        //return menu name
        return homeMenus.get(position).Name;
    }

    //----------------------------------------------------------------------------------------------
    //  Set transaksi
    //----------------------------------------------------------------------------------------------
    public void SetTransaction(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get badge data
    //----------------------------------------------------------------------------------------------
    public void CreateGetBadgeData()
    {
        new SendGetBadgeData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create request object
    private JSONObject CreateGetBadgeRequestObject()
    {
        //inisialisasi request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);

            //populate
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_NOTIFICATION_COUNTER");
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
    private class SendGetBadgeData extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetBadgeRequestObject();

            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            AttachBadge(s);
        }
    }

    //attach badge
    private void AttachBadge(String resultString)
    {
        //pastikan result tidak null / empty
        if (resultString == null || resultString.isEmpty())
            return;

        try
        {
            //extract
            JSONArray dataArray = new JSONArray(resultString);

            //add all to object
            ArrayList<HomeMenuBadge> homeMenuBadges = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++)
            {
                HomeMenuBadge homeMenuBadge = new HomeMenuBadge();
                homeMenuBadge.Parse(dataArray.getJSONObject(i));
                homeMenuBadges.add(homeMenuBadge);
            }

            //attach badge to menu
            for (int i = 0; i < homeMenuBadges.size(); i++)
            {
                String currentPageType = homeMenuBadges.get(i).PageType;

                //search pagetype di home menu
                for (int j = 0; j < homeMenus.size(); j++)
                {
                    HomeMenu currentHomeMenu = homeMenus.get(j);

                    //bandingkan pagetype dengan text home menu
                    if (currentPageType.equals("PagePtp") && currentHomeMenu.Name.equals("PTP Reminder"))
                    {
                        currentHomeMenu.BadgeCounter = homeMenuBadges.get(i).Total;
                        break;
                    }
                    else if (currentPageType.equals("PageChat") && currentHomeMenu.Name.equals("Chat"))
                    {
                        currentHomeMenu.BadgeCounter = homeMenuBadges.get(i).Total;
                        break;
                    }
                    else if (currentPageType.equals("PageNewsGroup") && currentHomeMenu.Name.equals("Berita"))
                    {
                        currentHomeMenu.BadgeCounter = homeMenuBadges.get(i).Total;
                        break;
                    }
                    else if (currentPageType.equals("PageAssignment") && currentHomeMenu.Name.equals("Penugasan"))
                    {
                        currentHomeMenu.BadgeCounter = homeMenuBadges.get(i).Total;
                        break;
                    }
                }
            }

            if (!onBind)
                notifyDataSetChanged();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    //  Handle Input
    //----------------------------------------------------------------------------------------------
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class HomeMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView Icon;
        TextView Name;
        View badgeHolder;
        TextView badgeValue;

        public HomeMenuViewHolder(View itemView)
        {
            super(itemView);

            Icon = itemView.findViewById(R.id.HomeMenuAdapter_Icon);
            Name = itemView.findViewById(R.id.HomeMenuAdapter_Text);
            badgeHolder = itemView.findViewById(R.id.HomeMenuAdapter_BadgeHolder);
            badgeValue = itemView.findViewById(R.id.HomeMenuAdapter_BadgeCounter);

            //add input listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
