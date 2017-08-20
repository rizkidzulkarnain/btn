package com.mitkoindo.smartcollection.dialog;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mitkoindo.smartcollection.BR;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.ItemDialogSimpleSpinnerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericwijaya on 8/20/17.
 */

public class DialogSimpleSpinnerAdapter extends RecyclerView.Adapter<DialogSimpleSpinnerAdapter.BindingViewHolder> {

    private List<String> nameList = new ArrayList<>();
    private int viewId;

    public DialogSimpleSpinnerAdapter(List<String> nameList, int viewId) {
        this.nameList = nameList != null ? nameList : new ArrayList<>();
        this.viewId = viewId;
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    private String getItemData(int position) {
        return (nameList.size() > position) ? nameList.get(position) : null;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_simple_spinner, parent, false);
        return new BindingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        String name = getItemData(position);
        holder.binding.setVariable(BR.dialogSimpleSpinnerItemVM, new DialogSimpleSpinnerItemViewModel(name, viewId));
        holder.binding.executePendingBindings();
    }


    static class BindingViewHolder extends RecyclerView.ViewHolder {
        ItemDialogSimpleSpinnerBinding binding;

        BindingViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
