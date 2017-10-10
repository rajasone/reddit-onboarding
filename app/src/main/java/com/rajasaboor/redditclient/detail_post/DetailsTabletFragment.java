package com.rajasaboor.redditclient.detail_post;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.appbar_layout.DetailPagerAdapter;
import com.rajasaboor.redditclient.databinding.DetailFragmentLayoutBinding;
import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by default on 8/8/2017.
 */

public class DetailsTabletFragment extends Fragment {
    private DetailFragmentLayoutBinding layoutBinding = null;
    private DetailPagerAdapter detailPagerAdapter;

    public static DetailsTabletFragment newInstance() {
        return new DetailsTabletFragment();
    }

    public DetailsTabletFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment_layout, container, false);
        setUpTheViewPager(null);
        return layoutBinding.getRoot();
    }

    private void setUpTheViewPager(RedditPost post) {
        detailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), post);
        layoutBinding.detailsViewPager.setAdapter(detailPagerAdapter);
        layoutBinding.detailsTabsLayout.setupWithViewPager(layoutBinding.detailsViewPager);
    }

    public void setViewPagerPost(RedditPost redditPost) {
        setUpTheViewPager(redditPost);
    }
}
