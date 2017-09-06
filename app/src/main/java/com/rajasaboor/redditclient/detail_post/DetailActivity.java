package com.rajasaboor.redditclient.detail_post;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.appbar_layout.DetailViewPager;
import com.rajasaboor.redditclient.databinding.ActivityDetailBinding;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.util.Util;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setSupportActionBar(detailBinding.detailToolbar.customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DetailActivityFragment detailActivityFragment = (DetailActivityFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
        if (detailActivityFragment == null) {
            detailActivityFragment = DetailActivityFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, detailActivityFragment).commit();
        }

        getSupportActionBar().setTitle(getParcelableFromBundle().getPostTitle());
        detailActivityFragment.setPresenter(new DetailPresenter(getParcelableFromBundle()));
    }

    private RedditPost getParcelableFromBundle() {
        RedditPost post = getIntent().getExtras().getParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY);

        if (post != null) {
            return post;
        } else {
            throw new IllegalStateException("Something is wrong post is null");
        }
    }
}
