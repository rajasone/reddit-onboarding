package com.rajasaboor.redditclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.appbar_layout.DetailViewPager;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.util.Consts;

/**
 * Created by default on 8/8/2017.
 */

public class DetailsFragment extends Fragment {
    private static final String TAG = DetailsFragment.class.getSimpleName();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RedditPost post;
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
        iniViews(view);
        Log.d(TAG, "onCreateView: end");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: start");
        super.onActivityCreated(savedInstanceState);
        viewPager = getActivity().findViewById(R.id.details_view_pager);
        tabLayout = getActivity().findViewById(R.id.details_tabs_layout);
        Log.d(TAG, "onActivityCreated: end");
    }

    public void setUpTheViewPager() {
        Log.d(TAG, "setUpTheViewPager: start");
        DetailViewPager detailViewPager = new DetailViewPager(getFragmentManager(), post);
        viewPager.setAdapter(detailViewPager);
        tabLayout.setupWithViewPager(viewPager);
        Log.d(TAG, "setUpTheViewPager: end");
    }

    private void iniViews(View view) {
        Log.d(TAG, "iniViews: start");
        if (view != null) {
            viewPager = view.findViewById(R.id.details_view_pager);
            tabLayout = view.findViewById(R.id.details_tabs_layout);
        } else {
            Log.e(TAG, "iniViews: View is NULL");
        }
        Log.d(TAG, "iniViews: end");
    }


    public RedditPost getPost() {
        return post;
    }

    public void setPost(RedditPost post) {
        this.post = post;
        Log.d(TAG, "setPost: post title ===> " + post.getPostTitle());
        Log.d(TAG, "setPost: post URL ===> " + post.getPostURL());
    }

    /*
    * True means Hide the toolbar
    * False means show the toolbar
     */
    public void hideOrShowTheTabsToolbar(boolean command) {
        if (tabLayout != null) {
            tabLayout.setVisibility(command ? View.GONE : View.VISIBLE);
            Log.d(TAG, "hideOrShowTheTabsToolbar: Hiding the tab layout");
        } else {
            Log.e(TAG, "hideOrShowTheTabsToolbar: Tab layout is NULL");
        }
    }


}
