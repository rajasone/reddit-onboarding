package com.rajasaboor.redditclient.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import java.util.List;

/**
 * Created by default on 8/3/2017.
 * This class provides some static helper methods to other classes e.g displaying a list etc
 */

public class Util {
    private static final String TAG = Util.class.getSimpleName(); // Tag name for the Debug purposes

    public static void printList(List<RedditPostWrapper> postWrappers) {
        Log.d(TAG, "printList: start");
        for (RedditPostWrapper wrapper : postWrappers) {
            Log.d(TAG, "printList: Title ---> " + wrapper.getData().getPostTitle());
            Log.d(TAG, "printList: Author ---> " + wrapper.getData().getPostAuthor());
            Log.d(TAG, "printList: Comments Link ---> " + Uri.parse(Consts.BASE_URI).buildUpon().path(wrapper.getData().getCommentsLink()).build().toString());
            Log.d(TAG, "printList: Post URL ---> " + wrapper.getData().getPostURL());
            Log.d(TAG, "printList: Is PostIsSelf ---> " + wrapper.getData().isPostIsSelf());
            Log.d(TAG, "printList: Comments ---> " + wrapper.getData().getPostComments());
            Log.d(TAG, "printList: Is Video ---> " + wrapper.getData().isVideo());
            Log.d(TAG, "printList: Number of Ups ---> " + wrapper.getData().getNumberOfUps());
            Log.d(TAG, "printList: ************************************");
        }

        Log.d(TAG, "printList: end");
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void displayErrorDialog(Context context, String errorMessage, boolean isCancelAble) {
        Log.d(TAG, "displayErrorDialog: start");
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.error_alert_dialog_title))
                .setCancelable(isCancelAble)
                .setMessage(errorMessage)
                .setNegativeButton(context.getResources().getString(R.string.error_dialog_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: Dismiss the dialog");
                        dialogInterface.dismiss();
                    }
                });

        errorDialog.show();
        Log.d(TAG, "displayErrorDialog: end");
    }
}
