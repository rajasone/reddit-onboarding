package com.rajasaboor.redditclient.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.List;

/**
 * Created by default on 8/3/2017.
 * This class provides some static helper methods to other classes e.g displaying a list etc
 */

public class Util {
    private static final String TAG = Util.class.getSimpleName(); // Tag name for the Debug purposes

    public static void printList(List<RedditPostWrapper> postWrappers) {
        Log.d(TAG, "printList: start");
        for (RedditPostWrapper wrapper : postWrappers) {
            Log.d(TAG, "printList: Title ---> " + wrapper.getData().getPostTitle());
            Log.d(TAG, "printList: Author ---> " + wrapper.getData().getPostAuthor());
            Log.d(TAG, "printList: Comments Link ---> " + Uri.parse(BuildConfig.BASE_URI).buildUpon().path(wrapper.getData().getCommentsLink()).build().toString());
            Log.d(TAG, "printList: Post URL ---> " + wrapper.getData().getPostURL());
            Log.d(TAG, "printList: Is PostIsSelf ---> " + wrapper.getData().isPostIsSelf());
            Log.d(TAG, "printList: Comments ---> " + wrapper.getData().getPostComments());
            Log.d(TAG, "printList: Is Video ---> " + wrapper.getData().isVideo());
            Log.d(TAG, "printList: Number of Ups ---> " + wrapper.getData().getNumberOfUps());
            Log.d(TAG, "printList: ************************************");
        }

        Log.d(TAG, "printList: end");
    }
     /*
    * This method check the progress of the web view and updating the progress bar
     */

    public static void downloadProgressCheck(final ProgressBar postProgressBar, final WebView postWebView) {
        final Handler ha = new Handler();
        postProgressBar.setVisibility(View.VISIBLE);
        ha.postDelayed(new Runnable() {
            int postProgress = 0;

            @Override
            public void run() {
                postProgress = postWebView.getProgress();
                if (postProgress == 100) {
                    Log.d(TAG, "run: now off the thread");
                    postProgressBar.setProgress(postProgress);
                    postProgressBar.setVisibility(View.GONE);
                } else {
                    postProgressBar.setProgress(postProgress);
                    ha.postDelayed(this, 500);
                }

            }
        }, 500);
    }


    /*
    * This method return TRUE if there is a connection with the internet
     */

    public static boolean checkConnection(ConnectivityManager manager) {
        boolean result = false;
        try {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if (networkInfo != null)
                result = networkInfo.isConnected();
        } catch (Exception e) {
            Log.e(TAG, "checkConnection: An error happen ===> " + e.getMessage());
            e.printStackTrace();
        }

        Log.d(TAG, "checkConnection: Result ===> " + result);
        return result;
    }


}
