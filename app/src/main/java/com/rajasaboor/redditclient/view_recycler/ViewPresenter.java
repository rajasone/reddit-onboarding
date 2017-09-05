package com.rajasaboor.redditclient.view_recycler;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.adapter.ItemsAdapter;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.retrofit.RetrofitController;
import com.rajasaboor.redditclient.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rajaSaboor on 9/4/2017.
 */

public class ViewPresenter implements ViewPostContract.Presenter, RetrofitController.IOnDownloadComplete, ItemsAdapter.IOnPostTapped {
    private static final String TAG = ViewPresenter.class.getSimpleName();
    private RetrofitController controller = null;
    private UpdateAdapter updateAdaper = null;
    private SharedPreferences preferences = null;

    public ViewPresenter(UpdateAdapter saveDataInSharedPrefs, SharedPreferences preferences) {
        controller = new RetrofitController(this);
        this.updateAdaper = saveDataInSharedPrefs;
        this.preferences = preferences;
    }

    @Override
    public void onPostTappedListener(int position) {

    }

    @Override
    public void onDownloadCompleteListener(int responseCode, List<RedditPostWrapper> postsList) {
        Log.d(TAG, "onDownloadCompleteListener: start");
        Log.d(TAG, "onDownloadCompleteListener: Response code ===> " + responseCode);
        saveDownloadTimeHelper(); // save the current time in shared prefs
        updateAdaper.updateAdapter(postsList); // call back in the fragment and update the recycler view
        removeDataFromCache(preferences); // remove the existing data from the cache
        saveDownloadDataInCache(postsList); // save the new download data in shared prefs
        // Util.printList(postsList); // print the list for debug purpose
        Log.d(TAG, "onDownloadCompleteListener: end");
    }

    @Override
    public void startTheDownloadProcess() {
        controller.start();
    }

    @Override
    public void saveDownloadDataInCache(List<RedditPostWrapper> redditPostWrapper) {
        controller.saveTheDataInSharedPrefs(redditPostWrapper, preferences);
    }

    @Override
    public void removeDataFromCache(SharedPreferences preferences) {
        controller.removeTheCacheData(preferences);
    }

    @Override
    public List<RedditPostWrapper> getCacheData(SharedPreferences preferences) {
        return controller.getCacheDataFromSharedPrefs(preferences);
    }

    @Override
    public void checkTheCacheAndRequestServer(Bundle savedInstanceState, ConnectivityManager manager) {
        Log.d(TAG, "checkTheCacheAndRequestServer: start");
        if (preferences.getString(MainActivity.KEY_TO_CHECK_DATA, null) != null) {
            List<RedditPostWrapper> postWrapperList = getCacheData(preferences);
//            Util.printList(postWrapperList);
            updateAdaper.updateAdapter(postWrapperList);
        } else if ((preferences.getString(MainActivity.KEY_TO_CHECK_DATA, null) == null) && (Util.checkConnection(manager))) {
            controller.start();
        }
        Log.d(TAG, "checkTheCacheAndRequestServer: end");
    }

    private long getTimeDifferenceInMinutes() {
        /*
        Log.e(TAG, "getTimeDifferenceInMinutes: Is Contains ===>" + (preferences.contains(BuildConfig.LAST_DOWNLOAD_TIME_KEY)));
        long timeDifference = (System.currentTimeMillis()) - (preferences.getLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, System.currentTimeMillis()));
        long temp = preferences.getLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, 0L);

        Log.d(TAG, "getTimeDifferenceInMinutes: Time Fetched From preferences ===> " + temp);
        Log.d(TAG, "getTimeDifferenceInMinutes: Current Time ===> " + System.currentTimeMillis());
        Log.d(TAG, "getTimeDifferenceInMinutes: Time diff in milis ===> " + timeDifference);
        Log.d(TAG, "getTimeDifferenceInMinutes: Time diff in minutes ===> " + TimeUnit.MILLISECONDS.toMinutes(timeDifference));
        return TimeUnit.MILLISECONDS.toMinutes(timeDifference);
*/
        long timeDifference = (System.currentTimeMillis()) - (preferences.getLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, System.currentTimeMillis()));
        return TimeUnit.MILLISECONDS.toMinutes(timeDifference);
    }

    @Override
    public int manageTheLastDownloadTime() {
        long timeDiff = getTimeDifferenceInMinutes();
        Log.d(TAG, "manageTheLastDownloadTime: Time Difference ===> " + timeDiff);
        return (int) timeDiff;
    }

    @Override
    public void saveDownloadTimeInSharedPrefs() {
        Log.d(TAG, "saveDownloadTimeInSharedPrefs: start");
        saveDownloadTimeHelper();
        Log.d(TAG, "saveDownloadTimeInSharedPrefs: end");
    }

    private void saveDownloadTimeHelper() {
        long downloadTime = System.currentTimeMillis(); // set the current time field
//        Log.d(TAG, "saveDownloadTimeHelper: Time to save ===> " + downloadTime);
//        Log.d(TAG, "saveDownloadTimeHelper: Time to save current time ===> " + System.currentTimeMillis());
        preferences.edit().putLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, System.currentTimeMillis()).apply();
//        Log.d(TAG, "saveDownloadTimeHelper: Saved Time ===> " + preferences.getLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, 0L));
    }

    /*
    * Callback to update the adapter
     */

    interface UpdateAdapter {
        void updateAdapter(List<RedditPostWrapper> redditPostWrapper);
    }
}
