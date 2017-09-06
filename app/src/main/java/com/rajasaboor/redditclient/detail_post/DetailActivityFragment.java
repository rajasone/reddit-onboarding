package com.rajasaboor.redditclient.detail_post;

import android.content.Intent;
import android.content.res.Resources;
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
import com.rajasaboor.redditclient.appbar_layout.DetailViewPager;
import com.rajasaboor.redditclient.databinding.ActivityDetailBinding;
import com.rajasaboor.redditclient.databinding.DetailFragmentLayoutBinding;
import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by rajaSaboor on 9/5/2017.
 */

public class DetailActivityFragment extends Fragment implements DetailPostContract.View {
    private static final String TAG = DetailActivityFragment.class.getSimpleName();
    private DetailFragmentLayoutBinding detailFragmentLayoutBinding = null;
    private DetailPostContract.Presenter presenter = null;

    public static DetailActivityFragment newInstance() {
        return new DetailActivityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        detailFragmentLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment_layout, container, false);
        setHasOptionsMenu(true);

        setUpViewPager();
        Log.d(TAG, "onCreateView: end");
        return detailFragmentLayoutBinding.getRoot();
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
                sharePost();
                break;
        }
        Log.d(TAG, "onOptionsItemSelected: end");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUpViewPager() {
        DetailViewPager detailViewPager = new DetailViewPager(getFragmentManager(), ((DetailPresenter) presenter).getPost());
        detailFragmentLayoutBinding.detailsViewPager.setAdapter(detailViewPager);
        detailFragmentLayoutBinding.detailsTabsLayout.setupWithViewPager(detailFragmentLayoutBinding.detailsViewPager);
    }

    @Override
    public void sharePost() {
        String urlToShare = detailFragmentLayoutBinding.detailsTabsLayout.getSelectedTabPosition() == 0 ? ((DetailPresenter) presenter).getPost().getPostURL() : BuildConfig.BASE_URI + ((DetailPresenter) presenter).getPost().getCommentsLink();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getContext().getString(R.string.share_message), urlToShare));
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    public void setPresenter(DetailPostContract.Presenter presenter) {
        this.presenter = presenter;
    }
}