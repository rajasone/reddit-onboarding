package com.rajasaboor.redditclient.model;

/**
 * Created by default on 8/2/2017.
 * Parent JSON object "DATA"
 */

public class RedditRespone {

    private RedditChildren data;

    public RedditRespone(RedditChildren data) {
        this.data = data;
    }

    public RedditChildren getData() {
        return data;
    }

    public void setData(RedditChildren data) {
        this.data = data;
    }
}
