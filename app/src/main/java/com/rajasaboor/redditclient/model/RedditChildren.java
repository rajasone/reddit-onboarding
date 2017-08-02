package com.rajasaboor.redditclient.model;

import java.util.List;

/**
 * Created by default on 8/2/2017.
 */

public class RedditChildren {
    public List<RedditPostWrapper> children;

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
