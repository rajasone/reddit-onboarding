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
    private static final String IS_POST = "is_post";
    private RedditPost post;
    private int postProgress = 0; // maintain the progress bar progress for the loading purpose
    private FragmentPostBinding postBinding = null;
    private boolean isPost;


    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().getParcelable(BuildConfig.KEY_URL_STRING) != null) {
            post = getArguments().getParcelable(BuildConfig.KEY_URL_STRING);
        }

        if (savedInstanceState != null) {
            isPost = savedInstanceState.getBoolean(PostFragment.IS_POST);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        postBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false);


        ((RedditApplication) getActivity().getApplication()).getBus().register(this);

        // If key == 0 Post URI will be loaded otherwise Comments URI will be loaded
        isPost = getArguments().getInt(BuildConfig.POST_TAB_KEY) == 0;
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
            if (isPost) {
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
        outState.putBoolean(PostFragment.IS_POST, isPost);
    }

    @Subscribe
    public void refreshWebView(Integer pos) {
        Log.d(TAG, "refreshWebView: Refresh the web View");

        boolean temp = getUserVisibleHint();
        Log.d(TAG, "refreshWebView: Temp ----> " + temp);

        if (pos == 0) {
            Log.d(TAG, "refreshWebView: Post view is open");
            Log.d(TAG, "refreshWebView: Url to load ====> " + post.getPostURL());
            postBinding.postWebview.loadUrl(post.getPostURL());
        } else {
            Log.d(TAG, "refreshWebView: Comments view is open");
            Log.d(TAG, "refreshWebView: Url to load ----> " + BuildConfig.BASE_URI + post.getCommentsLink());
            postBinding.postWebview.loadUrl(BuildConfig.BASE_URI + post.getCommentsLink());
        }
    }

    @Subscribe
    public void pageSelected(DetailActivityFragment.PageSelected pageSelected) {
        Log.d(TAG, "pageSelected: start");
        Log.d(TAG, "pageSelected: Selected page ----> " + pageSelected.getSelectedPage());

        if (pageSelected.getSelectedPage() == 0) {
            if (!postBinding.postWebview.getUrl().equals(post.getPostURL())) {
                postBinding.postWebview.loadUrl(post.getPostURL());
            }
        } else {
            if (!postBinding.postWebview.getUrl().equals(BuildConfig.BASE_URI + post.getCommentsLink())) {
                postBinding.postWebview.loadUrl(BuildConfig.BASE_URI + post.getCommentsLink());
            }
        }
        Log.d(TAG, "pageSelected: end");
    }
}
