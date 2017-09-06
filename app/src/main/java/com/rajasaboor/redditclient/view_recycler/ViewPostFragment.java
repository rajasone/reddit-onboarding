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


    public static ViewPostFragment newInstance() {
        return new ViewPostFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate: end");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        fragmentBinding.swipeLayout.setOnRefreshListener(this);
        Log.d(TAG, "onCreateView: end");
        return fragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: start");
        super.onActivityCreated(savedInstanceState);
        settingUpTheViews();
        Log.d(TAG, "onActivityCreated: end");
    }

    private void settingUpTheViews() {
        setUpTheToolbar();
        setUpTheRecyclerView();
    }

    private void setUpTheRecyclerView() {
        Log.d(TAG, "setUpTheRecyclerView: start");
        fragmentBinding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsAdapter = new ItemsAdapter(R.layout.post_layout, new ArrayList<RedditPostWrapper>(), this);
        fragmentBinding.postsRecyclerView.setHasFixedSize(true);
        fragmentBinding.postsRecyclerView.setAdapter(itemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        fragmentBinding.postsRecyclerView.addItemDecoration(dividerItemDecoration);
        Log.d(TAG, "setUpTheRecyclerView: end");
    }

    private void setUpTheToolbar() {
        Log.d(TAG, "setUpTheToolbar: start");
        ((AppCompatActivity) getActivity()).setSupportActionBar(fragmentBinding.toolbarInclude.customToolbar);
        Log.d(TAG, "setUpTheToolbar: end");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: start");
        super.onResume();
        int lastDownloadTime = viewPresenter.manageTheLastDownloadTime();
        updateTheActionBarSubtitles(lastDownloadTime);
        actionPerformWhileRequestingServer(lastDownloadTime);

        viewPresenter.checkTheCacheAndRequestServer((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));

        Log.d(TAG, "onResume: end");
    }

    private void actionPerformWhileRequestingServer(int lastDownloadTime) {
        Log.d(TAG, "actionPerformWhileRequestingServer: start");
        if (lastDownloadTime >= 5) {
            Log.d(TAG, "actionPerformWhileRequestingServer: Requesting the call to server");
            refreshListHandler();
        }

        Log.d(TAG, "actionPerformWhileRequestingServer: end");
    }

    public void setViewPresenter(ViewPostContract.Presenter viewPresenter) {
        this.viewPresenter = viewPresenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        inflater.inflate(R.menu.main_menu, menu);
        setMenu(menu);
        Log.d(TAG, "onCreateOptionsMenu: end");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: start");
        switch (item.getItemId()) {
            case R.id.refresh_post_list_menu:
                refreshListHandler();
                break;
            case R.id.share_menu:
                break;
        }
        Log.d(TAG, "onOptionsItemSelected: end");
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
        Log.d(TAG, "updateTheActionBarSubtitles: start");
        Log.d(TAG, "updateTheActionBarSubtitles: Download Time ===> " + downloadTime);

        // if the app starts first time and no didn't request the server then don't show any subtitle
        if (!getActivity().getSharedPreferences(BuildConfig.SHARED_PREFS_NAME, MODE_PRIVATE).contains(BuildConfig.LAST_DOWNLOAD_TIME_KEY)) {
            return;
        }

        if (downloadTime == 0) {
            Log.d(TAG, "updateTheActionBarSubtitles: In 0");
            fragmentBinding.toolbarInclude.customToolbar.setSubtitle(getString(R.string.update_message_less_then_minute));
        } else if (downloadTime >= 1 && downloadTime < 5) {
            Log.d(TAG, "updateTheActionBarSubtitles: >=1 && < 5");
            fragmentBinding.toolbarInclude.customToolbar.setSubtitle(String.format(getResources().getString(R.string.update_message_more_than_minute), downloadTime,
                    (downloadTime == 1 ? getResources().getString(R.string.minute) : getResources().getString(R.string.minutes))));
        } else {
            Log.d(TAG, "updateTheActionBarSubtitles: >5");
            fragmentBinding.toolbarInclude.customToolbar.setSubtitle(String.format(getResources().getString(R.string.update_message_more_than_minute), downloadTime,
                    (downloadTime >= 5 ? getResources().getString(R.string.minute) : getResources().getString(R.string.minutes))));
        }

        Log.d(TAG, "updateTheActionBarSubtitles: end");
    }

    @Override
    public void showTheRefreshIcon(boolean hide) {
        if (getMenu() != null) {
            getMenu().findItem(R.id.refresh_post_list_menu).setVisible(hide);
        } else {
            Log.e(TAG, "showTheRefreshIcon: MENU IS NULL");
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
        fragmentBinding.toolbarInclude.menuProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateAdapter(List<RedditPostWrapper> redditPostWrapper) {
        Log.d(TAG, "updateAdapter: start");
        Log.d(TAG, "updateAdapter: Size of list in update adapter ===> " + redditPostWrapper.size());
        ((ViewPresenter) viewPresenter).setPostWrapperList(redditPostWrapper);
        toolbarSubtitleSetter();
        hideTheNoOfflineDataAvailableTextView(true);
        showProgressBar(false);
        showTheRefreshIcon(true);
        itemsAdapter.updateAdapter(redditPostWrapper);


        Log.d(TAG, "updateAdapter: end");
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
        Log.d(TAG, "onRefresh: start");
        refreshListHandler();
        fragmentBinding.swipeLayout.setRefreshing(false);
        Log.d(TAG, "onRefresh: end");
    }

    @Override
    public void onPostTappedListener(int position) {
        Log.d(TAG, "onPostTappedListener: start");
        Log.d(TAG, "onPostTappedListener: Position ===> " + position);
        Log.d(TAG, "onPostTappedListener: Title ===> " + ((ViewPresenter) viewPresenter).getPostWrapperList().get(position).getData().getPostTitle());

        Intent detail = new Intent(getContext(), DetailActivity.class);
        detail.putExtra(BuildConfig.INDIVIDUAL_POST_ITEM_KEY, ((ViewPresenter) viewPresenter).getPostWrapperList().get(position).getData());
        startActivity(detail);
        Log.d(TAG, "onPostTappedListener: end");
    }
}
