package com.rajasaboor.redditclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.retrofit.RetrofitController;
import com.rajasaboor.redditclient.util.Consts;
import com.rajasaboor.redditclient.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RetrofitController.IOnDownloadComplete {
    private static final String TAG = MainActivity.class.getSimpleName(); // Tag name for the Debug purposes
    private List<RedditPostWrapper> postWrapperList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Initiate the call to the base URI
         */
        RetrofitController controller = new RetrofitController(this);
        controller.start();
        Log.d(TAG, "onCreate: end");
    }

    @Override
    public void onDownloadCompleteListener(int responseCode, List<RedditPostWrapper> postsList) {
        Log.d(TAG, "onDownloadCompleteListener: start");
        Log.d(TAG, "onDownloadCompleteListener: Response Code ---> " + responseCode);
        Log.d(TAG, "onDownloadCompleteListener: Size of List ---> " + postsList.size());

        switch (responseCode) {
            case Consts.RESPONSE_CODE_OK:
                setPostWrapperList(postsList);
                Util.printList(postsList);
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
}
