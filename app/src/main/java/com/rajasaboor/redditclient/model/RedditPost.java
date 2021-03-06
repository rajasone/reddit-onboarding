package com.rajasaboor.redditclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by default on 8/2/2017.
 * The Reddit Model which contains the post attributes e.g Title, username, comments etc
 */

public class RedditPost implements Parcelable {
    /*
    Tag is used for the logging/debug purposes
     */
    private static final String TAG = RedditPost.class.getSimpleName();
    @SerializedName("title")
    private String postTitle;

    @SerializedName("author")
    private String postAuthor;

    @SerializedName("permalink")
    private String commentsLink;

    @SerializedName("url")
    private String postURL;

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

    public RedditPost(String postTitle, String postAuthor, String commentsLink, String postURL, boolean isPostIsSelf, int postComments, int numberOfUps, boolean isVideo) {
        this.postTitle = postTitle;
        this.postAuthor = postAuthor;
        this.commentsLink = commentsLink;
        this.postURL = postURL;
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

    public String getCommentsLink() {
        return commentsLink;
    }

    public void setCommentsLink(String commentsLink) {
        this.commentsLink = commentsLink;
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

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    @Override
    public String toString() {
        return "RedditPost{" +
                "postTitle='" + postTitle + '\'' +
                ", postAuthor='" + postAuthor + '\'' +
                ", commentsLink='" + commentsLink + '\'' +
                ", isPostIsSelf=" + isPostIsSelf +
                ", postComments=" + postComments +
                ", numberOfUps=" + numberOfUps +
                ", isVideo=" + isVideo +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postTitle);
        dest.writeString(this.postAuthor);
        dest.writeString(this.commentsLink);
        dest.writeString(this.postURL);
        dest.writeByte(this.isPostIsSelf ? (byte) 1 : (byte) 0);
        dest.writeInt(this.postComments);
        dest.writeInt(this.numberOfUps);
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
    }

    protected RedditPost(Parcel in) {
        this.postTitle = in.readString();
        this.postAuthor = in.readString();
        this.commentsLink = in.readString();
        this.postURL = in.readString();
        this.isPostIsSelf = in.readByte() != 0;
        this.postComments = in.readInt();
        this.numberOfUps = in.readInt();
        this.isVideo = in.readByte() != 0;
    }

    public static final Creator<RedditPost> CREATOR = new Creator<RedditPost>() {
        @Override
        public RedditPost createFromParcel(Parcel source) {
            return new RedditPost(source);
        }

        @Override
        public RedditPost[] newArray(int size) {
            return new RedditPost[size];
        }
    };
}
