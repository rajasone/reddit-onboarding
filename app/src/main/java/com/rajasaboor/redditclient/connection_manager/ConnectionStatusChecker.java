package com.rajasaboor.redditclient.connection_manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by default on 8/8/2017.
 * This class will be responsible to check the connection status and will tell the caller class the current status of the connection (wifi)
 */

public class ConnectionStatusChecker {
    private static final String TAG = ConnectionStatusChecker.class.getSimpleName();

    public static boolean checkConnection(Context context) {
        boolean result = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if (networkInfo != null)
                result = networkInfo.isConnected();
        } catch (Exception e) {
            Log.e(TAG, "checkConnection: An error happen ===> " + e.getMessage());
            e.printStackTrace();
        }

        Log.d(TAG, "checkConnection: Result ===> " + result);
        return result;
    }
}
