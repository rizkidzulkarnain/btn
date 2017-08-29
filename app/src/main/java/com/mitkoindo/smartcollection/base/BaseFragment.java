package com.mitkoindo.smartcollection.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ericwijaya on 8/22/17.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    private List<ILifecycleViewModel> viewModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyViewModels();
    }

    private void destroyViewModels() {
        if (viewModels == null) return;
        for (ILifecycleViewModel vm : viewModels) {
            vm.onDestroy();
        }
    }

    protected void addViewModel(ILifecycleViewModel vm) {
        if (viewModels == null) {
            viewModels = new ArrayList<>();
        }
        if (!viewModels.contains(vm)) {
            viewModels.add(vm);
        }
    }

    protected void showLoadingDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoadingDialog();
        }
    }

    protected void hideLoadingDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoadingDialog();
        }
    }

    protected void displayMessage(String message) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).displayMessage(message);
        }
    }

    protected void displayMessage(int messageResId) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).displayMessage(messageResId);
        }
    }

    protected String getAccessToken() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getAccessToken();
        } else {
            return "";
        }
    }
}
