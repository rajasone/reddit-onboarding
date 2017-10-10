package com.rajasaboor.redditclient.view_recycler;

import android.support.annotation.StringRes;

import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.squareup.otto.Bus;

import java.util.List;

/**
 * Created by rajaSaboor on 9/4/2017.
 */

interface ViewPostContract {
    interface ActivityView {
        void hideNoPostSelectedTextView(boolean hide);

        void updateLastDownloadMessageInToolbar(int minutes);

        void showServerRequestProgressBar(boolean show);

        void hideDetailFragment(boolean hide);

        void setViewPagerPost(RedditPost redditPost);

        void sharePost(String message);

        Bus getBusInstance();
    }

    interface FragmentView {
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

        void saveDownloadTimeInSharedPrefs();

        RedditPost getSelectedPost();

        void setSelectedPost(RedditPost selectedPost);

        long getTimeDifferenceInMinutes();

        void updateToolbarSubtitle();

        void requestServer();

        boolean isTabletLayoutIsActive();

        void checkCurrentLayoutAndSetUpViews();

        void sharePost();

        void loadCacheOrRequestServer();

        void handleRefreshIcon();
    }
}
