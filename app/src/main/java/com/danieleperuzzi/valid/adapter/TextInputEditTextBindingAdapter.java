package com.danieleperuzzi.valid.adapter;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;

public class TextInputEditTextBindingAdapter {

    @BindingAdapter("focus")
    public static void setFocus(TextInputEditText view, boolean shouldHaveFocus) {
        if (shouldHaveFocus && !view.hasFocus()) {
            view.requestFocus();
        }

        if (!shouldHaveFocus && view.hasFocus()) {
            view.clearFocus();
        }
    }

    @InverseBindingAdapter(attribute = "focus")
    public static boolean hasFocus(TextInputEditText view) {
        return view.hasFocus();
    }

    @BindingAdapter(value = {"onGainFocus", "onLossFocus", "focusAttrChanged"}, requireAll = false)
    public static void setFocusListener(TextInputEditText view, final OnGainFocus onGainFocus, final OnLossFocus onLossFocus, final InverseBindingListener attrChange) {
        view.setOnFocusChangeListener((focusableView, hasFocus) -> {

            if (attrChange != null) {
                attrChange.onChange();
            }

            if (hasFocus && onGainFocus != null) {
                onGainFocus.onGainFocus(view.getText());
            }

            if (!hasFocus && onLossFocus != null) {
                onLossFocus.onLossFocus(view.getText());
            }
        });
    }

    public interface OnGainFocus {
        void onGainFocus(Editable view);
    }

    public interface OnLossFocus {
        void onLossFocus(Editable view);
    }
}
