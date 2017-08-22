package com.rajasaboor.redditclient.appbar_layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.rajasaboor.redditclient.fragments.PostFragment;
import com.rajasaboor.redditclient.model.RedditPost;

import static com.rajasaboor.redditclient.fragments.PostFragment.KEY_URL_STRING;

/**
 * Created by default on 8/4/2017.
 */

public class DetailViewPager extends FragmentStatePagerAdapter {
    private static final String TAG = DetailViewPager.class.getSimpleName();
    private RedditPost post;
    private static final int COMMENT_TAB_POSITION = 1;
    public static final String[] TABS_NAMES = {"Posts", "Comments"};


    public DetailViewPager(FragmentManager fm, RedditPost post) {
        super(fm);
        this.post = post;
        Log.d(TAG, "DetailViewPager: start/end");
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: start/end");
        Log.d(TAG, "getItem: Post Title ====> " + post.getPostTitle());

        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_URL_STRING, post);
        PostFragment postFragment = new PostFragment();

        switch (position) {
            case 0:
                Log.d(TAG, "getItem: Case 0");
                bundle.putInt("key", 0);
                postFragment.setArguments(bundle);
                return postFragment;
            case 1:
                Log.d(TAG, "getItem: Case 1");
                bundle.putInt("key", 1);
                postFragment.setArguments(bundle);
                return postFragment;
            default:
                throw new IllegalArgumentException("Unknown position ---> " + position);
        }

    }

    @Override
    public int getCount() {
        return (post.isPostIsSelf() ? 1 : 2);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d(TAG, "getPageTitle: start/end");
        return (post.isPostIsSelf() ? DetailViewPager.TABS_NAMES[DetailViewPager.COMMENT_TAB_POSITION] : DetailViewPager.TABS_NAMES[position]);
    }

    public RedditPost getPost() {
        return post;
    }

    public void setPost(RedditPost post) {
        this.post = post;
    }
}
