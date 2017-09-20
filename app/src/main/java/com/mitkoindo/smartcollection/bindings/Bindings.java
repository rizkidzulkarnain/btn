package com.mitkoindo.smartcollection.bindings;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mitkoindo.smartcollection.R;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

/**
 * Created by ericwijaya on 8/19/17.
 */

public class Bindings {

    @BindingAdapter("bind:errorText")
    public static void setErrorText(TextInputLayout textInputLayout, String errorText) {
        textInputLayout.setError(errorText);
    }

    @BindingAdapter(value = {"bind:imageUrl"}, requireAll = false)
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(centerCropTransform()
                        .placeholder(R.drawable.ic_photo_white_36dp)
                        .centerCrop())
                .into(imageView);
    }

    @BindingAdapter("bind:imageResourceId")
    public static void loadImage(ImageView imageView, int imageResourceId) {
        Glide.with(imageView.getContext())
                .load(imageResourceId)
                .apply(fitCenterTransform()
                        .fitCenter())
                .into(imageView);
    }

    @BindingAdapter(value = {"bind:selectedValue", "bind:selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(AppCompatSpinner pAppCompatSpinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        pAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (newSelectedValue != null && newSelectedValue.equals(parent.getSelectedItem())) {
                    return;
                }
                newTextAttrChanged.onChange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (newSelectedValue != null) {
            int pos = ((ArrayAdapter<String>) pAppCompatSpinner.getAdapter()).getPosition(newSelectedValue);
            pAppCompatSpinner.setSelection(pos, true);
        }
    }

    @InverseBindingAdapter(attribute = "bind:selectedValue", event = "bind:selectedValueAttrChanged")
    public static String captureSelectedValue(AppCompatSpinner pAppCompatSpinner) {
        return (String) pAppCompatSpinner.getSelectedItem();
    }
}
