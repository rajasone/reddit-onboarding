package com.rajasaboor.redditclient.util;

import android.net.Uri;
import android.util.Log;

import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.List;

/**
 * Created by default on 8/3/2017.
 * This class provides some static helper methods to other classes e.g displaying a list etc
 */

public class Util {
    private static final String TAG = Util.class.getSimpleName(); // Tag name for the Debug purposes

    public static void printList(List<RedditPostWrapper> postWrappers) {
        Log.d(TAG, "printList: start");
        for (RedditPostWrapper wrapper : postWrappers) {
            Log.d(TAG, "printList: Title ---> " + wrapper.getData().getPostTitle());
            Log.d(TAG, "printList: Author ---> " + wrapper.getData().getPostAuthor());
            Log.d(TAG, "printList: Comments Link ---> " + Uri.parse(Consts.BASE_URI).buildUpon().path(wrapper.getData().getCommentsLink()).build().toString());
            Log.d(TAG, "printList: Post URL ---> " + wrapper.getData().getPostURL());
            Log.d(TAG, "printList: Is PostIsSelf ---> " + wrapper.getData().isPostIsSelf());
            Log.d(TAG, "printList: Comments ---> " + wrapper.getData().getPostComments());
            Log.d(TAG, "printList: Is Video ---> " + wrapper.getData().isVideo());
            Log.d(TAG, "printList: Number of Ups ---> " + wrapper.getData().getNumberOfUps());
            Log.d(TAG, "printList: ************************************");
        }

        Log.d(TAG, "printList: end");
    }
}
