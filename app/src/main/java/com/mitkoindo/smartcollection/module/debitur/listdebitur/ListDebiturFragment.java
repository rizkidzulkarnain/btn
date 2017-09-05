package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.base.BaseFragment;
import com.mitkoindo.smartcollection.databinding.FragmentListDebiturBinding;
import com.mitkoindo.smartcollection.dialog.DialogSimpleSpinnerAdapter;
import com.mitkoindo.smartcollection.event.EventDialogSimpleSpinnerSelected;
import com.mitkoindo.smartcollection.module.debitur.detaildebitur.DetailDebiturActivity;
import com.mitkoindo.smartcollection.module.debitur.tambahalamat.TambahAlamatActivity;
import com.mitkoindo.smartcollection.module.debitur.tambahtelepon.TambahTeleponActivity;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.objectdata.DebiturItem;
import com.mitkoindo.smartcollection.objectdata.DropDownRelationship;
import com.mitkoindo.smartcollection.utils.SimpleListItemDecoration;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ericwijaya on 8/22/17.
 */

public class ListDebiturFragment extends BaseFragment {

    private static final String EXTRA_STATUS = "extra_status";
    private static final String EXTRA_TYPE = "extra_type";

    private ListDebiturViewModel mListDebiturViewModel;
    private FragmentListDebiturBinding mBinding;
    private Dialog mSpinnerDialog;
    private FastItemAdapter mFastAdapter;
    private FooterAdapter<ProgressItem> mFooterAdapter;
    private String mStatus = RestConstants.LIST_DEBITUR_STATUS_PENDING_VALUE;
    private String mType = ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE;
    private List<String> mListTambah = new ArrayList<>();
    private DebiturItem selectedDebitur;


    public static ListDebiturFragment getInstance() {
        return new ListDebiturFragment();
    }

    public static Fragment getInstance(String status, String type) {
        Fragment fragment = new ListDebiturFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_STATUS, status);
        bundle.putString(EXTRA_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_debitur, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mStatus = args.getString(EXTRA_STATUS);
        mType = args.getString(EXTRA_TYPE);

        mListDebiturViewModel = new ListDebiturViewModel(getAccessToken());
        addViewModel(mListDebiturViewModel);
        mBinding = DataBindingUtil.bind(view);

        mListDebiturViewModel.obsIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mListDebiturViewModel.obsIsLoading.get()) {
                    showLoadingDialog();
                } else {
                    mBinding.swipeRefreshLayoutDebitur.setRefreshing(false);
                    hideLoadingDialog();
                }
            }
        });
        mListDebiturViewModel.error.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                displayMessage(R.string.GagalMendapatkanData);
            }
        });
        mListDebiturViewModel.obsDebiturResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mFastAdapter.clear();
                mFastAdapter.add(mListDebiturViewModel.obsDebiturResponse.get());
                if (mListDebiturViewModel.obsDebiturResponse.get().size() > 0) {
                    mListDebiturViewModel.obsIsEmpty.set(false);
                } else {
                    mListDebiturViewModel.obsIsEmpty.set(true);
                }
            }
        });
        mListDebiturViewModel.obsDebiturResponseLoadMore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mFastAdapter.add(mListDebiturViewModel.obsDebiturResponseLoadMore.get());
            }
        });

        setupArrayTambah();
        setupRecyclerView();

        mListDebiturViewModel.getListDebitur(getUserId(), mStatus, 1);
    }

    private void setupArrayTambah() {
        mListTambah.clear();
        mListTambah.add(getString(R.string.ListDebitur_TambahTelepon));
        mListTambah.add(getString(R.string.ListDebitur_TambahAlamat));
    }

    private static final int PILIH_TAMBAH = 1;
    private void setupRecyclerView() {
        mFastAdapter = new FastItemAdapter();
        mFooterAdapter = new FooterAdapter<>();

        int divider = getResources().getDimensionPixelSize(R.dimen.padding_medium_large);
        RecyclerView.ItemDecoration itemDecoration = new SimpleListItemDecoration(divider, RecyclerView.VERTICAL);
        mBinding.recyclerViewDebitur.addItemDecoration(itemDecoration);
        mBinding.recyclerViewDebitur.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewDebitur.setAdapter(mFooterAdapter.wrap(mFastAdapter));

        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<DebiturItem>() {
            @Override
            public boolean onClick(View v, IAdapter<DebiturItem> adapter, DebiturItem item, int position) {
                if (mType.equals(ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE)) {
                    startActivity(DetailDebiturActivity.instantiate(getActivity(), item.getNoRekening(), item.getCustomerReference()));
                } else if (mType.equals(ListDebiturActivity.EXTRA_TYPE_TAMBAH_KONTAK_VALUE)) {
                    selectedDebitur = item;
                    showInstallmentDialogSimpleSpinner(mListTambah, getString(R.string.ListDebitur_PilihTambah), PILIH_TAMBAH);
                }

                return true;
            }
        });
        mBinding.recyclerViewDebitur.addOnScrollListener(new EndlessRecyclerOnScrollListener(mFooterAdapter) {
            @Override
            public void onLoadMore(final int currentPage) {
                mFooterAdapter.clear();
                mFooterAdapter.add(new ProgressItem().withEnabled(false));

                mListDebiturViewModel.getListDebitur(getUserId(), mStatus, currentPage);
            }
        });

        mBinding.swipeRefreshLayoutDebitur.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefreshLayoutDebitur.setRefreshing(true);
                mListDebiturViewModel.getListDebitur(getUserId(), mStatus, 1);
            }
        });
    }

    private void showInstallmentDialogSimpleSpinner(List<String> nameList, String dialogTitle, int viewId) {
        if (mSpinnerDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder
                    .setTitle(null)
                    .setMessage(null)
                    .setCancelable(true)
                    .setView(R.layout.dialog_simple_spinner);
            mSpinnerDialog = dialogBuilder.create();
        }
        if (nameList.size() > 0) {
            mSpinnerDialog.show();
            RecyclerView recyclerView = (RecyclerView) mSpinnerDialog.findViewById(R.id.rv_dialog_simple_spinner);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            DialogSimpleSpinnerAdapter adapter = new DialogSimpleSpinnerAdapter(nameList, viewId);
            recyclerView.setAdapter(adapter);
            TextView title = (TextView) mSpinnerDialog.findViewById(R.id.tv_dialog_simple_spinner_title);
            title.setText(dialogTitle);
        } else {
            ToastUtils.toastShort(getContext(), getString(R.string.TidakAdaDataPilihan));
        }
    }

    @Subscribe
    public void onDialogSimpleSpinnerSelected(EventDialogSimpleSpinnerSelected event) {
        if (mSpinnerDialog != null && mSpinnerDialog.isShowing()) {
            mSpinnerDialog.dismiss();

            switch (event.getViewId()) {
                case PILIH_TAMBAH: {
                    if (event.getName().equals(getString(R.string.ListDebitur_TambahTelepon))) {
                        startActivity(TambahTeleponActivity.instantiate(getActivity(), selectedDebitur.getNoRekening(), selectedDebitur.getCustomerReference()));
                    } else if (event.getName().equals(getString(R.string.ListDebitur_TambahAlamat))) {
                        startActivity(TambahAlamatActivity.instantiate(getActivity(), selectedDebitur.getNoRekening(), selectedDebitur.getCustomerReference()));
                    }
                    break;
                }
            }
        }
    }
}
