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
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DebiturItemWithFlag;

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

    //----------------------------------------------------------------------------------------------
    //  Views
    //----------------------------------------------------------------------------------------------
    //assign button
    private Button button_Assign;

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
        if (currentDebitur.Checked)
            holder.checkBox.setChecked(true);
        else
            holder.checkBox.setChecked(false);
    }

    @Override
    public int getItemCount()
    {
        return debiturItems.size();
    }

    //get jumlah user yang diselect
    public int GetSelectedDebiturCount()
    {
        return count_SelectedDebitur;
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
    class AccountAssignmentViewHolder extends RecyclerView.ViewHolder implements CheckBox.OnCheckedChangeListener
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

            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
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

            /*CheckboxStateListener(getAdapterPosition(), compoundButton.isChecked());

            //pastikan index nggak keluar dari posisi
            if (adapterPos >= getItemCount())
                return;
            */

            /*//notify changes
            notifyDataSetChanged();*/
        }
    }
}
