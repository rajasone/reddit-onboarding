package com.rajasaboor.redditclient.view_recycler;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.detail_post.DetailActivity;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.adapter.ItemsAdapter;
import com.rajasaboor.redditclient.databinding.ActivityMainBinding;
import com.rajasaboor.redditclient.fragments.DetailsFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ItemsAdapter.IOnPostTapped {
    private static final String TAG = MainActivity.class.getSimpleName(); // Tag name for the Debug purposes
    private List<RedditPostWrapper> postWrapperList = new ArrayList<>();
    private RedditPost selectedPost; // A post which is selected in the landscape layout
    private DetailsFragment detailsFragmentInTablet;  // instance of detail fragment to hide or show the toolbar and set view pager adapter and used to know the current mode either Tablet/Phone

    // Some Global constants which are used by the MainActivity ONLY
    public static final String KEY_TO_CHECK_DATA = "1";
    public static String CURRENT_SELECTED_OBJECT = "current_object";

    private ActivityMainBinding mainBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // adding the fragment to the ActivityMain
        ViewPostFragment viewPostFragment = ViewPostFragment.newInstance();
        viewPostFragment.setViewPresenter(new ViewPresenter(viewPostFragment, getSharedPreferences(BuildConfig.SHARED_PREFS_NAME, MODE_PRIVATE)));
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, viewPostFragment).commit();

        Log.d(TAG, "onCreate: end");
    }

    private void initViews() {
        detailsFragmentInTablet = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: start");
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: end");
    }


    /*
    * Check the details fragment if it is not null and it is visible set the tab bar is hide by default
     */
    private void hideTheTabsInLandscapeLayout() {
        if (detailsFragmentInTablet != null) {
            detailsFragmentInTablet.hideTheToolbar(true);
        }
    }

    private boolean isTableLayoutIsActive() {
        return (detailsFragmentInTablet != null);
    }

    /*
    * If user selected post is available in shared prefs and orientation is landscape
    * set the current post to the shared prefs post
    * then call the callTheDetailViewPagerMethod() which sets up the view pager and hide/show the tabs
     */
    private void actionsPerformIfPostIsSelectedInLandscape() {
        if (getCurrentPostFromSharedPrefs() != null && isTableLayoutIsActive()) {
            setSelectedPost(getCurrentPostFromSharedPrefs());
            Log.d(TAG, "actionsPerformIfPostIsSelectedInLandscape: Yes post is selected");
            callTheDetailViewPagerMethod();
        } else {
            Log.d(TAG, "actionsPerformIfPostIsSelectedInLandscape: NO post is selected");
        }
    }

    public void setSelectedPost(RedditPost selectedPost) {
        this.selectedPost = selectedPost;
    }


     /*
    * Set up the current post and hide the toolbar if selected post is is self otherwise show the toolbar
     */

    private void callTheDetailViewPagerMethod() {
        detailsFragmentInTablet.setPost(getSelectedPost());
        if (detailsFragmentInTablet.getPost().isPostIsSelf()) {
            detailsFragmentInTablet.hideTheToolbar(true);
        } else {
            detailsFragmentInTablet.hideTheToolbar(false);
        }
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: start");
        super.onResume();
        Log.d(TAG, "onResume: end");
    }


    private void showNoOfflineDataTextView(boolean show) {
        mainBinding.noOfflineDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    public void setPostWrapperList(List<RedditPostWrapper> postWrapperList) {
        Log.d(TAG, "setPostWrapperList: start");
        this.postWrapperList = postWrapperList;
        Log.d(TAG, "setPostWrapperList: end");

    }

    public List<RedditPostWrapper> getPostWrapperList() {
        return postWrapperList;
    }

    @Override
    public void onPostTappedListener(int position) {
        Log.d(TAG, "onPostTappedListener: start");
        Log.d(TAG, "onPostTappedListener: position ---> " + position);

        if (!isTableLayoutIsActive()) {
            Log.d(TAG, "onPostTappedListener: Inside the portrait mode so open a DetailActivity");
            Intent detailActivityIntent = new Intent(this, DetailActivity.class);
            detailActivityIntent.putExtra(BuildConfig.INDIVIDUAL_POST_ITEM_KEY, getPostWrapperList().get(position).getData());
            startActivity(detailActivityIntent);
        } else {
            Log.d(TAG, "onPostTappedListener: !!!!!!!!!!!!!!!!!!Tablet layout is ACTIVE!!!!!!!!!!!!!!!!!!");
            setSelectedPost(getPostWrapperList().get(position).getData());
            saveTheCurrentPostInSharedPrefs();
            Log.d(TAG, "onPostTappedListener: Inside the Landscape mode so display the result in DetailsFragment");
            callTheDetailViewPagerMethod();
        }
        Log.d(TAG, "onPostTappedListener: end");
    }


    public RedditPost getSelectedPost() {
        return selectedPost;
    }


    /*
    * Save the current post in shared prefs
    * Current post means post which is selected by the user in the LANDSCAPE
     */
    private void saveTheCurrentPostInSharedPrefs() {
        if (getSelectedPost() != null) {
            getSharedPreferences(MainActivity.CURRENT_SELECTED_OBJECT, MODE_PRIVATE).edit().putString(MainActivity.CURRENT_SELECTED_OBJECT, new Gson().toJson(getSelectedPost())).apply();
        } else {
            Log.e(TAG, "saveTheCurrentPostInSharedPrefs: Post is NULL");
        }
    }

    /*
    * Get the post which is selected by the user in landscape mode from the shared prefs IF ANY EXISTS
     */
    private RedditPost getCurrentPostFromSharedPrefs() {
        return new Gson().fromJson(getSharedPreferences(MainActivity.CURRENT_SELECTED_OBJECT, MODE_PRIVATE).getString(MainActivity.CURRENT_SELECTED_OBJECT, null), RedditPost.class);
    }
}
