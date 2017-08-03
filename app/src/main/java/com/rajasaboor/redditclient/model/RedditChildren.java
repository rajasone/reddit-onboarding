package com.rajasaboor.redditclient.model;

import java.util.List;

/**
 * Created by default on 8/2/2017.
 * Child of {@link RedditRespone} contains the JSON array named as CHILDREN
 */

public class RedditChildren {
    private List<RedditPostWrapper> children;

    public RedditChildren(List<RedditPostWrapper> children) {
        this.children = children;
    }

    public List<RedditPostWrapper> getChildren() {
        return children;
    }

    public void setChildren(List<RedditPostWrapper> children) {
        this.children = children;
    }
}
