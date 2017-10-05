package com.rajasaboor.redditclient.view_recycler;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
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


    ViewPresenter(SharedPreferences preferences, ViewPostContract.FragmentView fragmentView, ViewPostContract.ActivityView activityView) {
        this.preferences = preferences;
        this.fragmentView = fragmentView;
        this.activityView = activityView;
        controller = new RetrofitController(this, preferences);
    }


    @Override
    public void onDownloadCompleteListener(int responseCode, List<RedditPostWrapper> postsList) {
        if (responseCode == BuildConfig.OK_RESPONSE_CODE) {
            saveDownloadTimeInSharedPrefs(); // save the current time in shared prefs
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

    @Override
    public void checkTheCacheAndRequestServer(ConnectivityManager manager) {
        if (preferences.getString(BuildConfig.KEY_TO_CHECK_DATA, null) != null) {
            fragmentView.hideNoOfflineDataAvailableTextView(true);
            postWrapperList = getCacheData();
            fragmentView.updatePostAdapter(postWrapperList);
            updateToolbarSubtitle();
        } else if ((preferences.getString(BuildConfig.KEY_TO_CHECK_DATA, null) == null) && (Util.checkConnection(manager))) {
            controller.start();
        }
    }

    @Override
    public long getTimeDifferenceInMinutes() {
        long timeDifference = (System.currentTimeMillis()) - (preferences.getLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, System.currentTimeMillis()));
        return TimeUnit.MILLISECONDS.toMinutes(timeDifference);
    }

    @Override
    public void hideNoPostSelectedTextView(boolean hide) {
        activityView.hideNoPostSelectedTextView(hide);
    }

    @Override
    public void updateToolbarSubtitle() {
        activityView.updateLastDownloadMessageInToolbar((int) getTimeDifferenceInMinutes());
    }

    @Override
    public void requestServer(ConnectivityManager manager) {
        if (Util.checkConnection(manager)) {
            startDownloadProcess();
        } else {
            fragmentView.showToast(fragmentView.getMessageFromStringRes(R.string.no_internet_connection));
        }
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
