package com.rajasaboor.redditclient.view_recycler;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.detail_post.DetailActivity;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.adapter.ItemsAdapter;
import com.rajasaboor.redditclient.databinding.ActivityMainBinding;
import com.rajasaboor.redditclient.fragments.DetailsFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.ArrayList;
import java.util.List;


public class ViewActivity extends AppCompatActivity {
    private static final String TAG = ViewActivity.class.getSimpleName(); // Tag name for the Debug purposes
    private ActivityMainBinding mainBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mainBinding.toolbarInclude.customToolbar);


        // setting the fragment for mobile devices
        ViewPostFragment viewPostFragment = (ViewPostFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        if (viewPostFragment == null) {
            viewPostFragment = ViewPostFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, viewPostFragment).commit();
        }
        viewPostFragment.setViewPresenter(new ViewPresenter(viewPostFragment, getSharedPreferences(BuildConfig.SHARED_PREFS_NAME, MODE_PRIVATE)));

        // if tablet layout is activated inflate the detail fragment
        if (mainBinding.detailFragmentContainer != null) {
            setUpTheTabletLayout();
        }
    }

    public void showProgressBar(boolean show) {
        mainBinding.toolbarInclude.menuProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public ActivityMainBinding getMainBinding() {
        return mainBinding;
    }

    private void setUpTheTabletLayout() {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
        if (detailsFragment == null) {
            detailsFragment = DetailsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, detailsFragment)
                    .commit();
        }
        mainBinding.detailFragmentContainer.setVisibility(View.GONE); // hide the fragment if user tap on post then it will be visible
    }
}
