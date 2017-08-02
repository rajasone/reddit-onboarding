package com.rajasaboor.redditclient.model;

/**
 * Created by default on 8/2/2017.
 */

public class RedditPostWrapper {
    public RedditPost data;

    public RedditPostWrapper(RedditPost data) {
        this.data = data;
    }

    public RedditPost getData() {
        return data;
    }

    public void setData(RedditPost data) {
        this.data = data;
    }
}
