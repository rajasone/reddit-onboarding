package com.rajasaboor.redditclient.model;

/**
 * Created by default on 8/2/2017.
 * Child of {@link RedditChildren} a JSON Array Object which contains the following data {@link RedditPost} which we need
 */

public class RedditPostWrapper {
    private RedditPost data;

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
