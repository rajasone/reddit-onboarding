package com.rajasaboor.redditclient.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.appbar_layout.DetailViewPager;
import com.rajasaboor.redditclient.databinding.DetailFragmentLayoutBinding;
import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by default on 8/8/2017.
 */

public class DetailsFragment extends Fragment {
    private static final String TAG = DetailsFragment.class.getSimpleName();
    private RedditPost post;
    private DetailFragmentLayoutBinding layoutBinding = null;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    public DetailsFragment() {
        Log.d(TAG, "DetailsFragment: start/end");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        layoutBinding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment_layout, container, false);
        Log.d(TAG, "onCreateView: end");
        return layoutBinding.getRoot();
    }

    public RedditPost getPost() {
        return post;
    }

    public void setPost(RedditPost post) {
        this.post = post;
        setUpTheViewPager();
        Log.d(TAG, "setPost: post title ===> " + post.getPostTitle());
        Log.d(TAG, "setPost: post URL ===> " + post.getPostURL());
    }

    private void setUpTheViewPager() {
        Log.d(TAG, "setUpTheViewPager: start");
        if (layoutBinding.detailsViewPager.getAdapter() == null) {
            DetailViewPager detailViewPager = new DetailViewPager(getFragmentManager(), post);
            layoutBinding.detailsViewPager.setAdapter(detailViewPager);
            layoutBinding.detailsTabsLayout.setupWithViewPager(layoutBinding.detailsViewPager);
        }
        Log.d(TAG, "setUpTheViewPager: end");
    }


    /*
    * True means Hide the toolbar
    * False means show the toolbar
     */
    public void hideTheToolbar(boolean hide) {
        if (layoutBinding.detailsTabsLayout != null) {
            layoutBinding.detailsTabsLayout.setVisibility(hide ? View.GONE : View.VISIBLE);
            Log.d(TAG, "hideTheToolbar: Hiding the tab layout");
        } else {
            Log.e(TAG, "hideTheToolbar: Tab layout is NULL");
        }
    }

    public TabLayout getTabLayout() {
        return layoutBinding.detailsTabsLayout;
    }
}
