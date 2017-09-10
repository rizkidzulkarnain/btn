package com.mitkoindo.smartcollection.dialog;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mitkoindo.smartcollection.BR;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.databinding.ItemDialogTwoLineSpinnerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericwijaya on 9/8/17.
 */

public class DialogTwoLineSpinnerAdapter extends RecyclerView.Adapter<DialogTwoLineSpinnerAdapter.BindingViewHolder> {

    private List<TwoLineSpinner> itemList = new ArrayList<>();
    private int viewId;

    public DialogTwoLineSpinnerAdapter(List<TwoLineSpinner> itemList, int viewId) {
        this.itemList = itemList != null ? itemList : new ArrayList<>();
        this.viewId = viewId;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private TwoLineSpinner getItemData(int position) {
        return (itemList.size() > position) ? itemList.get(position) : null;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_two_line_spinner, parent, false);
        return new BindingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        TwoLineSpinner item = getItemData(position);
        holder.binding.setVariable(BR.dialogTwoLineSpinnerItemVM, new DialogTwoLineSpinnerItemViewModel(item, viewId));
        holder.binding.executePendingBindings();
    }

    public static class TwoLineSpinner {
        public String title;
        public String description;

        public TwoLineSpinner() {
        }
    }

    static class BindingViewHolder extends RecyclerView.ViewHolder {
        ItemDialogTwoLineSpinnerBinding binding;

        BindingViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
