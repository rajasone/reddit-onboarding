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
public class CommentsFragment extends Fragment {
    private static final String TAG = CommentsFragment.class.getSimpleName();
    private RedditPost post = null; // contains the Reddit object which is passed by the MainActivity to the DetailActivity
    private WebView commentsWebView; // WebView is responsible to load the URL
    private ProgressBar commentsProgressBar; // progress bar which shows a user that how much the page is load
    private int commentsProgress; // store the progress value of the page

    /*
    * This static method is called from the {@link com.rajasaboor.redditclient.appbar_layout.DetailViewPager} getItem method and Reddit object is passed as an parameter
     */

    public static CommentsFragment newInstance(RedditPost post) {
        Bundle args = new Bundle();
        CommentsFragment fragment = new CommentsFragment();
        args.putSerializable(Consts.COMMENTS_KEY, post);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        this.post = (RedditPost) getArguments().getSerializable(Consts.COMMENTS_KEY);

        if (post != null) {
            Log.d(TAG, "onCreate: comments bundle have data");
            Log.d(TAG, "onCreate: Comments URL ===> " + post.getCommentsLink());
        } else {
            Log.e(TAG, "onCreate: comments bundle is empty");
        }
        Log.d(TAG, "onCreate: end");
    }

     /*
        * Check whether the bundle is NULL or not
        * if bundle is NOT NULL just restore the web view and get the value of the progress bar from the bundle and set the progress bar value to the fetched value
        * If bundle is NULL load the url in web view
         */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        iniViews(view);


        if (savedInstanceState != null) {
            Log.d(TAG, "onCreateView: Restoring the state of the bundle");
            commentsWebView.restoreState(savedInstanceState); // if bundle is not null then restore the state of the webview
            commentsProgress = savedInstanceState.getInt(Consts.COMMENTS_PAGE_PROGRESS);
            commentsProgressBar.setProgress(commentsProgress);
            Log.d(TAG, "onCreateView: Comments progress fetch from the bundle is ===> " + commentsProgress);
        } else {
            Log.d(TAG, "onCreateView: Loading the URL in webview");
            commentsWebView.loadUrl(Consts.BASE_URI + post.getCommentsLink()); // if bundle is NULL load the URL
        }


        /*
        * If progress value is less than 100 only then call the progress checker method
         */

        if (commentsProgress < 100) {
            Log.d(TAG, "onCreateView: Progress is < 100 ===> " + commentsProgress);
            progressChecker();
        } else {
            commentsProgressBar.setVisibility(View.GONE);
        }
        Log.d(TAG, "onCreateView: end");

        return view;
    }

    /*
    * Get the references of the XML views
    * Configure the web view
     */

    private void iniViews(View view) {
        commentsWebView = view.findViewById(R.id.comments_webview);
        commentsProgressBar = view.findViewById(R.id.comments_progress_bar);
        commentsWebView.getSettings().setJavaScriptEnabled(true);
        commentsWebView.setWebViewClient(new WebViewClient());
    }

    /*
   * Saving the state of the web view in this method
   * Saving the value of progress in this method
    */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: start");
        super.onSaveInstanceState(outState);
        commentsWebView.saveState(outState);
        outState.putInt(Consts.COMMENTS_PAGE_PROGRESS, commentsProgress);
        Log.d(TAG, "onSaveInstanceState: end");
    }

    /*
    * This method check the progress of the web view and updating the progress bar
     */

    private void progressChecker() {
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "progressChecker: Progress ---> " + commentsWebView.getProgress());
                commentsProgress = commentsWebView.getProgress();
                if (commentsProgress == 100) {
                    Log.d(TAG, "progressChecker: now off the thread");
                    commentsProgressBar.setProgress(commentsProgress);
                    commentsProgressBar.setVisibility(View.GONE);
                } else {
                    commentsProgressBar.setProgress(commentsProgress);
                    ha.postDelayed(this, 10);
                }

            }
        }, 10);
    }


}
