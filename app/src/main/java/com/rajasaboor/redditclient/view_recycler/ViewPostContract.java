package com.rajasaboor.redditclient.view_recycler;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.rajasaboor.redditclient.databinding.MainFragmentBinding;
import com.rajasaboor.redditclient.fragments.DetailsFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.List;

/**
 * Created by rajaSaboor on 9/4/2017.
 */

interface ViewPostContract {
    interface View {
        void updateTheActionBarSubtitles(int downloadTime);

        void showTheRefreshIcon(boolean hide);

        void hideTheNoOfflineDataAvailableTextView(boolean hide);

        void showToast(String message);

        void showProgressBar(boolean show);

        boolean isTabletActive();

        void hideTheDetailFragment(boolean hide);

        DetailsFragment getDetailFragmentReferenceInTablet();

        void sharePost();

        void hideTheNoPostSelected(boolean hide);
    }

    interface Presenter {
        void startTheDownloadProcess();

        void saveDownloadDataInCache(List<RedditPostWrapper> redditPostWrapper);

        void removeDataFromCache(SharedPreferences sharedPreferences);

        List<RedditPostWrapper> getCacheData(SharedPreferences preferences);

        void checkTheCacheAndRequestServer(ConnectivityManager manager);

        int manageTheLastDownloadTime();

        void saveDownloadTimeInSharedPrefs();

    }
}
