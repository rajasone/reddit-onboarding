package com.rajasaboor.redditclient.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.databinding.PostLayoutBinding;
import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.util.Util;

import java.util.List;

/**
 * Created by default on 8/3/2017.
 * An adpater class which is responsible for holding the data
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    private static final String TAG = ItemsAdapter.class.getSimpleName();
    private List<RedditPostWrapper> postWrapperList = null; // Data source which is assigned to this by constructor or setter method
    private int layoutResource; // Layout address which will be inflate by this class
    private ItemsAdapter.IOnPostTapped onPostTapped = null;


    public ItemsAdapter(@LayoutRes int layoutResource, List<RedditPostWrapper> postWrapperList, ItemsAdapter.IOnPostTapped onPostTapped) {
        this.layoutResource = layoutResource;
        this.postWrapperList = postWrapperList;
        this.onPostTapped = onPostTapped;
    }

    /*
    * Inflate the layout and return the object of {@link ItemsViewHolder}
     */
    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PostLayoutBinding postLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResource, parent, false);
        return new ItemsViewHolder(postLayoutBinding, onPostTapped);
    }


    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        holder.bind(postWrapperList.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return ((postWrapperList != null) ? postWrapperList.size() : 0);
    }

    public void updateAdapter(List<RedditPostWrapper> postWrappers) {
        postWrapperList = postWrappers;
        notifyDataSetChanged();
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final String TAG = ItemsViewHolder.class.getSimpleName(); // Tag for the debug purposes
        private final ItemsAdapter.IOnPostTapped onPostTapped;
        private final PostLayoutBinding postLayoutBinding;


        ItemsViewHolder(PostLayoutBinding postLayoutBinding, ItemsAdapter.IOnPostTapped onPostTapped) {
            super(postLayoutBinding.getRoot());
            this.onPostTapped = onPostTapped;
            this.postLayoutBinding = postLayoutBinding;
            postLayoutBinding.getRoot().setOnClickListener(this);
        }

        void bind(RedditPost post) {
            postLayoutBinding.setPost(post);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.post_parent_layout:
                    if ((onPostTapped != null) && (Util.checkConnection((ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)))) {
                        Log.d(TAG, "onClick: Sending the call to the interface");
                        onPostTapped.onPostTappedListener(getAdapterPosition());
                    } else {
                        Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onClick: No internet connection");
                    }
                    break;
            }
        }
    }

    /*
    * An interface which is used to send the current position of the adapter to the caller in this case caller is the MainActivity
     */

    public interface IOnPostTapped {
        void onPostTappedListener(int position);
    }
}

