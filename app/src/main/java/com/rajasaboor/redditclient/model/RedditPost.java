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
    private final String postTitle;

    @SerializedName("author")
    private final String postAuthor;

    @SerializedName("permalink")
    private final String commentsLink;

    @SerializedName("url")
    private final String postURL;

    @SerializedName("is_self")
    private final boolean isPostIsSelf;

    @SerializedName("num_comments")
    private final int postComments;

    @SerializedName("ups")
    private final int numberOfUps;

    @SerializedName("is_video")
    private final boolean isVideo;


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

    public String getPostAuthor() {
        return postAuthor;
    }

    public String getCommentsLink() {
        return commentsLink;
    }

    public boolean isPostIsSelf() {
        return isPostIsSelf;
    }

    public int getPostComments() {
        return postComments;
    }

    public int getNumberOfUps() {
        return numberOfUps;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public String getPostURL() {
        return postURL;
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
