package com.rajasaboor.redditclient.appbar_layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rajasaboor.redditclient.detail_post.PostFragment;
import com.rajasaboor.redditclient.model.RedditPost;


/**
 * Created by default on 8/4/2017.
 */

public class DetailPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = DetailPagerAdapter.class.getSimpleName();
    private RedditPost post;
    private static final int COMMENT_TAB_POSITION = 1;
    private static final String[] TABS_NAMES = {"Posts", "Comments"};


    public DetailPagerAdapter(FragmentManager fm, RedditPost post) {
        super(fm);
        this.post = post;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PostFragment.newInstance(post, true);
            case 1:
                return PostFragment.newInstance(post, false);
            default:
                throw new IllegalArgumentException("Unknown position ---> " + position);
        }

    }

    @Override
    public int getCount() {
        return ((post != null) && (post.isPostIsSelf()) ? 1 : 2);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ((post != null) && (post.isPostIsSelf()) ? DetailPagerAdapter.TABS_NAMES[DetailPagerAdapter.COMMENT_TAB_POSITION] :
                DetailPagerAdapter.TABS_NAMES[position]);
    }

    public RedditPost getPost() {
        return post;
    }

    public void setPost(RedditPost post) {
        this.post = post;
    }

}
