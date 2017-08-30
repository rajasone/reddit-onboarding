package com.rajasaboor.redditclient.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.databinding.FragmentPostBinding;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    private static final String TAG = PostFragment.class.getSimpleName();
    private RedditPost post;
    private int postProgress = 0; // maintain the progress bar progress for the loading purpose
    private FragmentPostBinding postBinding = null;


    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);

        if (getArguments().getParcelable(BuildConfig.KEY_URL_STRING) != null) {
            Log.d(TAG, "onCreate: Bundle have data");
            post = getArguments().getParcelable(BuildConfig.KEY_URL_STRING);
            Log.d(TAG, "onCreate: Port URL is ---> " + ((RedditPost) getArguments().getParcelable(BuildConfig.KEY_URL_STRING)).getPostURL());
            Log.d(TAG, "onCreate: Flag ===> " + getArguments().getInt(BuildConfig.POST_TAB_KEY));
        } else {
            Log.e(TAG, "onCreate: Bundle is empty");
        }
        Log.d(TAG, "onCreate: end");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        postBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false);


        // If key == 0 Post URI will be loaded otherwise Comments URI will be loaded
        boolean isPost = getArguments().getInt(BuildConfig.POST_TAB_KEY) == 0;
        initWebView();
        /*
        * Check whether the bundle is NULL or not
        * if bundle is NOT NULL just restore the web view and get the value of the progress bar from the bundle and set the progress bar value to the fetched value
        * If bundle is NULL load the url in web view
         */
        if (savedInstanceState != null) {
            postBinding.postWebview.restoreState(savedInstanceState);
            Log.d(TAG, "onCreateView: Fetched Progress ===> " + postBinding.postWebview.getProgress());
            postProgress = postBinding.postWebview.getProgress() == 10 ? 100 : postBinding.postWebview.getProgress();

        } else {
            Log.d(TAG, "onCreateView: Bundle is empty");
            Log.d(TAG, "onCreateView: Flag ===> " + getArguments().getInt(BuildConfig.POST_TAB_KEY));

            if (isPost) {
                Log.d(TAG, "onCreateView: Loading the POST");
                postBinding.postWebview.loadUrl(post.getPostURL());
            } else {
                Log.d(TAG, "onCreateView: Loading the Comments");
                postBinding.postWebview.loadUrl(BuildConfig.BASE_URI + post.getCommentsLink());
            }
        }
        postBinding.postProgressBar.setProgress(postProgress);

        /*
        * If progress value is less than 100 only then call the progress checker method
         */
        if (postProgress < 100) {
            Util.downloadProgressCheck(postBinding.postProgressBar, postBinding.postWebview);
        } else {
            postBinding.postProgressBar.setVisibility(View.GONE);
        }
        Log.d(TAG, "onCreateView: end");
        return postBinding.getRoot();
    }

    private void initWebView() {
        postBinding.postWebview.getSettings().setJavaScriptEnabled(true);
        postBinding.postWebview.setWebViewClient(new WebViewClient());
    }

    /*
    * Saving the state of the web view in this method
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: start");
        super.onSaveInstanceState(outState);
        postBinding.postWebview.saveState(outState);
        Log.d(TAG, "onSaveInstanceState: end");
    }


}
