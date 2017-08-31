package com.mitkoindo.smartcollection.objectdata;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mitkoindo.smartcollection.R;

import java.util.List;

/**
 * Created by ericwijaya on 8/30/17.
 */

public class LoadMoreListItem extends AbstractItem<LoadMoreListItem, LoadMoreListItem.ViewHolder> {

    private LoadMoreListener mListener;

    public LoadMoreListItem(LoadMoreListener listener) {
        mListener = listener;
    }

    public LoadMoreListItem() { }

    @Override
    public int getType() {
        return LoadMoreListItem.class.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_load_more;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        if (mListener != null) {
            mListener.onLoadMore();
        }
    }

    public interface LoadMoreListener {

        void onLoadMore();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
