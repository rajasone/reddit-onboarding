package com.rajasaboor.redditclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.List;

/**
 * Created by default on 8/3/2017.
 * An adpater class which is responsible for holding the data
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsViewHolder> {
    private static final String TAG = ItemsAdapter.class.getSimpleName(); // Tag for the debug purposes
    private List<RedditPostWrapper> postWrapperList = null; // Data source which is assigned to this by constructor or setter method
    private int layoutResource; // Layout address which will be inflate by this class
    private ItemsViewHolder.IOnPostTapped onPostTapped;

    public ItemsAdapter(int layoutResource, List<RedditPostWrapper> postWrapperList, ItemsViewHolder.IOnPostTapped onPostTapped) {
        this.layoutResource = layoutResource;
        this.postWrapperList = postWrapperList;
        this.onPostTapped = onPostTapped;
    }

    /*
    * Inflate the layout and return the object of {@link ItemsViewHolder}
     */
    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemsViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false), onPostTapped);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        // Getting a string from the XML strings file and placing the argument for a placeholder
        holder.getUpsTextView().setText(holder.getUpsTextView().getContext().getString(R.string.ups_text, postWrapperList.get(position).getData().getNumberOfUps()));
        holder.getTitleTextView().setText(postWrapperList.get(position).getData().getPostTitle());
        holder.getUserNameTextView().setText(postWrapperList.get(position).getData().getPostAuthor());
        // Getting a string from the XML strings file and placing the argument for a placeholder
        holder.getCommentsTextView().setText(holder.getUpsTextView().getContext().getString(R.string.comments_text, postWrapperList.get(position).getData().getPostComments()));
        // Getting a string from the XML strings file and placing the Link argument for a placeholder
        holder.getLinkTextView().setText(holder.getUpsTextView().getContext().getString(R.string.base_link, postWrapperList.get(position).getData().getCommentsLink()));
    }

    @Override
    public int getItemCount() {
        return (postWrapperList != null ? postWrapperList.size() : 1);
    }

    public void updateAdapter(List<RedditPostWrapper> postWrappers) {
        postWrapperList = postWrappers;
        notifyDataSetChanged();
    }
}

