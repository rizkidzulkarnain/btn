package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithFlag;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 10/09/2017.
 */

public class DebiturArchiveAdapter extends RecyclerView.Adapter<DebiturArchiveAdapter.DebiturViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    //reference ke context
    private Activity context;

    //list of debitur
    private ArrayList<DebiturItemWithFlag> debiturItems = new ArrayList<>();

    //flag data binding
    private boolean onBind;

    //search query
    private String searchQuery;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //base url dan url yang digunakan
    private String baseURL;
    private String url_DataSP;

    //auth token
    private String authToken;

    //user ID
    private String userID;

    //page counter
    private int currentPage;

    //flag boleh load page baru atau tidak
    private boolean flag_AllowLoadNewPage;

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
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public DebiturArchiveAdapter(Activity context)
    {
        this.context = context;

        //set default page
        currentPage = 1;

        //set default ke not allow load new page
        flag_AllowLoadNewPage = false;
    }

    @Override
    public DebiturViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View thisView = layoutInflater.inflate(R.layout.adapter_list_debitur, parent, false);
        return new DebiturViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(DebiturViewHolder holder, int position)
    {
        //pastikan item tidak keluar dari index
        if (position >= getItemCount())
            return;

        //set bind data
        onBind = true;

        //get current item
        DebiturItemWithFlag currentDebitur = debiturItems.get(position);

        //set data
        holder.namaDebitur.setText(currentDebitur.getNama());
        holder.nomorRekening.setText(currentDebitur.getNoRekening());
        holder.lastPayment.setText(currentDebitur.getLastPayment());
        holder.tagihan.setText(currentDebitur.getTagihan());
        holder.dpd.setText(currentDebitur.getDpd());

        //set listener
        final int currentPos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenDetail(currentPos);
            }
        });

        //release bind
        onBind = false;
    }

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

    //open detail
    private void OpenDetail(int position)
    {
        //get current item
        DebiturItemWithFlag currentDebitur = debiturItems.get(position);

        //open detail page
        context.startActivity(DetailDebiturActivity.instantiate(context, currentDebitur.getNoRekening(),
                currentDebitur.getCustomerReference(), ListDebiturActivity.EXTRA_TYPE_ACCOUNT_ASSIGNMENT_VALUE));
    }

    //----------------------------------------------------------------------------------------------
    //  Other functions
    //----------------------------------------------------------------------------------------------
    //create request buat search debitur
    public void SearchDebitur(String query)
    {
        this.searchQuery = query;

        //execute search
        LoadInitialDebiturData();
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
        new SendGetDebiturRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //----------------------------------------------------------------------------------------------
    //  Create request buat get data debitur
    //----------------------------------------------------------------------------------------------
    //load data debitur untuk pertama kali
    public void LoadInitialDebiturData()
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

        //send request
        new SendGetDebiturRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create request object
    private JSONObject CreateGetDebiturRequestObject()
    {
        //inisialisasi request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            spParameterObject.put("page", currentPage);
            spParameterObject.put("limit", 10);
            spParameterObject.put("keyword", searchQuery);
            /*spParameterObject.put("orderBy", "LH.User_Assign_Date");*/
            spParameterObject.put("orderDirection", "ASC");

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_DEBITUR_ARCHIVE_LIST");
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
    private class SendGetDebiturRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //set url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetDebiturRequestObject();

            //send request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetArchiveResult(s);
        }
    }

    //handle result
    private void HandleGetArchiveResult(String resultString)
    {
        //hide progress bar
        view_PageLoadIndicator.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan response tidak kosong / null
        if (resultString == null || resultString.isEmpty())
        {
            //show error
            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
            return;
        }

        //jika response 404, maka jangan allow load new page lagi
        if (resultString.equals("Not Found"))
        {
            flag_AllowLoadNewPage = false;

            if (currentPage == 1)
            {
                view_Alert.setText(R.string.Text_NoData);
                view_Alert.setVisibility(View.VISIBLE);
            }
            return;
        }


        //ekstrak data
        ArrayList<DebiturItemWithFlag> newItems = ExtractData(resultString);

        if (currentPage == 1)
            SetupAdapter(newItems);
        else
            AddNewData(newItems);

        //add page counter
        currentPage++;

        //saat transaksi sudah beres, allow load page
        flag_AllowLoadNewPage = true;
    }

    //ekstrak data response jadi list object
    private ArrayList<DebiturItemWithFlag> ExtractData(String resultString)
    {
        //create list of data
        ArrayList<DebiturItemWithFlag> newItems = new ArrayList<>();

        try
        {
            //parse hasilnya
            JSONArray dataArray = new JSONArray(resultString);

            //insert data dari response
            for (int i = 0; i < dataArray.length(); i++)
            {
                DebiturItemWithFlag item = new DebiturItemWithFlag();
                item.ParseData(dataArray.getString(i));
                newItems.add(item);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return newItems;
    }


    //Setup adapter jika ini request data pertama
    private void SetupAdapter(ArrayList<DebiturItemWithFlag> newItems)
    {
        //reset list data
        debiturItems = new ArrayList<>();

        //add data ke dalam list
        for (int i = 0; i < newItems.size(); i++)
        {
            debiturItems.add(newItems.get(i));
        }

        if (!onBind)
            notifyDataSetChanged();

        //hide progress bar, show recyclerview
        view_ProgressBar.setVisibility(View.GONE);
        view_Recycler.setVisibility(View.VISIBLE);
    }

    //add new data saat request load page baru selesai
    private void AddNewData(ArrayList<DebiturItemWithFlag> newItems)
    {
        //add data ke dalam list
        for (int i = 0; i < newItems.size(); i++)
        {
            debiturItems.add(newItems.get(i));
        }

        if (!onBind)
            notifyDataSetChanged();

        //hide page load indicator
        view_PageLoadIndicator.setVisibility(View.GONE);
    }

    //----------------------------------------------------------------------------------------------
    //  view holder
    //----------------------------------------------------------------------------------------------
    class DebiturViewHolder extends RecyclerView.ViewHolder
    {
        TextView namaDebitur;
        TextView nomorRekening;
        TextView tagihan;
        TextView dpd;
        TextView lastPayment;

        public DebiturViewHolder(View itemView)
        {
            super(itemView);

            namaDebitur = itemView.findViewById(R.id.text_view_nama_debitur);
            nomorRekening = itemView.findViewById(R.id.text_view_no_rekening_value);
            tagihan = itemView.findViewById(R.id.text_view_tagihan_value);
            dpd = itemView.findViewById(R.id.text_view_dpd_value);
            lastPayment = itemView.findViewById(R.id.text_view_last_payment_value);
        }
    }
}
