package com.rajasaboor.redditclient.detail_post;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.RedditApplication;
import com.rajasaboor.redditclient.databinding.FragmentPostBinding;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.util.Util;
import com.squareup.otto.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    private static final String TAG = PostFragment.class.getSimpleName();
    private static final String ACTION_TYPE = "type";
    private RedditPost post;
    private int postProgress = 0; // maintain the progress bar progress for the loading purpose
    private FragmentPostBinding postBinding = null;
    private boolean showContent;

    public static PostFragment newInstance(RedditPost post, boolean showContent) {
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY, post);
        bundle.putBoolean(ACTION_TYPE, showContent);
        postFragment.setArguments(bundle);
        return postFragment;
    }

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY) != null) {
            post = getArguments().getParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY);
            showContent = getArguments().getBoolean(ACTION_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        postBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false);
        ((RedditApplication) getActivity().getApplication()).getBus().register(this);
        initWebView();
        /*
        * Check whether the bundle is NULL or not
        * if bundle is NOT NULL just restore the web view and get the value of the progress bar from the bundle and set the progress bar value to the fetched value
        * If bundle is NULL load the url in web view
         */
        if (savedInstanceState != null) {
            postBinding.postWebview.restoreState(savedInstanceState);
            postProgress = postBinding.postWebview.getProgress() == 10 ? 100 : postBinding.postWebview.getProgress();
        } else {
            if (showContent) {
                postBinding.postWebview.loadUrl(post.getPostURL());
            } else {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((RedditApplication) getActivity().getApplication()).getBus().unregister(this);
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
        super.onSaveInstanceState(outState);
        postBinding.postWebview.saveState(outState);
    }

    @Subscribe
    public void refreshWebView(PostFragment postFragment) {
        Log.d(TAG, "refreshWebView: Refresh the web View");
        if (showContent) {
            Log.d(TAG, "refreshWebView: Show the POST URL");
            postBinding.postWebview.loadUrl(post.getPostURL());
        } else {
            Log.d(TAG, "refreshWebView: show the COMMENT URL");
            postBinding.postWebview.loadUrl(BuildConfig.BASE_URI + post.getCommentsLink());
        }
    }
}
