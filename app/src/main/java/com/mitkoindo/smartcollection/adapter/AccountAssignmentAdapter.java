package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.StringHelper;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.listdebitur.ListDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithFlag;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithPetugas;
import com.mitkoindo.smartcollection.utilities.GenericAlert;
import com.mitkoindo.smartcollection.utilities.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by W8 on 29/08/2017.
 */

public class AccountAssignmentAdapter extends RecyclerView.Adapter<AccountAssignmentAdapter.AccountAssignmentViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    private ArrayList<DebiturItemWithPetugas> debiturItems = new ArrayList<>();

    //reference ke context
    private Activity context;

    //counter debiture yang diselect
    private int count_SelectedDebitur;

    //max jumlah debitur yang boleh diselect
    private final int maxSelectedDebitur = 10;

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //assign button
    private Button button_Assign;

    //generic alert
    private GenericAlert genericAlert;

    //recyclerview
    private RecyclerView view_Recycler;

    //progress bar
    private ProgressBar view_ProgressBar;

    //indicator load new page
    private ProgressBar view_PageLoadIndicator;

    //alert text
    private TextView view_Alert;

    //flag binding
    private boolean onBind;

    //----------------------------------------------------------------------------------------------
    //  Transaksi
    //----------------------------------------------------------------------------------------------
    //url
    private String baseURL;
    private String url_DataSP;

    //auth token;
    private String authToken;

    //user id
    private String userID;

    //search query
    private String searchQuery = "";

    //page counter
    private int currentPage;

    //flag boleh load page baru atau tidak
    private boolean flag_AllowLoadNewPage;

    //sorting
    private String sortParameter;

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public AccountAssignmentAdapter(Activity context)
    {
        this.context = context;

        //set jumlah debitur yang diselect jadi nol
        count_SelectedDebitur = 0;

        //set page
        currentPage = 1;

        //create generic alert
        genericAlert = new GenericAlert(context);
    }

    //set reference ke assign button
    public void SetAssignButton(Button button_Assign)
    {
        this.button_Assign = button_Assign;
    }

    @Override
    public AccountAssignmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //inflate layout
        LayoutInflater inflater = context.getLayoutInflater();
        View thisView = inflater.inflate(R.layout.adapter_debitur_assignment, parent, false);
        return new AccountAssignmentViewHolder(thisView);
    }

    @Override
    public void onBindViewHolder(AccountAssignmentViewHolder holder, int position)
    {
        //pastikan posisi tidak melebihi size
        if (position >= getItemCount())
            return;

        //set data ke bind
        onBind = true;

        //get current data
        DebiturItemWithPetugas currentDebitur = debiturItems.get(position);

        //set item
        holder.Name.setText(currentDebitur.getNama());
        holder.NoRekening.setText(currentDebitur.getNoRekening());
        holder.Tagihan.setText(currentDebitur.getTagihan());
        holder.DPD.setText(currentDebitur.getDpd());
        holder.LastPayment.setText(currentDebitur.getLastPayment());

        //set checkbox
        holder.checkBox.setOnCheckedChangeListener(null);
        if (currentDebitur.Checked)
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);

        //add listener to checkbox
        final int currentPos = position;
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                //update jumlah debitur yang diselect
                if (compoundButton.isChecked())
                {
                    //tes, tidak bisa select lebih dari 5 orang
                    if (count_SelectedDebitur >= maxSelectedDebitur)
                    {
                        String title = context.getString(R.string.Text_MohonMaaf);
                        String message = context.getString(R.string.AccountAssignment_Alert_MaxDebiturReached);
                        genericAlert.DisplayAlert(message, title);
                        compoundButton.setChecked(!b);
                        return;
                    }

                    //tambah jumlah debitur yang diselect
                    count_SelectedDebitur++;
                }
                else
                {
                    //kurangi jumlah debitur yang diselect
                    count_SelectedDebitur--;
                }

                //update checked status
                debiturItems.get(currentPos).Checked = compoundButton.isChecked();

                //update display assign button
                UpdateView_AssignButton();
            }
        });

        holder.infoSection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenDetail(position);
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
    public void SetViews(ProgressBar view_ProgressBar, RecyclerView view_Recycler, TextView view_Alert,
                         ProgressBar view_PageLoadIndicator)
    {
        this.view_ProgressBar = view_ProgressBar;
        this.view_Recycler = view_Recycler;
        this.view_Alert = view_Alert;
        this.view_PageLoadIndicator = view_PageLoadIndicator;
    }

    //set sorting parameter
    public void SetSortParameter(String sortParameter)
    {
        this.sortParameter = sortParameter;
    }

    //open detail
    private void OpenDetail(int position)
    {
        //get current data
        DebiturItemWithPetugas currentDebitur = debiturItems.get(position);

        //open detail page
        context.startActivity(DetailDebiturActivity.instantiate(context, currentDebitur.getNoRekening(),
                currentDebitur.getCustomerReference(), ListDebiturActivity.EXTRA_TYPE_ACCOUNT_ASSIGNMENT_VALUE));
    }

    //----------------------------------------------------------------------------------------------
    //  Manipulasi views
    //----------------------------------------------------------------------------------------------
    //get jumlah user yang diselect
    public int GetSelectedDebiturCount()
    {
        return count_SelectedDebitur;
    }

    //get id debitur yang diselect
    public String GetSelectedDebiturAccount()
    {
        //initialize variable
        String selectedDebiturAccount = "";

        //populate data
        for (int i = 0; i < debiturItems.size(); i++)
        {
            //get current item
            DebiturItemWithPetugas currentItem = debiturItems.get(i);

            //cek apakah dia selected atau nggak
            if (!currentItem.Checked)
                continue;

            //kalo selected, add ke string
            selectedDebiturAccount += currentItem.getNoRekening() + ",";
        }

        //element terakhir pasti koma, maka remove
        selectedDebiturAccount = StringHelper.RemoveLastElement(selectedDebiturAccount);

        //return item
        return selectedDebiturAccount;
    }

    //update displaynya assign button
    private void UpdateView_AssignButton()
    {
        //hide assign button jika jumlah debitur yang diselect = 0
        if (count_SelectedDebitur <= 0)
            button_Assign.setVisibility(View.GONE);
        else
            //show assign button jika jumlah debitur yang diselect lebih dari nol
            button_Assign.setVisibility(View.VISIBLE);
    }

    //select all / unselect all item
    public void ToggleSelectAllItem(boolean checked)
    {
        //reset count debitur
        for (int i = 0; i < debiturItems.size(); i++)
        {
            debiturItems.get(i).Checked = checked;
        }

        //update counter
        if (checked)
            count_SelectedDebitur = debiturItems.size();
        else
            count_SelectedDebitur = 0;

        if (!onBind)
            notifyDataSetChanged();

        UpdateView_AssignButton();
    }

    //----------------------------------------------------------------------------------------------
    //  Create request
    //----------------------------------------------------------------------------------------------
    //create request buat get list debitur
    public void CreateGetListDebiturRequest()
    {
        //set views
        view_Alert.setVisibility(View.GONE);
        view_Recycler.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.VISIBLE);
        button_Assign.setVisibility(View.GONE);
        count_SelectedDebitur = 0;
        currentPage = 1;

        //don't allow data load lagi ketika transaksi
        flag_AllowLoadNewPage = false;

        //send request buat get list debitur
        new SendGetListDebitureRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //create request buat search
    public void CreateSearchRequest(String searchQuery)
    {
        this.searchQuery = searchQuery;

        CreateGetListDebiturRequest();
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
        new SendGetListDebitureRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    //----------------------------------------------------------------------------------------------
    //  Execute transaksi
    //----------------------------------------------------------------------------------------------
    //send request buat get list debitur
    private class SendGetListDebitureRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            //create url
            String usedURL = baseURL + url_DataSP;

            //create request object
            JSONObject requestObject = CreateGetDebiturRequestObject();

            //execute request
            NetworkConnection networkConnection = new NetworkConnection(authToken, "");
            networkConnection.SetRequestObject(requestObject);
            return networkConnection.SendPostRequest(usedURL);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            HandleGetDebiturResult(s);
        }
    }

    //create request object
    private JSONObject CreateGetDebiturRequestObject()
    {
        //create request object
        JSONObject requestObject = new JSONObject();

        try
        {
            //create sp parameter object
            JSONObject spParameterObject = new JSONObject();
            spParameterObject.put("userID", userID);
            /*spParameterObject.put("userID", "BTN0013887");*/
            spParameterObject.put("isAssign", 0);
            spParameterObject.put("filterKeyword", searchQuery);
            /*spParameterObject.put("filterByField", "NamaNasabah");
            spParameterObject.put("filterOperator", "LIKE");*/
            spParameterObject.put("sortByField", sortParameter);
            spParameterObject.put("sortDirection", "DESC");
            spParameterObject.put("page", currentPage);
            spParameterObject.put("limit", 10);
            /*spParameterObject.put("userID", "btn0100011");
            spParameterObject.put("status", "'PENDING'");*/

            //populate request object
            requestObject.put("DatabaseID", "db1");
            requestObject.put("SpName", "MKI_SP_ACCOUNT_ASSIGNMENT_LIST");
            /*requestObject.put("SpName", "MKI_SP_DEBITUR_LIST");*/
            requestObject.put("SpParameter", spParameterObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //return request object
        return requestObject;
    }

    //handle result
    private void HandleGetDebiturResult(String result)
    {
        //hide progress bar
        view_PageLoadIndicator.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);

        //pastikan result nggak kosong
        if (result == null || result.isEmpty())
        {
            view_Alert.setText(R.string.Text_SomethingWrong);
            view_Alert.setVisibility(View.VISIBLE);
            return;
        }

        //pastikan response nggak 404
        if (result.equals("Not Found") && currentPage == 1)
        {
            view_Alert.setText(R.string.Text_NoData);
            view_Alert.setVisibility(View.VISIBLE);
            flag_AllowLoadNewPage = false;
            return;
        }

        try
        {
            //parse result ke json array
            JSONArray resultArray = new JSONArray(result);

            //initialize array
            ArrayList<DebiturItemWithPetugas> newDebiturItems = new ArrayList<>();

            //add item to debitur
            for (int i = 0; i < resultArray.length(); i++)
            {
                //parse item
                DebiturItemWithPetugas debiturItem = new DebiturItemWithPetugas();
                debiturItem.ParseData(resultArray.getString(i));

                //add to list
                newDebiturItems.add(debiturItem);
            }

            //create adapter
            if (currentPage == 1)
                AddItemsToList(newDebiturItems);
            else
                AddNewItems(newDebiturItems);

            //increase page count
            currentPage++;

            //saat transaksi sudah beres, allow load page
            flag_AllowLoadNewPage = true;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //Add item to list
    private void AddItemsToList(ArrayList<DebiturItemWithPetugas> newDebiturItems)
    {
        debiturItems = new ArrayList<>();
        for (int i = 0; i < newDebiturItems.size(); i++)
        {
            debiturItems.add(newDebiturItems.get(i));
        }

        if (!onBind)
            notifyDataSetChanged();

        //update view
        view_Recycler.setVisibility(View.VISIBLE);
        view_Alert.setVisibility(View.GONE);
        view_ProgressBar.setVisibility(View.GONE);
    }

    //add new item
    private void AddNewItems(ArrayList<DebiturItemWithPetugas> newDebiturItems)
    {
        for (int i = 0; i < newDebiturItems.size(); i++)
        {
            debiturItems.add(newDebiturItems.get(i));
        }

        if (!onBind)
            notifyDataSetChanged();

        view_Alert.setVisibility(View.GONE);
    }

    //----------------------------------------------------------------------------------------------
    //  View holder
    //----------------------------------------------------------------------------------------------
    class AccountAssignmentViewHolder extends RecyclerView.ViewHolder/* implements CheckBox.OnCheckedChangeListener*/
    {
        TextView Name;
        TextView NoRekening;
        TextView Tagihan;
        TextView DPD;
        TextView LastPayment;
        CheckBox checkBox;

        View infoSection;

        public AccountAssignmentViewHolder(View itemView)
        {
            super(itemView);

            Name = itemView.findViewById(R.id.text_view_nama_debitur);
            NoRekening = itemView.findViewById(R.id.text_view_no_rekening_value);
            Tagihan = itemView.findViewById(R.id.text_view_tagihan_value);
            DPD = itemView.findViewById(R.id.text_view_dpd_value);
            LastPayment = itemView.findViewById(R.id.text_view_last_payment_value);
            checkBox = itemView.findViewById(R.id.AccountAssignmentAdapter_CheckBox);
            infoSection = itemView.findViewById(R.id.AccountAssignmentAdapter_InfoSection);
        }
    }
}
