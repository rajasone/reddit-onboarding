package com.rajasaboor.redditclient.detail_post;

import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by rajaSaboor on 9/5/2017.
 */

class DetailPresenter implements DetailPostContract.Presenter {
    private RedditPost post = null;

    DetailPresenter(RedditPost post) {
        this.post = post;
    }

    @Override
    public RedditPost getPost() {
        return post;
    }

}
