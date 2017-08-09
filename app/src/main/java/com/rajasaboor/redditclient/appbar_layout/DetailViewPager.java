package com.rajasaboor.redditclient.appbar_layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.rajasaboor.redditclient.fragments.CommentsFragment;
import com.rajasaboor.redditclient.fragments.PostFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.util.Consts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by default on 8/4/2017.
 */

public class DetailViewPager extends FragmentStatePagerAdapter {
    private static final String TAG = DetailViewPager.class.getSimpleName();
    //// TODO: 8/4/2017 Add the fragments in the fragment list
    private List<Fragment> fragmentList = new ArrayList<>();
    private RedditPost post;

    public void addFragmentsInList(Fragment fragment) {
        Log.d(TAG, "addFragmentsInList: start");
        fragmentList.add(fragment);
        Log.d(TAG, "addFragmentsInList: end");
    }


    public DetailViewPager(FragmentManager fm, RedditPost post) {
        super(fm);
        this.post = post;
        Log.d(TAG, "DetailViewPager: start/end");
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: start/end");
        Log.d(TAG, "getItem: Post Title ====> " + post.getPostTitle());
        switch (position) {
            case 0:
                Log.d(TAG, "getItem: Case 0");
                return (!post.isPostIsSelf() ? PostFragment.newInstance(post) : CommentsFragment.newInstance(post));
            case 1:
                Log.d(TAG, "getItem: Case 1");
                return CommentsFragment.newInstance(post);
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
        return (post.isPostIsSelf() ? Consts.TABS_NAMES[Consts.COMMENT_TAB_POSITION] : Consts.TABS_NAMES[position]);
    }

    public RedditPost getPost() {
        return post;
    }

    public void setPost(RedditPost post) {
        this.post = post;
    }
}
