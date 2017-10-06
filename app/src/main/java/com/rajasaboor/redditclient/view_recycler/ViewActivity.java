package com.rajasaboor.redditclient.view_recycler;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.databinding.ActivityMainBinding;
import com.rajasaboor.redditclient.detail_post.DetailsTabletFragment;
import com.rajasaboor.redditclient.model.RedditPost;

public class ViewActivity extends AppCompatActivity implements ViewPostContract.ActivityView {
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

        // If the device is tablet set up the tablet layout and pass it to the presenter
        DetailsTabletFragment detailsTabletFragment = null;
        if (mainBinding.detailFragmentContainer != null) {
            detailsTabletFragment = (DetailsTabletFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
            if (detailsTabletFragment == null) {
                detailsTabletFragment = DetailsTabletFragment.newInstance(null);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_fragment_container, detailsTabletFragment)
                        .commit();
            }
            mainBinding.detailFragmentContainer.setVisibility(View.GONE); // hide the fragment if user tap on post then it will be visible
        }
        viewPostFragment.setViewPresenter(new ViewPresenter(getSharedPreferences(BuildConfig.SHARED_PREFS_NAME, MODE_PRIVATE), viewPostFragment,
                this, detailsTabletFragment, (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)));
    }

    @Override
    public void showServerRequestProgressBar(boolean show) {
        mainBinding.toolbarInclude.menuProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void hideDetailFragment(boolean hide) {
        mainBinding.detailFragmentContainer.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setViewPagerPost(RedditPost redditPost) {
        ((DetailsTabletFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container)).setViewPagerPost(redditPost);
    }

    @Override
    public void sharePost(String message) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.share_message), message));
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    @Override
    public void hideNoPostSelectedTextView(boolean hide) {
        mainBinding.noPostSelectedTextView.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void updateLastDownloadMessageInToolbar(int downloadTime) {
        String temp;

        if (downloadTime == 0) {
            Log.d(TAG, "updateTheActionBarSubtitles: In 0");
            temp = getString(R.string.update_message_less_then_minute);
        } else {
            Log.d(TAG, "updateTheActionBarSubtitles: >=1 ");
            temp = String.format(getString(R.string.update_message_more_than_minute), downloadTime, (downloadTime == 1 ? getString(R.string.minute) :
                    getString(R.string.minutes)));
        }
        getSupportActionBar().setSubtitle(temp);
    }
}
