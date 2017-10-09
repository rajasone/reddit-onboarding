package com.rajasaboor.redditclient.binding_util;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.rajasaboor.redditclient.R;

/**
 * Created by rajaSaboor on 8/29/2017.
 */

public class CustomBindingAdapter {
    @BindingAdapter("showColorScheme")
    public static void showColorScheme(SwipeRefreshLayout refreshLayout, boolean applyColorScheme) {
        if (applyColorScheme) {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(refreshLayout.getContext(), R.color.colorRedHundred), ContextCompat.getColor(refreshLayout.getContext(), R.color.colorGreen),
                    ContextCompat.getColor(refreshLayout.getContext(), R.color.colorParrot), ContextCompat.getColor(refreshLayout.getContext(), R.color.colorYellow),
                    ContextCompat.getColor(refreshLayout.getContext(), R.color.colorCustom), ContextCompat.getColor(refreshLayout.getContext(), R.color.colorCustomTwo));
        }
    }
}
