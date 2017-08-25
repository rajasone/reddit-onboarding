package com.rajasaboor.redditclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rajasaboor.redditclient.appbar_layout.DetailViewPager;
import com.rajasaboor.redditclient.fragments.PostFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.util.Util;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private RedditPost redditPost; // this object is passed by the MainActivity when user tap on the post


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent().getExtras() != null) {
            Log.d(TAG, "onCreate: Bundle have data");
            redditPost = (RedditPost) getIntent().getExtras().getParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY);
        } else {
            Log.d(TAG, "onCreate: Bundle is empty");
        }

        /*
        * Getting the references of Views
         */
        initViews();
        setUpTheToolbar();
        setUpThePager();


        Log.d(TAG, "onCreate: end");
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.appbar_parent_tab);
    }


    private void setUpTheToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(redditPost.getPostTitle());
    }

    private void setUpThePager() {
        DetailViewPager detailViewPager = new DetailViewPager(getSupportFragmentManager(), redditPost);
        viewPager.setAdapter(detailViewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.d(TAG, "onSaveInstanceState: start");
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG, "onSaveInstanceState: end");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        getMenuInflater().inflate(R.menu.share_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: end");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: start");

        switch (item.getItemId()) {
            case R.id.share_menu:
                Util.shareThisPostWithFriends(this, tabLayout, redditPost);
                return true;
        }
        Log.d(TAG, "onOptionsItemSelected: end");
        return false;
    }

}
