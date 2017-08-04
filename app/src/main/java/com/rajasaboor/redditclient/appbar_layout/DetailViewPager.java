package com.rajasaboor.redditclient.appbar_layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

public class DetailViewPager extends FragmentPagerAdapter {
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
        switch (position) {
            case 0:
                Log.d(TAG, "getItem: Case 0");
                return PostFragment.newInstance(post);
            case 1:
                Log.d(TAG, "getItem: Case 1");
                return CommentsFragment.newInstance(post);
            default:
                throw new IllegalArgumentException("Unknown position ---> " + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d(TAG, "getPageTitle: start/end");
        return Consts.TABS_NAMES[position];
    }
}
