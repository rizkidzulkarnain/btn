package com.mitkoindo.smartcollection.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.StringHelper;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithFlag;
import com.mitkoindo.smartcollection.utilities.GenericAlert;

import java.util.ArrayList;

/**
 * Created by W8 on 29/08/2017.
 */

public class AccountAssignmentAdapter extends RecyclerView.Adapter<AccountAssignmentAdapter.AccountAssignmentViewHolder>
{
    //----------------------------------------------------------------------------------------------
    //  Data
    //----------------------------------------------------------------------------------------------
    private ArrayList<DebiturItemWithFlag> debiturItems;

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

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------
    //constructor
    public AccountAssignmentAdapter(Activity context, ArrayList<DebiturItemWithFlag> debiturItems)
    {
        this.context = context;
        this.debiturItems = debiturItems;

        //set jumlah debitur yang diselect jadi nol
        count_SelectedDebitur = 0;

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

        //get current data
        DebiturItemWithFlag currentDebitur = debiturItems.get(position);

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
    }

    @Override
    public int getItemCount()
    {
        return debiturItems.size();
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
            DebiturItemWithFlag currentItem = debiturItems.get(i);

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

    //----------------------------------------------------------------------------------------------
    //  Manipulasi views
    //----------------------------------------------------------------------------------------------
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

    /*//listen to checkbox's state change
    private void CheckboxStateListener(int adapterPos, boolean isChecked)
    {
        //pastikan index nggak keluar dari posisi
        if (adapterPos >= getItemCount())
            return;

        //update checked status
        debiturItems.get(adapterPos).Checked = isChecked;

        //notify changes
        notifyDataSetChanged();
    }*/

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

        public AccountAssignmentViewHolder(View itemView)
        {
            super(itemView);

            Name = itemView.findViewById(R.id.text_view_nama_debitur);
            NoRekening = itemView.findViewById(R.id.text_view_no_rekening_value);
            Tagihan = itemView.findViewById(R.id.text_view_tagihan_value);
            DPD = itemView.findViewById(R.id.text_view_dpd_value);
            LastPayment = itemView.findViewById(R.id.text_view_last_payment_value);
            checkBox = itemView.findViewById(R.id.AccountAssignmentAdapter_CheckBox);

            /*checkBox.setOnCheckedChangeListener(this);*/
        }

        /*@Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            //update checked status
            debiturItems.get(getAdapterPosition()).Checked = compoundButton.isChecked();

            //update jumlah debitur yang diselect
            if (compoundButton.isChecked())
            {
                //tambah jumlah debitur yang diselect
                count_SelectedDebitur++;
            }
            else
            {
                //kurangi jumlah debitur yang diselect
                count_SelectedDebitur--;
            }

            //update display assign button
            UpdateView_AssignButton();

            CheckboxStateListener(getAdapterPosition(), compoundButton.isChecked());

            //pastikan index nggak keluar dari posisi
            if (adapterPos >= getItemCount())
                return;

            //notify changes
            notifyDataSetChanged();
        }*/
    }
}
