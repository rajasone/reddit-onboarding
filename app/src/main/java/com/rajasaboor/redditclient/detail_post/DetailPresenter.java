package com.rajasaboor.redditclient.detail_post;

import android.util.Log;

import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by rajaSaboor on 9/5/2017.
 */

class DetailPresenter implements DetailPostContract.Presenter {
    private static final String TAG = DetailPresenter.class.getSimpleName();
    private RedditPost post = null;

    DetailPresenter(RedditPost post) {
        this.post = post;
    }

    public RedditPost getPost() {
        return post;
    }
}
