package com.rajasaboor.redditclient.view_recycler;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajaSaboor on 8/31/2017.
 */

public class ViewPostFragment extends Fragment implements ViewPostContract.FragmentView,
        SwipeRefreshLayout.OnRefreshListener, ItemsAdapter.IOnPostTapped {

    private static final String TAG = ViewPostFragment.class.getSimpleName();
    private MainFragmentBinding fragmentBinding = null;
    private ItemsAdapter itemsAdapter = null;
    private Menu menu = null;
    private ViewPostContract.Presenter viewPresenter;


    public static ViewPostFragment newInstance() {
        return new ViewPostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        fragmentBinding.swipeLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);
        setUpRecyclerView();

        if (savedInstanceState != null) {
            viewPresenter.setSelectedPost((RedditPost) savedInstanceState.getParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY));
        }
        return fragmentBinding.getRoot();
    }

    private void setUpRecyclerView() {
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
        viewPresenter.loadCacheOrRequestServer();
        viewPresenter.checkCurrentLayoutAndSetUpViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BuildConfig.INDIVIDUAL_POST_ITEM_KEY, viewPresenter.getSelectedPost());
    }

    public void setViewPresenter(ViewPostContract.Presenter viewPresenter) {
        this.viewPresenter = viewPresenter;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        setMenu(menu);
        if (viewPresenter.isTabletLayoutIsActive()) {
            menu.findItem(R.id.share_menu).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_post_list_menu:
                viewPresenter.handleRefreshIcon();
                break;
            case R.id.share_menu:
                viewPresenter.sharePost();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorDialogWhileServerRequest() {
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(getContext());
        errorDialog.setTitle(getResources().getString(R.string.error_alert_dialog_title))
                .setCancelable(false)
                .setMessage(getResources().getString(R.string.fetching_data_failed))
                .setNegativeButton(getResources().getString(R.string.error_dialog_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: Dismiss the dialog");
                        dialogInterface.dismiss();
                    }
                });
        errorDialog.show();
    }

    @Override
    public String getMessageFromStringRes(@StringRes int resID) {
        return getString(resID);
    }

    @Override
    public void showRefreshIcon(boolean hide) {
        if (getMenu() != null) {
            getMenu().findItem(R.id.refresh_post_list_menu).setVisible(hide);
        }
    }

    @Override
    public void hideNoOfflineDataAvailableTextView(boolean hide) {
        fragmentBinding.noOfflineDataTextView.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void updatePostAdapter(List<RedditPostWrapper> wrapperList) {
        itemsAdapter.updateAdapter(wrapperList);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void onRefresh() {
        viewPresenter.requestServer();
        fragmentBinding.swipeLayout.setRefreshing(false);
    }

    @Override
    public void onPostTappedListener(int position) {
        Log.d(TAG, "onPostTappedListener: Position ---> " + position);
        viewPresenter.setSelectedPost(viewPresenter.getPostWrapperList().get(position).getData());

        if (!viewPresenter.isTabletLayoutIsActive()) {
            Intent detail = new Intent(getContext(), DetailActivity.class);
            detail.putExtra(BuildConfig.INDIVIDUAL_POST_ITEM_KEY, viewPresenter.getPostWrapperList().get(position).getData());
            startActivity(detail);
        } else {
            viewPresenter.checkCurrentLayoutAndSetUpViews();
        }
    }
}
