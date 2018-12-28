package com.danieleperuzzi.valid.adapter;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;

public class TextInputLayoutBindingAdapter {

    @BindingAdapter("app:errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }
}
