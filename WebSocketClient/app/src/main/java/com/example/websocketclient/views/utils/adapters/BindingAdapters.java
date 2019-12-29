package com.example.websocketclient.views.utils.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("android:text")
    public static void afterInputEditText(TextView view, int visibility) {
        view.setText("");
    }
}
