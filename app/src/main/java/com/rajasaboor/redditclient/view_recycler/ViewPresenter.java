package com.rajasaboor.redditclient.view_recycler;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.detail_post.DetailsTabletFragment;
import com.rajasaboor.redditclient.detail_post.PostFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.retrofit.RetrofitController;
import com.rajasaboor.redditclient.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rajaSaboor on 9/4/2017.
 */

class ViewPresenter implements ViewPostContract.Presenter, RetrofitController.IOnDownloadComplete {
    private static final String TAG = ViewPresenter.class.getSimpleName();
    private RetrofitController controller = null;
    private SharedPreferences preferences = null;
    private List<RedditPostWrapper> postWrapperList = new ArrayList<>();
    private ViewPostContract.FragmentView fragmentView;
    private ViewPostContract.ActivityView activityView;
    private RedditPost selectedPost;
    private DetailsTabletFragment tabletFragment;
    private ConnectivityManager connectivityManager;

    ViewPresenter(SharedPreferences preferences, ViewPostContract.FragmentView fragmentView, ViewPostContract.ActivityView activityView,
                  DetailsTabletFragment tabletFragment, ConnectivityManager connectivityManager) {
        this.preferences = preferences;
        this.fragmentView = fragmentView;
        this.activityView = activityView;
        this.tabletFragment = tabletFragment;
        this.connectivityManager = connectivityManager;
        controller = new RetrofitController(this, preferences);
    }


    @Override
    public void onDownloadCompleteListener(int responseCode, List<RedditPostWrapper> postsList) {
        if (responseCode == BuildConfig.OK_RESPONSE_CODE) {
            saveDownloadTimeInSharedPrefs(); // save the current time in shared prefs
            this.postWrapperList = postsList;
            fragmentView.hideNoOfflineDataAvailableTextView(true);
            activityView.showServerRequestProgressBar(false);
            fragmentView.showRefreshIcon(true);
            fragmentView.updatePostAdapter(postsList);
            controller.saveTheDataInSharedPrefs(postsList);
            updateToolbarSubtitle();
        } else {
            fragmentView.showErrorDialogWhileServerRequest();
        }
        Log.d(TAG, "onDownloadCompleteListener: end");
    }

    @Override
    public void startDownloadProcess() {
        activityView.showServerRequestProgressBar(true);
        fragmentView.showRefreshIcon(false);
        controller.start();
    }


    @Override
    public List<RedditPostWrapper> getCacheData() {
        return controller.getCacheDataFromSharedPrefs();
    }

    private void checkTheCacheAndRequestServer() {
        Log.d(TAG, "checkTheCacheAndRequestServer: start");
        if (preferences.getString(BuildConfig.KEY_TO_CHECK_DATA, null) != null) {
            fragmentView.hideNoOfflineDataAvailableTextView(true);
            postWrapperList = getCacheData();
            fragmentView.updatePostAdapter(postWrapperList);
            updateToolbarSubtitle();
        }
        Log.d(TAG, "checkTheCacheAndRequestServer: end");
    }

    @Override
    public long getTimeDifferenceInMinutes() {
        long timeDifference = (System.currentTimeMillis()) - (preferences.getLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, System.currentTimeMillis()));
        return TimeUnit.MILLISECONDS.toMinutes(timeDifference);
    }

    @Override
    public void updateToolbarSubtitle() {
        activityView.updateLastDownloadMessageInToolbar((int) getTimeDifferenceInMinutes());
    }

    @Override
    public void requestServer() {
        if (Util.checkConnection(connectivityManager)) {
            startDownloadProcess();
        } else {
            fragmentView.showToast(fragmentView.getMessageFromStringRes(R.string.no_internet_connection));
        }
    }

    @Override
    public boolean isTabletLayoutIsActive() {
        return (tabletFragment != null);
    }

    @Override
    public void checkCurrentLayoutAndSetUpViews() {
        if (isTabletLayoutIsActive()) {
            if (getSelectedPost() == null) {
                activityView.hideNoPostSelectedTextView(false); // NO post is selected SHOW the text view no post is selected
            } else {
                activityView.hideNoPostSelectedTextView(true); // post is selected HIDE the text view no post is selected
                activityView.hideDetailFragment(false); // selected post is not NULL show the detail fragment
                activityView.setViewPagerPost(getSelectedPost());
            }
        }
    }

    @Override
    public void sharePost() {
        if (getSelectedPost() == null) {
            fragmentView.showToast(fragmentView.getMessageFromStringRes(R.string.no_post_selected));
        } else {
            tabletFragment.getTabLayout().getSelectedTabPosition();
            String urlToShare = tabletFragment.getTabLayout().getSelectedTabPosition() == 0 ? getSelectedPost().getPostURL() :
                    BuildConfig.BASE_URI + getSelectedPost().getCommentsLink();
            activityView.sharePost(urlToShare);
        }
    }

    @Override
    public void loadCacheOrRequestServer() {
        Log.d(TAG, "loadCacheOrRequestServer: start");
        checkTheCacheAndRequestServer();
        if (getTimeDifferenceInMinutes() >= 5) {
            if (Util.checkConnection(connectivityManager)) {
                requestServer();
                return;
            }
        }
        Log.d(TAG, "loadCacheOrRequestServer: Getting the cache data");
    }

    @Override
    public void handleRefreshIcon() {
        if (isTabletLayoutIsActive()) {
            Log.d(TAG, "handleRefreshIcon: Tablet layout is loaded");
            if (getSelectedPost() != null) {
                requestServer();
                activityView.getBusInstance().post(new PostFragment());
            }
        }
        requestServer();
    }

    @Override
    public void saveDownloadTimeInSharedPrefs() {
        preferences.edit().putLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, System.currentTimeMillis()).apply();
    }

    @Override
    public List<RedditPostWrapper> getPostWrapperList() {
        return postWrapperList;
    }

    @Override
    public RedditPost getSelectedPost() {
        return selectedPost;
    }

    @Override
    public void setSelectedPost(RedditPost selectedPost) {
        this.selectedPost = selectedPost;
    }

}
