package com.rajasaboor.redditclient.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rajasaboor.redditclient.MainActivity;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.connection_manager.ConnectionStatusChecker;
import com.rajasaboor.redditclient.util.Util;

import org.w3c.dom.Text;

/**
 * Created by default on 8/3/2017.
 */
public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final String TAG = ItemsViewHolder.class.getSimpleName(); // Tag for the debug purposes
    private ConstraintLayout postParentLayout;
    private ImageView upsImageView;
    private TextView upsTextView;
    private TextView titleTextView;
    private TextView userNameTextView;
    private TextView commentsTextView;
    private TextView linkTextView;
    private final IOnPostTapped onPostTapped;
    private int listSize = 0; // check the list size if size is less than 1 don't call the interface method
    private TextView noOfflineDataTextView;


    ItemsViewHolder(View itemView, IOnPostTapped onPostTapped, int listSize) {
        super(itemView);
        this.onPostTapped = onPostTapped;
        this.listSize = listSize;

        upsImageView = itemView.findViewById(R.id.ups_image_view);
        postParentLayout = (ConstraintLayout) itemView.findViewById(R.id.post_parent_layout);
        upsTextView = (TextView) itemView.findViewById(R.id.ups_text_view);
        titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
        userNameTextView = (TextView) itemView.findViewById(R.id.user_name_text_view);
        commentsTextView = (TextView) itemView.findViewById(R.id.comments_text_view);
        linkTextView = (TextView) itemView.findViewById(R.id.link_text_view);
        noOfflineDataTextView = itemView.findViewById(R.id.no_offline_data_message_text_view);


        postParentLayout.setOnClickListener(this); // listener is attach to detect when user tap on the post
    }

    ConstraintLayout getPostParentLayout() {
        return postParentLayout;
    }

    void setPostParentLayout(ConstraintLayout postParentLayout) {
        this.postParentLayout = postParentLayout;
    }

    ImageView getUpsImageView() {
        return upsImageView;
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

    public TextView getNoOfflineDataTextView() {
        return noOfflineDataTextView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_parent_layout:
                if ((onPostTapped != null) && (ConnectionStatusChecker.checkConnection(view.getContext())) && (listSize > 0)) {
                    Log.d(TAG, "onClick: Sending the call to the interface");
                    onPostTapped.onPostTappedListener(getAdapterPosition());
                } else {
                    if (listSize == 0) {
                        return;
                    }
                    Util.showToast(view.getContext(), view.getContext().getResources().getString(R.string.no_internet_connection));
                    Log.e(TAG, "onClick: No internet connection");
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


