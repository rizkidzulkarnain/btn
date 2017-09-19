package com.mitkoindo.smartcollection.module.debitur.listdebitur;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mitkoindo.smartcollection.network.RestConstants;

/**
 * Created by ericwijaya on 8/23/17.
 */

public class ListDebiturViewPagerAdapter extends FragmentPagerAdapter {

    private static final int TAB_COUNT = 4;
    private static final int TAB_INDEX_PENDING = 0;
    private static final int TAB_INDEX_IN_PROGRESS = 1;
    private static final int TAB_INDEX_LANCAR = 2;
    private static final int TAB_INDEX_MATURED = 3;

    private String mType = ListDebiturActivity.EXTRA_TYPE_PENUGASAN_VALUE;

    public ListDebiturViewPagerAdapter(FragmentManager fragmentManager, String type) {
        super(fragmentManager);

        mType = type;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_INDEX_PENDING:
                return ListDebiturFragment.getInstance(RestConstants.LIST_DEBITUR_STATUS_PENDING_VALUE, mType);
            case TAB_INDEX_IN_PROGRESS:
                return ListDebiturFragment.getInstance(RestConstants.LIST_DEBITUR_STATUS_IN_PROGRESS_VALUE, mType);
            case TAB_INDEX_LANCAR:
                return ListDebiturFragment.getInstance(RestConstants.LIST_DEBITUR_STATUS_LANCAR_VALUE, mType);
            case TAB_INDEX_MATURED:
                return ListDebiturFragment.getInstance(RestConstants.LIST_DEBITUR_STATUS_MATURED_VALUE, mType);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TAB_INDEX_PENDING:
                return RestConstants.LIST_DEBITUR_STATUS_PENDING_VALUE;
            case TAB_INDEX_IN_PROGRESS:
                return RestConstants.LIST_DEBITUR_STATUS_IN_PROGRESS_VALUE;
            case TAB_INDEX_LANCAR:
                return RestConstants.LIST_DEBITUR_STATUS_LANCAR_VALUE;
            case TAB_INDEX_MATURED:
                return RestConstants.LIST_DEBITUR_STATUS_MATURED_VALUE;
            default:
                return "";
        }
    }
}
