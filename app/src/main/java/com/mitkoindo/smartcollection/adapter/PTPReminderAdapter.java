package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithPTP;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 31/08/2017.
 */

public class PTPReminderAdapter extends RecyclerView.Adapter<PTPReminderAdapter.PTPReminderViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //List debitur with PTP
    private ArrayList<DebiturItemWithPTP> debiturItems = new ArrayList<>();

    //context
    private Activity context;

    //flag data binding
    private boolean onBind;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //progress bar buat initial load / refresh
    private ProgressBar view_ProgressBar;

    //progress bar buat load page baru
    private ProgressBar view_PageLoadIndicator;

    //recycler view
    private RecyclerView view_Recycler;

    //alert text
    private TextView view_Alert;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user id
    private String userID;

    //page counter
    private int currentPage;

    //flag boleh load page baru atau tidak
    private boolean flag_AllowLoadNewPage;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public PTPReminderAdapter(Activity context)
    {
        this.context = context;

        //set default page
        currentPage = 1;

        //set default ke not allow load new page
        flag_AllowLoadNewPage = false;
    }

    @Override
    public PTPReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_ptp_reminder, parent, false);
        return new PTPReminderViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(PTPReminderViewHolder holder, int position)
    {
        //pastikan index tidak keluar dari size
        if (position >= getItemCount())
            return;

        onBind = true;

        //get currentData
        DebiturItemWithPTP currentItem = debiturItems.get(position);

        //attach data
        holder.Name.setText(currentItem.getNama());
        holder.NoRekening.setText(currentItem.getNoRekening());
        holder.Tagihan.setText(currentItem.getTagihan());
        holder.DPD.setText(currentItem.getDpd());
        holder.LastPayment.setText(currentItem.getLastPayment());
        holder.PTP.setText(currentItem.PTPHint);
        holder.PTPAmount.setText(currentItem.PTPAmount);
        holder.kurangSetor.setText(currentItem.KurangSetor);

        //add listener
        final int currentPos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenDetailPage(currentPos);
            }
        });

        onBind = false;
    }

    //----------------------------------------------------------------------------------------------
    //  Setup transaksi
    //----------------------------------------------------------------------------------------------
    @Override
    public int getItemCount()
    {
        return debiturItems.size();
    }

    //set transaction data
    public void SetTransactionData(String baseURL, String url_DataSP, String authToken, String userID)
    {
        this.baseURL = baseURL;
        this.url_DataSP = url_DataSP;
        this.authToken = authToken;
        this.userID = userID;
    }

    //set view
    public void SetViews(ProgressBar view_ProgressBar, ProgressBar view_PageLoadIndicator,
                         RecyclerView view_Recycler, TextView view_Alert)
    {
        this.view_ProgressBar = view_ProgressBar;
        this.view_PageLoadIndicator = view_PageLoadIndicator;
        this.view_Recycler = view_Recycler;
        this.view_Alert = view_Alert;
    }

    //----------------------------------------------------------------------------------------------
    //  Open detail page
    //----------------------------------------------------------------------------------------------
    private void OpenDetailPage(int position)
    {
        //get currentData
        DebiturItemWithPTP currentItem = debiturItems.get(position);

        //open detail page
        Intent intent = null;

        //cek group ID user ini
        String userGroupID = ResourceLoader.LoadGroupID(context);

        //get user group
        final String userGroup_Staff1 = context.getString(R.string.UserGroup_Staff1);
        final String userGroup_Staff2 = context.getString(R.string.UserGroup_Staff2);
        final String userGroup_Staff3 = context.getString(R.string.UserGroup_Staff3);

        //if petugas, dia bisa akses penugasan
        if (userGroupID.equals(userGroup_Staff1) || userGroupID.equals(userGroup_Staff2) || userGroupID.equals(userGroup_Staff3))
            intent = DetailDebiturActivity.instantiate(context, currentItem.getNoRekening(), "", ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE);
        else
            intent = DetailDebiturActivity.instantiate(context, currentItem.getNoRekening(), "", ListDebiturActivity.EXTRA_TYPE_ACCOUNT_ASSIGNMENT_VALUE);
        //else, hanya muncul history tindakan & gallery

        context.startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get PTP Reminder
    //----------------------------------------------------------------------------------------------
    public void LoadInitialPTPData()
    {
        //show initial progress bar, hide everything else
        view_ProgressBar.setVisibility(View.VISIBLE);
        view_Alert.setVisibility(View.GONE);
        view_PageLoadIndicator.setVisibility(View.GONE);
        view_Recycler.setVisibility(View.GONE);

        //don't allow load new page ketika sedang load data baru
        flag_AllowLoadNewPage = false;

        //reset page
        currentPage = 1;

        new SendGetPTPData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create request buat load page baru
    public void CreateLoadNewPageRequest()
    {
        if (!flag_AllowLoadNewPage)
            return;

        //Show page load indicator
        view_PageLoadIndicator.setVisibility(View.VISIBLE);

        //don't allow data load lagi ketika transaksi
        flag_AllowLoadNewPage = false;

        //send request
        new SendGetPTPData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create request object buat get PTP list
    private JSONObject CreateGetPTPRequestObject()
    {
        //initialize
        JSONObject requestObject = new JSONObject();

        try
        {
            //create spParameter Object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("page", currentPage);
            spParameterObject.put("limit", 10);

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_PTP_LIST");
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }


    //create request buat get ptp data
    private class SendGetPTPData extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //get request object
            JSONObject requestObject = CreateGetPTPRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetPTPResult(s);
        }
    }

    //handle result
    //handle result
    private void HandleGetPTPResult(String resultString)
    {
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan result tidak kosong
        if (resultString == null || resultString.isEmpty())
        {
            //show alert
            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
            return;
        }

        //pastikan balasan tidak 404
        if (resultString.equals("Not Found"))
        {
            //hide progress bar
            view_PageLoadIndicator.setVisibility(View.GONE);
            view_ProgressBar.setVisibility(View.GONE);

            flag_AllowLoadNewPage = false;

            if (currentPage == 1)
            {
                view_Alert.setText(R.string.Text_NoData);
                view_Alert.setVisibility(View.VISIBLE);
            }
            return;
        }

        try
        {
            //extract data
            JSONArray dataArray = new JSONArray(resultString);

            //initialize data
            ArrayList<DebiturItemWithPTP> newDebiturItems = new ArrayList<>();

            //populate data
            for (int i = 0; i < dataArray.length(); i++)
            {
                DebiturItemWithPTP currentItem = new DebiturItemWithPTP();
                currentItem.ParseData(dataArray.getString(i));

                newDebiturItems.add(currentItem);
            }

            //create adapter dari item
            if (currentPage == 1)
                SetupPTPList(newDebiturItems);
            else
                AddNewPage(newDebiturItems);

            flag_AllowLoadNewPage = true;
            currentPage++;
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            /*view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);*/
        }
    }

    //Setup PTP
    private void SetupPTPList(ArrayList<DebiturItemWithPTP> newDebiturItems)
    {
        //inisialisasi data
        debiturItems = new ArrayList<>();

        //insert data to list
        for (int i = 0; i < newDebiturItems.size(); i++)
        {
            debiturItems.add(newDebiturItems.get(i));
        }

        if (!onBind)
            notifyDataSetChanged();

        //hide progress bar, show recyclerview
        view_PageLoadIndicator.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);
        view_Recycler.setVisibility(View.VISIBLE);
    }

    //add new page
    private void AddNewPage(ArrayList<DebiturItemWithPTP> newDebiturItems)
    {
        //add data ke dalam list
        for (int i = 0; i < newDebiturItems.size(); i++)
        {
            debiturItems.add(newDebiturItems.get(i));
        }

        if (!onBind)
            notifyDataSetChanged();

        //hide page load indicator
        view_PageLoadIndicator.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class PTPReminderViewHolder extends RecyclerView.ViewHolder
    {
        TextView Name;
        TextView NoRekening;
        TextView Tagihan;
        TextView DPD;
        TextView LastPayment;
        TextView PTP;
        TextView PTPAmount;
        TextView kurangSetor;

        public PTPReminderViewHolder(View itemView)
        {
            super(itemView);

            Name = itemView.findViewById(R.id.text_view_nama_debitur);
            NoRekening = itemView.findViewById(R.id.text_view_no_rekening_value);
            Tagihan = itemView.findViewById(R.id.text_view_tagihan_value);
            DPD = itemView.findViewById(R.id.text_view_dpd_value);
            LastPayment = itemView.findViewById(R.id.text_view_last_payment_value);
            PTP = itemView.findViewById(R.id.text_view_PTP_value);
            PTPAmount = itemView.findViewById(R.id.text_view_PTPAmount_value);
            kurangSetor = itemView.findViewById(R.id.text_view_KurangSetor_value);
        }
    }

    class PTPSummaryViewHolder extends RecyclerView.ViewHolder
    {
        TextView jumlahDebitur;
        TextView jumlahOutstanding;

        public PTPSummaryViewHolder(View itemView)
        {
            super(itemView);

            jumlahDebitur = itemView.findViewById(R.id.PTP_Summary_JumlahDebitur);
            jumlahOutstanding = itemView.findViewById(R.id.PTP_Summary_JumlahOutstanding);
        }
    }
}
