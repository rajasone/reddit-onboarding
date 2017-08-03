package com.rajasaboor.redditclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
    private RecyclerView postsRecyclerView;
    private ItemsAdapter itemsAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Ini views get the reference of the XML views
         */
        iniViews();
        RetrofitController controller = new RetrofitController(this);
        controller.start();
        setUpRecyclerView();
        /*
        * Initiate the call to the base URI
         */
        Log.d(TAG, "onCreate: end");
    }

    private void setUpRecyclerView() {
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(R.layout.post_layout, new ArrayList<RedditPostWrapper>(), this);
        postsRecyclerView.setAdapter(itemsAdapter);
    }

    private void iniViews() {
        postsRecyclerView = (RecyclerView) findViewById(R.id.posts_recycler_view);
    }

    @Override
    public void onDownloadCompleteListener(int responseCode, List<RedditPostWrapper> postsList) {
        Log.d(TAG, "onDownloadCompleteListener: start");
        Log.d(TAG, "onDownloadCompleteListener: Response Code ---> " + responseCode);
        Log.d(TAG, "onDownloadCompleteListener: Size of List ---> " + postsList.size());

        switch (responseCode) {
            case Consts.RESPONSE_CODE_OK:
                setPostWrapperList(postsList); // setting the List field of the MainActivity
                itemsAdapter.updateAdapter(postWrapperList); // sending the actual data which is downloaded and parsed by the Retrofit
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
        Log.d(TAG, "onPostTappedListener: end");
    }
}
