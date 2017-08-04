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
import com.rajasaboor.redditclient.util.Consts;

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


    public static PostFragment newInstance(RedditPost post) {
        Log.d(TAG, "newInstance: start");
        Log.d(TAG, "newInstance: Url of post ---> " + post.getPostURL());

        Bundle args = new Bundle();
        args.putSerializable(KEY_URL_STRING, post);
        PostFragment fragment = new PostFragment();
        fragment.setArguments(args);

        Log.d(TAG, "newInstance: Args check URL ---> " + ((RedditPost) fragment.getArguments().getSerializable(KEY_URL_STRING)).getPostURL());
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


        if (getArguments().getSerializable(KEY_URL_STRING) != null) {
            Log.d(TAG, "onCreate: Bundle have data");
            post = (RedditPost) getArguments().getSerializable(KEY_URL_STRING);
            Log.d(TAG, "onCreate: Port URL is ---> " + ((RedditPost) getArguments().getSerializable(KEY_URL_STRING)).getPostURL());
        } else {
            Log.d(TAG, "onCreate: Bundle is empty");
        }
        Log.d(TAG, "onCreate: end");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        iniViews(view);

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreateView: Bundle have data ---> " + savedInstanceState.getInt(Consts.POST_LOADING_STATUS));
            postWebView.restoreState(savedInstanceState);
            postProgress = savedInstanceState.getInt(Consts.POST_LOADING_STATUS);
        } else {
            Log.d(TAG, "onCreateView: Bundle is empty");
            postWebView.loadUrl(post.getPostURL());
        }
        postProgressBar.setProgress(postProgress);
        Log.d(TAG, "onCreateView: Progress ---> " + postWebView.getProgress());
        Log.d(TAG, "onCreateView: Bundle have data ---> " + postProgress);


        if (postProgress < 100) {
            progressChecker();
        }
        Log.d(TAG, "onCreateView: end");
        return view;
    }

    private void iniViews(View view) {
        postWebView = view.findViewById(R.id.post_webview);
        postProgressBar = view.findViewById(R.id.post_progress_bar);
        postWebView.getSettings().setJavaScriptEnabled(true);
        postWebView.setWebViewClient(new WebViewClient());
    }

    private void progressChecker() {
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "onCreateView: Progress ---> " + postWebView.getProgress());
                postProgress = postWebView.getProgress();
                if (postProgress == 100) {
                    Log.d(TAG, "run: now off the thread");
                    postProgressBar.setProgress(postProgress);
                } else {
                    postProgressBar.setProgress(postProgress);
                    ha.postDelayed(this, 10);
                }

            }
        }, 10);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: start");
        super.onSaveInstanceState(outState);
        outState.putInt(Consts.POST_LOADING_STATUS, postProgress);
        postWebView.saveState(outState);

        Log.d(TAG, "onSaveInstanceState: Progress from bundle ---> " + outState.getInt(Consts.POST_LOADING_STATUS));
        Log.d(TAG, "onSaveInstanceState: end");
    }

}
