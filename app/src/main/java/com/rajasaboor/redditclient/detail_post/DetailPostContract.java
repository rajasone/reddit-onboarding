package com.rajasaboor.redditclient.detail_post;

import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by rajaSaboor on 9/5/2017.
 */

interface DetailPostContract {
    interface Presenter {
        RedditPost getPost();
    }
}
