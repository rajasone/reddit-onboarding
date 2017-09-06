package com.rajasaboor.redditclient.detail_post;

import android.support.design.widget.TabLayout;

import com.rajasaboor.redditclient.model.RedditPost;

/**
 * Created by rajaSaboor on 9/5/2017.
 */

public interface DetailPostContract {
    interface View {

        void setUpViewPager();

        void sharePost(TabLayout tabLayout, RedditPost post);
    }

    interface Presenter {
    }
}
