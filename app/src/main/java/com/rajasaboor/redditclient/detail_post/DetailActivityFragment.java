package com.rajasaboor.redditclient.detail_post;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.RedditApplication;
import com.rajasaboor.redditclient.appbar_layout.DetailViewPager;
import com.rajasaboor.redditclient.databinding.DetailFragmentLayoutBinding;

/**
 * Created by rajaSaboor on 9/5/2017.
 */

public class DetailActivityFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = DetailActivityFragment.class.getSimpleName();
    private DetailFragmentLayoutBinding detailFragmentLayoutBinding = null;
    private DetailPostContract.Presenter presenter;
    private DetailViewPager detailViewPager;

    public static DetailActivityFragment newInstance() {
        return new DetailActivityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        detailFragmentLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment_layout, container, false);
        setHasOptionsMenu(true);
        setUpViewPager();
        detailFragmentLayoutBinding.detailsViewPager.addOnPageChangeListener(this);
        return detailFragmentLayoutBinding.getRoot();
    }

    private void setUpViewPager() {
        detailViewPager = new DetailViewPager(getFragmentManager(), presenter.getPost());
        detailFragmentLayoutBinding.detailsViewPager.setAdapter(detailViewPager);
        detailFragmentLayoutBinding.detailsTabsLayout.setupWithViewPager(detailFragmentLayoutBinding.detailsViewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share_menu, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_menu:
                sharePost();
                break;
            case R.id.refresh_menu:
                Log.d(TAG, "onOptionsItemSelected: Refresh Tapped");
                ((RedditApplication) getActivity().getApplication()).getBus().post(detailFragmentLayoutBinding.detailsViewPager.getCurrentItem());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sharePost() {
        String urlToShare = detailFragmentLayoutBinding.detailsTabsLayout.getSelectedTabPosition() == 0 ? presenter.getPost().getPostURL() :
                BuildConfig.BASE_URI + presenter.getPost().getCommentsLink();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getContext().getString(R.string.share_message), urlToShare));
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    public void setPresenter(DetailPostContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((RedditApplication) getActivity().getApplication()).getBus().post(new PageSelected(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class PageSelected {
        private int position;

        PageSelected(int position) {
            this.position = position;
        }

        public Integer getSelectedPage() {
            return this.position;
        }
    }
}
