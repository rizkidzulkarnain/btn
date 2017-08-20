package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseActivity;
import com.mitkoindo.smartcollection.databinding.ActivityListDebiturBinding;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericwijaya on 8/16/17.
 */

public class ListDebiturActivity extends BaseActivity {

    private ListDebiturViewModel mListDebiturViewModel;
    private ActivityListDebiturBinding mBinding;
    private FastItemAdapter<IItem> mAdapter = new FastItemAdapter<>();

    public static Intent instantiate(Context context) {
        Intent intent = new Intent(context, ListDebiturActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar(getString(R.string.ListDebitur_PageTitle));
        setupRecyclerView();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_list_debitur;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        mListDebiturViewModel = new ListDebiturViewModel();
        addViewModel(mListDebiturViewModel);
        mBinding = DataBindingUtil.bind(contentView);
    }

    private void setupRecyclerView() {

        FastItemAdapter fastAdapter = new FastItemAdapter();

        List<DebiturItem> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DebiturItem item = new DebiturItem();
            item.setNama("Indra Susilo Setiawan");
            item.setNoRekening("172371237");
            item.setTagihan("1.500.000");
            item.setDpd("25");
            item.setLastPayment("2017-08-17");

            items.add(item);
        }
        fastAdapter.add(items);

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewDebitur.addItemDecoration(itemDecoration);
        mBinding.recyclerViewDebitur.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerViewDebitur.setAdapter(fastAdapter);

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<DebiturItem>() {
            @Override
            public boolean onClick(View v, IAdapter<DebiturItem> adapter, DebiturItem item, int position) {
                startActivity(DetailDebiturActivity.instantiate(ListDebiturActivity.this));

                return true;
            }
        });

    }
}
