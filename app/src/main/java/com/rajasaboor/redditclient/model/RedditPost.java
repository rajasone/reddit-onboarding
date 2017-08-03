package com.rajasaboor.redditclient.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by default on 8/2/2017.
 * The Reddit Model which contains the post attributes e.g Title, username, comments etc
 */

public class RedditPost implements Serializable {
    /*
    Tag is used for the logging/debug purposes
     */
    private static final long serialVersionUID = 1L; // to make sure that serializable interface work on all devices
    private static final String TAG = RedditPost.class.getSimpleName();
    @SerializedName("title")
    private String postTitle;

    @SerializedName("author")
    private String postAuthor;

    @SerializedName("permalink")
    private String postLink;

    @SerializedName("is_self")
    private boolean isPostIsSelf;

    @SerializedName("num_comments")
    private int postComments;

    @SerializedName("ups")
    private int numberOfUps;

    @SerializedName("is_video")
    private boolean isVideo;

    public RedditPost() {
    }

    public RedditPost(String postTitle, String postAuthor, String postLink, boolean isPostIsSelf, int postComments, int numberOfUps, boolean isVideo) {
        this.postTitle = postTitle;
        this.postAuthor = postAuthor;
        this.postLink = postLink;
        this.isPostIsSelf = isPostIsSelf;
        this.postComments = postComments;
        this.numberOfUps = numberOfUps;
        this.isVideo = isVideo;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public boolean isPostIsSelf() {
        return isPostIsSelf;
    }

    public void setPostIsSelf(boolean postIsSelf) {
        isPostIsSelf = postIsSelf;
    }

    public int getPostComments() {
        return postComments;
    }

    public void setPostComments(int postComments) {
        this.postComments = postComments;
    }

    public int getNumberOfUps() {
        return numberOfUps;
    }

    public void setNumberOfUps(int numberOfUps) {
        this.numberOfUps = numberOfUps;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @Override
    public String toString() {
        return "RedditPost{" +
                "postTitle='" + postTitle + '\'' +
                ", postAuthor='" + postAuthor + '\'' +
                ", postLink='" + postLink + '\'' +
                ", isPostIsSelf=" + isPostIsSelf +
                ", postComments=" + postComments +
                ", numberOfUps=" + numberOfUps +
                ", isVideo=" + isVideo +
                '}';
    }
}
