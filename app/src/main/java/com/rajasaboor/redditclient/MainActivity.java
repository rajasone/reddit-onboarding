package com.rajasaboor.redditclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.rajasaboor.redditclient.adapter.ItemsAdapter;
import com.rajasaboor.redditclient.adapter.ItemsViewHolder;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.retrofit.RetrofitController;
import com.rajasaboor.redditclient.util.Consts;
import com.rajasaboor.redditclient.util.Util;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RetrofitController.IOnDownloadComplete, ItemsViewHolder.IOnPostTapped {
    private static final String TAG = MainActivity.class.getSimpleName(); // Tag name for the Debug purposes
    private List<RedditPostWrapper> postWrapperList = new ArrayList<>();
    private RecyclerView postsRecyclerView = null;
    private ItemsAdapter itemsAdapter = null;
    private Toolbar toolbar; // custom toolbar with the progressbar
    private ProgressBar progressBar; // this custom bar will shown to user when the refresh request is made

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Ini views get the reference of the XML views
         */
        iniViews();
        setSupportActionBar(toolbar); // setting up the custom toolbar as the action bar
        setUpRecyclerView();

        SharedPreferences preferences = getSharedPreferences(Consts.SHARED_PREFS_NAME, MODE_PRIVATE);

        /*
        * Following if and else condition is used to check whether data is inside the shared prefs or not and also check for the orientation change
        * Check whether the data is inside the shared prefs
        * if data is found dont make the request call to the server
        * if data is not found OR the savedInstanceState is empty then make a request call
         */
        if (preferences.getString(Consts.KEY_TO_CHECK_DATA, null) != null || savedInstanceState != null) {
            Log.d(TAG, "onCreate: Data is inside the preferences");
            postWrapperList = getPostListFromSharedPrefs();
            itemsAdapter.updateAdapter(postWrapperList);
        } else {
               /*
            * Initiate the call to the base URI and VISIBLE the progress bar
            */

            Log.d(TAG, "onCreate: Preference is empty");
            makeServerRequest();
        }
        Log.d(TAG, "onCreate: end");
    }

    /*
       *An utility method to make a server request
     */

    private void makeServerRequest() {
        hideOrShowTheProgressBar(true);
        RetrofitController controller = new RetrofitController(this);
        controller.start();
    }

    /*
    * An utility method to Hide Or Visible the Progress bar
     */

    private void hideOrShowTheProgressBar(boolean command) {
        progressBar.setVisibility(command ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        getMenuInflater().inflate(R.menu.main_menu, menu);

        Log.d(TAG, "onCreateOptionsMenu: end");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: start");
        switch (item.getItemId()) {
            case R.id.refresh_post_list_menu:
                makeServerRequest();
                break;
        }
        Log.d(TAG, "onOptionsItemSelected: end");
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: start");
        super.onSaveInstanceState(outState);
        if (postWrapperList.size() > 0) {
            Log.d(TAG, "onSaveInstanceState: data is inside the list");
            savePostListInJSON();
        } else {
            Log.d(TAG, "onSaveInstanceState: Post list is empty");
        }
        Log.d(TAG, "onSaveInstanceState: end");
    }

    /*
    * An utility method to perform the save operation for the list of the posts
     */
    private void savePostListInJSON() {
        Log.d(TAG, "savePostListInJSON: start");
        SharedPreferences.Editor editor = getSharedPreferences(Consts.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(Consts.SIZE_OF_POST_LIST, postWrapperList.size());
        Gson gson = new Gson();
        for (int i = 0; i < postWrapperList.size(); i++) {
            editor.putString(Integer.toString(i), gson.toJson(postWrapperList.get(i)));
        }
        editor.apply();
        Log.d(TAG, "savePostListInJSON: end");
    }

    /*
    * An utility method which is use to fetch the post list from the shared prefs and convert the json string into the object
     */
    private List<RedditPostWrapper> getPostListFromSharedPrefs() {
        Log.d(TAG, "getPostListFromSharedPrefs: start");
        Gson gson = new Gson();
        List<RedditPostWrapper> temp = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(Consts.SHARED_PREFS_NAME, MODE_PRIVATE);
        for (int i = 0; i < sharedPreferences.getInt(Consts.SIZE_OF_POST_LIST, 0); i++)
            temp.add(gson.fromJson(sharedPreferences.getString(String.valueOf(i), ""), RedditPostWrapper.class));

        Log.d(TAG, "getPostListFromSharedPrefs: size of temp ---> " + temp.size());
        Log.d(TAG, "getPostListFromSharedPrefs: end");
        return temp;
    }

    /*
    * Setting up the adapter and set the adapter with recycler view
     */

    private void setUpRecyclerView() {
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(R.layout.post_layout, new ArrayList<RedditPostWrapper>(), this);
        postsRecyclerView.setAdapter(itemsAdapter);
    }

    private void iniViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_include);
        progressBar = (ProgressBar) toolbar.findViewById(R.id.menu_progress_bar);
        postsRecyclerView = (RecyclerView) findViewById(R.id.posts_recycler_view);
    }

    @Override
    public void onDownloadCompleteListener(int responseCode, List<RedditPostWrapper> postsList) {
        Log.d(TAG, "onDownloadCompleteListener: start");
        Log.d(TAG, "onDownloadCompleteListener: Response Code ---> " + responseCode);
        Log.d(TAG, "onDownloadCompleteListener: Size of List ---> " + postsList.size());
        hideOrShowTheProgressBar(false);

        switch (responseCode) {
            case Consts.RESPONSE_CODE_OK:
                Log.d(TAG, "onDownloadCompleteListener: Response code is 200 now updating the Adapter");
                setPostWrapperList(postsList); // setting the List field of the MainActivity
                itemsAdapter.updateAdapter(postWrapperList); // sending the actual data which is downloaded and parsed by the Retrofit
                savePostListInJSON();
                Util.printList(postsList); // just for debug purpose printing the list
                break;
            default:
                Log.e(TAG, "onDownloadCompleteListener: Something wrong with the response response code is ---> " + responseCode);
        }


        Log.d(TAG, "onDownloadCompleteListener: end");
    }

    public void setPostWrapperList(List<RedditPostWrapper> postWrapperList) {
        this.postWrapperList = postWrapperList;
    }

    public List<RedditPostWrapper> getPostWrapperList() {
        return postWrapperList;
    }

    @Override
    public void onPostTappedListener(int position) {
        Log.d(TAG, "onPostTappedListener: start");
        Log.d(TAG, "onPostTappedListener: position ---> " + position);
        // TODO: 8/3/2017 will place the functionality for the detail screen
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(Consts.INDIVIDUAL_POST_ITEM_KEY, getPostListFromSharedPrefs().get(position).getData());
        startActivity(detailActivityIntent);
        Log.d(TAG, "onPostTappedListener: end");
    }
}
