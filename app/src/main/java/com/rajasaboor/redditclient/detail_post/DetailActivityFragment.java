package com.rajasaboor.redditclient.detail_post;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.databinding.DetailFragmentLayoutBinding;
import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by rajaSaboor on 9/5/2017.
 */

public class DetailActivityFragment extends Fragment implements DetailPostContract.View {
    private static final String TAG = DetailActivityFragment.class.getSimpleName();
    private DetailFragmentLayoutBinding detailFragmentLayoutBinding = null;

    public static DetailActivityFragment newInstance() {
        return new DetailActivityFragment();
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
        detailFragmentLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment_layout, container, false);
        setHasOptionsMenu(true);


        initToolbar();
        Log.d(TAG, "onCreateView: end");
        return detailFragmentLayoutBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: start");
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: end");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: edn");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: start");
        switch (item.getItemId()) {
            case R.id.share_menu:
                break;
        }
        Log.d(TAG, "onOptionsItemSelected: end");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(detailFragmentLayoutBinding.detailToolbar.customToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("abc");
    }

    @Override
    public void setUpViewPager() {

    }

}
