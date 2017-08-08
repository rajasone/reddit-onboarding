package com.rajasaboor.redditclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajasaboor.redditclient.R;

/**
 * Created by default on 8/8/2017.
 */

public class DetailsFragment extends Fragment {
    private static final String TAG = DetailsFragment.class.getSimpleName();
    private View view;

    public DetailsFragment() {
        Log.d(TAG, "DetailsFragment: start/end");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: end");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        view = inflater.inflate(R.layout.detail_fragment_layout, container, false);
        Log.d(TAG, "onCreateView: end");
        return view;
    }

    public void hideTheToolbar() {
        Log.d(TAG, "hideTheToolbar: start");
        if (view != null) {
            Toolbar toolbar = view.findViewById(R.id.detail_toolbar);
            if (toolbar == null) {
                Log.e(TAG, "hideTheToolbar: Toolbar is NULL");
            } else {
                Log.d(TAG, "hideTheToolbar: Toolbar is NOT NULL");
            }
        }else{
            Log.e(TAG, "hideTheToolbar: VIEW IS NULL");
        }
        Log.d(TAG, "hideTheToolbar: end");
    }
}
