package com.rajasaboor.redditclient.fragments;


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

import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    private static final String KEY_URL_STRING = "URL_STRING";
    private static final String TAG = PostFragment.class.getSimpleName();
    private RedditPost post;
    private WebView postWebView; // webview which contains the post webview
    private ProgressBar postProgressBar; // an horizontal progress bar which show the progress of page to user
    private int postProgress = 0; // maintain the progress bar progress for the loading purpose
    public static final String POST_LOADING_STATUS = "post_loading";

    /*
    * This static method is called from the {@link com.rajasaboor.redditclient.appbar_layout.DetailViewPager} getItem method and Reddit object is passed as an parameter
     */

    public static PostFragment newInstance(RedditPost post) {
        Log.d(TAG, "newInstance: start");
        Log.d(TAG, "newInstance: Url of post ---> " + post.getPostURL());

        Bundle args = new Bundle();
        args.putParcelable(KEY_URL_STRING, post);
        PostFragment fragment = new PostFragment();
        fragment.setArguments(args);

        Log.d(TAG, "newInstance: Args check URL ---> " + ((RedditPost) fragment.getArguments().getParcelable(KEY_URL_STRING)).getPostURL());
        Log.d(TAG, "newInstance: end");
        return fragment;
    }

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);


        if (getArguments().getParcelable(KEY_URL_STRING) != null) {
            Log.d(TAG, "onCreate: Bundle have data");
            post = (RedditPost) getArguments().getParcelable(KEY_URL_STRING);
            Log.d(TAG, "onCreate: Port URL is ---> " + ((RedditPost) getArguments().getParcelable(KEY_URL_STRING)).getPostURL());
        } else {
            Log.e(TAG, "onCreate: Bundle is empty");
        }
        Log.d(TAG, "onCreate: end");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        initViews(view);

        /*
        * Check whether the bundle is NULL or not
        * if bundle is NOT NULL just restore the web view and get the value of the progress bar from the bundle and set the progress bar value to the fetched value
        * If bundle is NULL load the url in web view
         */
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreateView: Bundle have data ---> " + savedInstanceState.getInt(PostFragment.POST_LOADING_STATUS));
            postWebView.restoreState(savedInstanceState);
            postProgress = savedInstanceState.getInt(PostFragment.POST_LOADING_STATUS);
        } else {
            Log.d(TAG, "onCreateView: Bundle is empty");
            postWebView.loadUrl(post.getPostURL());
        }
        postProgressBar.setProgress(postProgress);
        Log.d(TAG, "onCreateView: Progress ---> " + postWebView.getProgress());
        Log.d(TAG, "onCreateView: Bundle have data ---> " + postProgress);


        /*
        * If progress value is less than 100 only then call the progress checker method
         */
        if (postProgress < 100) {
//            progressChecker();
            Util.downloadProgressCheck(postProgressBar, postWebView);
        } else {
            postProgressBar.setVisibility(View.GONE);
        }
        Log.d(TAG, "onCreateView: end");
        return view;
    }

    /*
    * Get the references of the XML views
    * Configure the web view
     */

    private void initViews(View view) {
        postWebView = view.findViewById(R.id.post_webview);
        postProgressBar = view.findViewById(R.id.post_progress_bar);
        postWebView.getSettings().setJavaScriptEnabled(true);
        postWebView.setWebViewClient(new WebViewClient());
    }

    /*
    * Saving the state of the web view in this method
    * Saving the value of progress in this method
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: start");
        super.onSaveInstanceState(outState);
        outState.putInt(PostFragment.POST_LOADING_STATUS, postProgress);
        postWebView.saveState(outState);

        Log.d(TAG, "onSaveInstanceState: Progress from bundle ---> " + outState.getInt(PostFragment.POST_LOADING_STATUS));
        Log.d(TAG, "onSaveInstanceState: end");
    }

}
