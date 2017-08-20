package com.mitkoindo.smartcollection.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.dialog.DialogFactory;
import com.mitkoindo.smartcollection.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ericwijaya on 8/16/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    private List<ILifecycleViewModel> viewModels;
    private Dialog mLoadingDialog;
    private Toast mToastMessage;
    private AlertDialog.Builder mErrorDialogBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View contentView = getLayoutInflater().inflate(getLayoutResourceId(), root, false);
        setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mUnbinder = ButterKnife.bind(this);

        mErrorDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss());

        setupDataBinding(contentView);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    protected abstract @LayoutRes
    int getLayoutResourceId();

    protected abstract void setupDataBinding(View contentView);

    protected void setupToolbar() {
        setupToolbar(null);
    }

    protected void setupToolbar(String screenTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_general);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            TextView tvToolbarTitle = (TextView) findViewById(R.id.text_view_toolbar_title);
            tvToolbarTitle.setText(screenTitle != null ? screenTitle : getString(R.string.app_name));

            View btnBack = findViewById(R.id.image_btn_toolbar_back);
            if (btnBack != null) {
                btnBack.setOnClickListener(view -> onBackPressed());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

//        EventBus.getDefault().unregister(this);
        hideMessageToast();
    }

    @Override
    protected void onDestroy() {
        destroyViewModels();
        mUnbinder.unbind();
        super.onDestroy();
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

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogFactory.createLoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void displayMessage(String message) {
        mToastMessage = ToastUtils.toastLong(this, message);
    }

    public void displayMessage(int messageResId) {
        String message = getString(messageResId);
        mToastMessage = ToastUtils.toastLong(this, message);
    }

    private void hideMessageToast() {
        if (mToastMessage != null) {
            mToastMessage.cancel();
        }
    }

    public Dialog displayErrorDialog(String errorTitle, String errorMessage) {
        AlertDialog alertDialog = mErrorDialogBuilder.setTitle(errorTitle).setMessage(errorMessage)
                .create();
        alertDialog.show();
        return alertDialog;
    }
}
