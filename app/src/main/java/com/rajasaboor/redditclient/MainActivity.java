package com.rajasaboor.redditclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rajasaboor.redditclient.adapter.ItemsAdapter;
import com.rajasaboor.redditclient.databinding.ActivityMainBinding;
import com.rajasaboor.redditclient.fragments.DetailsFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.retrofit.RetrofitController;
import com.rajasaboor.redditclient.util.Util;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RetrofitController.IOnDownloadComplete, ItemsAdapter.IOnPostTapped, RetrofitController.IPublishLastDownloadTimeInToolbar, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName(); // Tag name for the Debug purposes
    private List<RedditPostWrapper> postWrapperList = new ArrayList<>();
    private ItemsAdapter itemsAdapter = null;
    private Menu menu = null; // this will hold the reference of the menu and will be used to hide or display the refresh menu icon
    private RedditPost selectedPost; // A post which is selected in the landscape layout
    private DetailsFragment detailsFragmentInTablet;  // instance of detail fragment to hide or show the toolbar and set view pager adapter and used to know the current mode either Tablet/Phone
    private RetrofitController controller = null;

    // Some Global constants which are used by the MainActivity ONLY
    private static final String KEY_TO_CHECK_DATA = "1";
    private static String CURRENT_SELECTED_OBJECT = "current_object";
    private static final int RESPONSE_CODE_OK = 200;

    private ActivityMainBinding mainBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        controller = new RetrofitController(this, this);

        initViews();
        setSupportActionBar(mainBinding.toolbarInclude.customToolbar); // setting up the custom toolbar as the action bar
        setUpRecyclerView();

        SharedPreferences preferences = getSharedPreferences(BuildConfig.SHARED_PREFS_NAME, MODE_PRIVATE);

        /*
        * Check whether the data is inside the shared prefs
        * if data is found dont make the request call to the server
        * if data is not found OR the savedInstanceState is empty then make a request call
         */
        if (preferences.getString(MainActivity.KEY_TO_CHECK_DATA, null) != null || savedInstanceState != null) {
            Log.d(TAG, "onCreate: Data is inside the preferences");
            postWrapperList = controller.getCacheDataFromSharedPrefs(this);
            itemsAdapter.updateAdapter(postWrapperList);
        } else if ((preferences.getString(MainActivity.KEY_TO_CHECK_DATA, null) == null) && (Util.checkConnection(this))) {
               /*
            * Initiate the call to the base URI and VISIBLE the progress bar
            */

            Log.d(TAG, "onCreate: Preference is empty but connection is available");
            fireUpNetworkRequest();
        } else {
            /*
            * No data is in the shared prefs and no internet connection as well
             */
            Log.d(TAG, "onCreate: No data available and no internet connection");
        }
        Log.d(TAG, "onCreate: end");
    }

    private void initViews() {
        detailsFragmentInTablet = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        mainBinding.swipeLayout.setOnRefreshListener(this);
    }

     /*
    * Setting up the adapter and set the adapter with recycler view
     */

    private void setUpRecyclerView() {
        mainBinding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(R.layout.post_layout, new ArrayList<RedditPostWrapper>(), this);
        mainBinding.postsRecyclerView.setHasFixedSize(true);
        mainBinding.postsRecyclerView.setAdapter(itemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, new LinearLayoutManager(this).getOrientation());
        mainBinding.postsRecyclerView.addItemDecoration(dividerItemDecoration);
    }



    /*
       *An utility method to make a server request
     */

    private void makeServerRequest() {
        showTheProgressBar(true);
        controller.start();
    }

    /*
    * An utility method to Hide Or Visible the Progress bar
     */

    private void showTheProgressBar(boolean show) {
        mainBinding.toolbarInclude.menuProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;

        // If tab layout is active ONLY then show the share icon
        if (isTableLayoutIsActive()) {
            menu.findItem(R.id.share_menu).setVisible(true);
        }

        Log.d(TAG, "onCreateOptionsMenu: end");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: start");
        switch (item.getItemId()) {
            case R.id.refresh_post_list_menu:
                if (Util.checkConnection(this)) {
                    showRefreshIcon(false);
                    fireUpNetworkRequest();
                    if (isTableLayoutIsActive() && getCurrentPostFromSharedPrefs() != null) {
                        Log.d(TAG, "onOptionsItemSelected: Lansdscape is active also refresh the web page");
                        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
                        detailsFragment.setPost(getCurrentPostFromSharedPrefs());
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.share_menu:
                /*
                * If tab layout is active and user selected some post then the share button will work otherwise user get the toast message "No post selected"
                 */
                if (isTableLayoutIsActive() && getSelectedPost() != null) {
                    Log.d(TAG, "onOptionsItemSelected: Yes do allow the share");
                    Util.shareThisPostWithFriends(this, detailsFragmentInTablet.getTabLayout(), getSelectedPost());
                } else {
                    Toast.makeText(this, getString(R.string.no_post_selected), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onOptionsItemSelected: No post is selected so not able to share");
                }

                return true;
        }
        Log.d(TAG, "onOptionsItemSelected: end");
        return super.onOptionsItemSelected(item);
    }

    private void showRefreshIcon(boolean command) {
        if ((menu != null)) {
            menu.findItem(R.id.refresh_post_list_menu).setVisible(command);
        } else {
            Log.e(TAG, "showRefreshIcon: Menu is NULL");
        }
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
        long timeDiff = controller.getTimeDifferenceInMinutes();
        Log.d(TAG, "onResume: Time Difference ===> " + timeDiff);

        if (timeDiff == 0 && controller.getCacheDataFromSharedPrefs(this).size() > 1) {
            Log.d(TAG, "onResume: In 0");
            mainBinding.toolbarInclude.customToolbar.setSubtitle(getString(R.string.update_message_less_then_minute));
        } else if (timeDiff > 0 && timeDiff < 5 && controller.getCacheDataFromSharedPrefs(this).size() > 1) {
            Log.d(TAG, "onResume: > 0 AND < 5");
            mainBinding.toolbarInclude.customToolbar.setSubtitle(String.format(getResources().getString(R.string.update_message_more_than_minute), timeDiff,
                    (timeDiff == 1 ? getResources().getString(R.string.minute) : getResources().getString(R.string.minutes))));
        } else if (timeDiff >= 5 && Util.checkConnection(this)) {
            Log.d(TAG, "onResume: >=5");
            showRefreshIcon(false);
            fireUpNetworkRequest();
        } else {
            Log.d(TAG, "onResume: ELSE");
        }


        hideTheTabsInLandscapeLayout();
        actionsPerformIfPostIsSelectedInLandscape();

        if (controller.getCacheDataFromSharedPrefs(this).size() < 1) {
            showNoOfflineDataTextView(true);
        } else {
            showNoOfflineDataTextView(false);
        }

        Log.d(TAG, "onResume: end");
    }


    private void showNoOfflineDataTextView(boolean show) {
        mainBinding.noOfflineDataTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onDownloadCompleteListener(int responseCode, List<RedditPostWrapper> postsList) {
        Log.d(TAG, "onDownloadCompleteListener: start");
        Log.d(TAG, "onDownloadCompleteListener: Response Code ---> " + responseCode);

        showTheProgressBar(false);
        showRefreshIcon(true);

        switch (responseCode) {
            case MainActivity.RESPONSE_CODE_OK:
                Log.d(TAG, "onDownloadCompleteListener: Response code is 200 now updating the Adapter");
                setPostWrapperList(postsList); // setting the List field of the MainActivity
                itemsAdapter.updateAdapter(postWrapperList); // sending the actual data which is downloaded and parsed by the Retrofit
                Util.printList(postsList); // just for debug purpose printing the list

                if (controller.getCacheDataFromSharedPrefs(this).size() < 1) {
                    showNoOfflineDataTextView(true);
                } else {
                    showNoOfflineDataTextView(false);
                }
                break;
            default:
                Log.e(TAG, "onDownloadCompleteListener: Something wrong with the response response code is ---> " + responseCode);
        }
        Log.d(TAG, "onDownloadCompleteListener: end");
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

    // Call back method to set the subtitles of the toolbar from the Retrofit class
    @Override
    public void setMessageToToolbar(String message) {
        if (mainBinding.toolbarInclude != null) {
            mainBinding.toolbarInclude.customToolbar.setSubtitle(message);
        } else {
            Log.e(TAG, "setMessageToToolbar: Toolbar is NULL");
        }
    }

    // Call back method to set the subtitles of the toolbar from the Retrofit class
    @Override
    public void setMessageToToolbar(@StringRes int message) {
        if (mainBinding.toolbarInclude != null) {
            mainBinding.toolbarInclude.customToolbar.setSubtitle(message);
        } else {
            Log.e(TAG, "setMessageToToolbar: Toolbar is NULL");
        }
    }

    // Call back method for the Swipe to refresh layout

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: start");
        if (Util.checkConnection(this)) {
            fireUpNetworkRequest();
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
        mainBinding.swipeLayout.setRefreshing(false);
        Log.d(TAG, "onRefresh: end");
    }

    private void fireUpNetworkRequest() {
        showRefreshIcon(false);
        makeServerRequest();
    }
}
