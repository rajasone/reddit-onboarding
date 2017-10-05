package com.rajasaboor.redditclient.view_recycler;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.rajasaboor.redditclient.databinding.MainFragmentBinding;
import com.rajasaboor.redditclient.fragments.DetailsFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.List;

/**
 * Created by rajaSaboor on 9/4/2017.
 */

interface ViewPostContract {
    interface ActivityView {
        void hideNoPostSelectedTextView(boolean hide);

        void updateLastDownloadMessageInToolbar(int minutes);

        void showServerRequestProgressBar(boolean show);
    }

    interface FragmentView {
        boolean isTabletActive();

        DetailsFragment getDetailFragmentReferenceInTablet();

        void sharePost();

        void showErrorDialogWhileServerRequest();

        String getMessageFromStringRes(@StringRes int resID);

        void updatePostAdapter(List<RedditPostWrapper> wrapperList);

        void showRefreshIcon(boolean hide);

        void showToast(String message);

        void hideNoOfflineDataAvailableTextView(boolean hide);
    }

    interface Presenter {
        void startDownloadProcess();

        List<RedditPostWrapper> getCacheData();

        List<RedditPostWrapper> getPostWrapperList();

        void checkTheCacheAndRequestServer(ConnectivityManager manager);

        void saveDownloadTimeInSharedPrefs();

        RedditPost getSelectedPost();

        void setSelectedPost(RedditPost selectedPost);

        long getTimeDifferenceInMinutes();

        void hideNoPostSelectedTextView(boolean hide);

        void updateToolbarSubtitle();

        void requestServer(ConnectivityManager manager);
    }
}
