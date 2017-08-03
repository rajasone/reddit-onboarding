package com.rajasaboor.redditclient.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rajasaboor.redditclient.R;

/**
 * Created by default on 8/3/2017.
 */
public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final String TAG = ItemsViewHolder.class.getSimpleName(); // Tag for the debug purposes
    private ConstraintLayout postParentLayout;
    private TextView upsTextView;
    private TextView titleTextView;
    private TextView userNameTextView;
    private TextView commentsTextView;
    private TextView linkTextView;
    private final IOnPostTapped onPostTapped;

    ItemsViewHolder(View itemView, IOnPostTapped onPostTapped) {
        super(itemView);
        this.onPostTapped = onPostTapped;
        postParentLayout = (ConstraintLayout) itemView.findViewById(R.id.post_parent_layout);
        upsTextView = (TextView) itemView.findViewById(R.id.ups_text_view);
        titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
        userNameTextView = (TextView) itemView.findViewById(R.id.user_name_text_view);
        commentsTextView = (TextView) itemView.findViewById(R.id.comments_text_view);
        linkTextView = (TextView) itemView.findViewById(R.id.link_text_view);

        postParentLayout.setOnClickListener(this); // listener is attach to detect when user tap on the post
    }

    ConstraintLayout getPostParentLayout() {
        return postParentLayout;
    }

    void setPostParentLayout(ConstraintLayout postParentLayout) {
        this.postParentLayout = postParentLayout;
    }

    TextView getUpsTextView() {
        return upsTextView;
    }

    TextView getTitleTextView() {
        return titleTextView;
    }

    TextView getUserNameTextView() {
        return userNameTextView;
    }

    TextView getCommentsTextView() {
        return commentsTextView;
    }

    TextView getLinkTextView() {
        return linkTextView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_parent_layout:
                if (onPostTapped != null) {
                    onPostTapped.onPostTappedListener(getAdapterPosition());
                }
                break;
        }
    }

    /*
    * An interface which is used to send the current position of the adapter to the caller in this case caller is the MainActivity
     */

    public interface IOnPostTapped {
        void onPostTappedListener(int position);
    }
}


