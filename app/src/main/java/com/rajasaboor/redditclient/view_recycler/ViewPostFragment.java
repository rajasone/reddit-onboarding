package com.rajasaboor.redditclient.view_recycler;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.adapter.ItemsAdapter;
import com.rajasaboor.redditclient.databinding.MainFragmentBinding;
import com.rajasaboor.redditclient.detail_post.DetailActivity;
import com.rajasaboor.redditclient.detail_post.DetailActivityFragment;
import com.rajasaboor.redditclient.fragments.DetailsFragment;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.util.Util;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rajaSaboor on 8/31/2017.
 */

public class ViewPostFragment extends Fragment implements ViewPostContract.View, ViewPresenter.UpdateAdapter,
        SwipeRefreshLayout.OnRefreshListener, ItemsAdapter.IOnPostTapped {
    private static final String TAG = ViewPostFragment.class.getSimpleName();
    private MainFragmentBinding fragmentBinding = null;
    private ViewPostContract.Presenter viewPresenter = null;
    private ItemsAdapter itemsAdapter = null;
    private Menu menu = null;
    private RedditPost selectedPost = null;


    public static ViewPostFragment newInstance() {
        return new ViewPostFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            selectedPost = savedInstanceState.getParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        fragmentBinding.swipeLayout.setOnRefreshListener(this);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingUpTheViews();
    }

    private void settingUpTheViews() {
        setUpTheRecyclerView();
    }

    private void setUpTheRecyclerView() {
        fragmentBinding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsAdapter = new ItemsAdapter(R.layout.post_layout, new ArrayList<RedditPostWrapper>(), this);
        fragmentBinding.postsRecyclerView.setHasFixedSize(true);
        fragmentBinding.postsRecyclerView.setAdapter(itemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        fragmentBinding.postsRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onResume() {
        super.onResume();

        int lastDownloadTime = viewPresenter.manageTheLastDownloadTime();
        updateTheActionBarSubtitles(lastDownloadTime);
        actionPerformWhileRequestingServer(lastDownloadTime);
        viewPresenter.checkTheCacheAndRequestServer((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));

        if (isTabletActive() && selectedPost != null) {
            hideTheDetailFragment(false);
            getDetailFragmentReferenceInTablet().setPost(selectedPost);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY, selectedPost);
        super.onSaveInstanceState(outState);
    }

    private void actionPerformWhileRequestingServer(int lastDownloadTime) {
        if (lastDownloadTime >= 5) {
            Log.d(TAG, "actionPerformWhileRequestingServer: Requesting the call to server");
            refreshListHandler();
        }
    }

    public void setViewPresenter(ViewPostContract.Presenter viewPresenter) {
        this.viewPresenter = viewPresenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        setMenu(menu);
        if (isTabletActive()) {
            menu.findItem(R.id.share_menu).setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_post_list_menu:
                refreshListHandler();
                break;
            case R.id.share_menu:
                if (selectedPost == null) {
                    showToast(getString(R.string.no_post_selected));
                } else {
                    sharePost();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void refreshListHandler() {
        if (Util.checkConnection((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE))) {
            showTheRefreshIcon(false);
            showProgressBar(true);
            viewPresenter.startTheDownloadProcess();
        } else {
            showToast(getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void updateTheActionBarSubtitles(int downloadTime) {
        // if the app starts first time and no didn't request the server then don't show any subtitle
        if (!getActivity().getSharedPreferences(BuildConfig.SHARED_PREFS_NAME, MODE_PRIVATE).contains(BuildConfig.LAST_DOWNLOAD_TIME_KEY)) {
            return;
        }

        ActionBar temp = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (temp == null) {
            Log.e(TAG, "updateTheActionBarSubtitles: Action bar is NULL");
            return;
        }

        if (downloadTime == 0) {
            Log.d(TAG, "updateTheActionBarSubtitles: In 0");
            temp.setSubtitle(getString(R.string.update_message_less_then_minute));
        } else if (downloadTime >= 1 && downloadTime < 5) {
            Log.d(TAG, "updateTheActionBarSubtitles: >=1 && < 5");
            temp.setSubtitle(String.format(getResources().getString(R.string.update_message_more_than_minute), downloadTime,
                    (downloadTime == 1 ? getResources().getString(R.string.minute) : getResources().getString(R.string.minutes))));
        } else {
            Log.d(TAG, "updateTheActionBarSubtitles: >5");
            temp.setSubtitle(String.format(getResources().getString(R.string.update_message_more_than_minute), downloadTime,
                    (downloadTime >= 5 ? getResources().getString(R.string.minute) : getResources().getString(R.string.minutes))));
        }

    }

    @Override
    public void showTheRefreshIcon(boolean hide) {
        if (getMenu() != null) {
            getMenu().findItem(R.id.refresh_post_list_menu).setVisible(hide);
        }
    }

    @Override
    public void hideTheNoOfflineDataAvailableTextView(boolean hide) {
        fragmentBinding.noOfflineDataTextView.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar(boolean show) {
        ((ViewActivity) getActivity()).showProgressBar(show);
    }

    @Override
    public boolean isTabletActive() {
        return (((ViewActivity) getActivity()).getMainBinding().detailFragmentContainer != null);
    }

    @Override
    public void updateAdapter(List<RedditPostWrapper> redditPostWrapper) {
        ((ViewPresenter) viewPresenter).setPostWrapperList(redditPostWrapper);
        toolbarSubtitleSetter();
        hideTheNoOfflineDataAvailableTextView(true);
        showProgressBar(false);
        showTheRefreshIcon(true);
        itemsAdapter.updateAdapter(redditPostWrapper);
    }

    private void toolbarSubtitleSetter() {
        int lastDownloadTime = viewPresenter.manageTheLastDownloadTime();
        updateTheActionBarSubtitles(lastDownloadTime);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void onRefresh() {
        refreshListHandler();
        fragmentBinding.swipeLayout.setRefreshing(false);
    }

    @Override
    public void onPostTappedListener(int position) {
        selectedPost = ((ViewPresenter) viewPresenter).getPostWrapperList().get(position).getData();

        if (!isTabletActive()) {
            Intent detail = new Intent(getContext(), DetailActivity.class);
            detail.putExtra(BuildConfig.INDIVIDUAL_POST_ITEM_KEY, ((ViewPresenter) viewPresenter).getPostWrapperList().get(position).getData());
            startActivity(detail);
        } else {
            hideTheDetailFragment(false);
            getDetailFragmentReferenceInTablet().setPost(selectedPost);
        }
    }

    @Override
    public void hideTheDetailFragment(boolean hide) {
        ((ViewActivity) getActivity()).getMainBinding().detailFragmentContainer.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public DetailsFragment getDetailFragmentReferenceInTablet() {
        return ((DetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container));
    }

    @Override
    public void sharePost() {
        String urlToShare = getDetailFragmentReferenceInTablet().getTabLayout().getSelectedTabPosition() == 0 ? selectedPost.getPostURL() : BuildConfig.BASE_URI + selectedPost.getCommentsLink();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getContext().getString(R.string.share_message), urlToShare));
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}
