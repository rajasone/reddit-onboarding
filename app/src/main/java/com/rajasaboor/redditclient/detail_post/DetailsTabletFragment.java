package com.rajasaboor.redditclient.detail_post;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.appbar_layout.DetailViewPager;
import com.rajasaboor.redditclient.databinding.DetailFragmentLayoutBinding;
import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by default on 8/8/2017.
 */

public class DetailsTabletFragment extends Fragment {
    private static final String TAG = DetailsTabletFragment.class.getSimpleName();
    private DetailFragmentLayoutBinding layoutBinding = null;

    public static DetailsTabletFragment newInstance(RedditPost redditPost) {
        Bundle bundle = new Bundle();
        DetailsTabletFragment detailsTabletFragment = new DetailsTabletFragment();
        bundle.putParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY, redditPost);
        detailsTabletFragment.setArguments(bundle);

        return detailsTabletFragment;
    }

    public DetailsTabletFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        layoutBinding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment_layout, container, false);
        setUpTheViewPager((RedditPost) getArguments().getParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY));
        Log.d(TAG, "onCreateView: end");
        return layoutBinding.getRoot();
    }

    private void setUpTheViewPager(RedditPost post) {
        DetailViewPager detailViewPager = new DetailViewPager(getChildFragmentManager(), post);
        layoutBinding.detailsViewPager.setAdapter(detailViewPager);
        layoutBinding.detailsTabsLayout.setupWithViewPager(layoutBinding.detailsViewPager);
    }

    public void setViewPagerPost(RedditPost redditPost) {
        setUpTheViewPager(redditPost);
    }

    public TabLayout getTabLayout() {
        return layoutBinding.detailsTabsLayout;
    }
}
