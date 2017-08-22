package com.rajasaboor.redditclient.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rajasaboor.redditclient.BuildConfig;
import com.rajasaboor.redditclient.MainActivity;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.model.RedditRespone;
import com.rajasaboor.redditclient.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by default on 8/2/2017.
 * A connection class which connects to the internet and download the raw data from the {@link BuildConfig} and when the download is complete without any exception
 * a call back method is invoke which sends the response back to the caller (in this case caller in MainActivity)
 */


interface IRetroApi {
    @GET(".json")
    Call<RedditRespone> loadChanges();
}

public class RetrofitController implements Callback<RedditRespone> {
    private static final String TAG = RetrofitController.class.getSimpleName(); // Tag name for the Debug purposes
    private static final String LAST_DOWNLOAD_MESSAGE_KEY = "download_key";
    private final IOnDownloadComplete onDownloadComplete; // An interface instance to call the listener method
    private long lastUpdateTimeInMilliSeconds;
    private IPublishLastDownloadTimeInToolbar downloadTimeInToolbar = null;

    public RetrofitController(IOnDownloadComplete onDownloadComplete, IPublishLastDownloadTimeInToolbar downloadTimeInToolbar) {
        this.onDownloadComplete = onDownloadComplete;
        this.downloadTimeInToolbar = downloadTimeInToolbar;
    }

    public void start() {
        Log.d(TAG, "start: start");
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URI).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = retrofitBuilder.build();
        Log.d(TAG, "start: retrofit ===> " + retrofit.baseUrl());
        IRetroApi iRetroApi = retrofit.create(IRetroApi.class);
        Call<RedditRespone> post = iRetroApi.loadChanges();
        post.enqueue(this);
        Log.d(TAG, "start: end");
    }

    @Override
    public void onResponse(Call<RedditRespone> call, Response<RedditRespone> response) {
        Log.d(TAG, "onResponse: start");

        if (response.isSuccessful()) {
            saveTheDownloadTime(); // save the download time of the response
            manageTheLastUpdate();
            if (onDownloadComplete != null) {
                saveTheDataInSharedPrefs(response.body().getData().getChildren());
                onDownloadComplete.onDownloadCompleteListener(response.code(), response.body().getData().getChildren());
            }
        } else {
            Log.e(TAG, "onResponse: error ===> " + response.errorBody() + " code ===> " + response.code());
        }
        Log.d(TAG, "onResponse: end");
    }

    private void saveTheDownloadTime() {
        setLastUpdateTimeInMilliSeconds(System.currentTimeMillis()); // set the current time field
        Context context = (Context) onDownloadComplete;
        SharedPreferences.Editor editor = context.getSharedPreferences(BuildConfig.LAST_DOWNLOAD_FILE_NAME, MODE_PRIVATE).edit();
        editor.putLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, getLastUpdateTimeInMilliSeconds());
        editor.apply();
    }

    private void manageTheLastUpdate() {
        Context context = (Context) onDownloadComplete;

        Log.d(TAG, "manageTheLastUpdate: Fetched milli seconds ===> " + getLastUpdateTimeInMilliSeconds());
        Log.d(TAG, "manageTheLastUpdate: Fetched milli seconds from shared prefs ===> " + getTheDownloadTimeFromSharedPrefs());
        Log.d(TAG, "manageTheLastUpdate: Current time - fetched time ===> " + (System.currentTimeMillis() - getLastUpdateTimeInMilliSeconds()));

        long minutes = getTimeDifferenceInMinutes();

        Log.d(TAG, "manageTheLastUpdate: Time in minutes ===> " + minutes);

        if (minutes >= 0 && minutes < 1) {
            downloadTimeInToolbar.setMessageToToolbar(R.string.update_message_less_then_minute);
            Log.d(TAG, "manageTheLastUpdate: less than a minute ago");
        } else {
            Log.d(TAG, "manageTheLastUpdate: In ELSE");
            downloadTimeInToolbar.setMessageToToolbar(String.format(context.getResources().getString(R.string.update_message_more_than_minute), minutes,
                    (minutes == 1 ? context.getResources().getString(R.string.minute) : context.getResources().getString(R.string.minutes))));
        }
    }

    private void saveTheDataInSharedPrefs(List<RedditPostWrapper> postWrapperList) {
        Context context = (Context) onDownloadComplete;
        SharedPreferences.Editor editor = context.getSharedPreferences(BuildConfig.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(BuildConfig.SIZE_OF_POST_LIST, postWrapperList.size());
        Gson gson = new Gson();
        for (int i = 0; i < postWrapperList.size(); i++) {
            editor.putString(Integer.toString(i), gson.toJson(postWrapperList.get(i)));
        }
        editor.apply();
    }


    @Override
    public void onFailure(Call<RedditRespone> call, Throwable t) {
        Log.d(TAG, "onFailure: start");
        t.printStackTrace();
        if (onDownloadComplete != null) {
            Util.displayErrorDialog((Context) onDownloadComplete, (((Context) onDownloadComplete).getResources().getString(R.string.no_internet_connection)), false);
            onDownloadComplete.onDownloadCompleteListener(BuildConfig.ERROR_RESPONSE_CODE, null);
        }
        Log.d(TAG, "onFailure: end");
    }


    public List<RedditPostWrapper> getCacheDataFromSharedPrefs(Context context) {
        Log.d(TAG, "getCacheDataFromSharedPrefs: start");
        Gson gson = new Gson();
        List<RedditPostWrapper> temp = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.SHARED_PREFS_NAME, MODE_PRIVATE);
        for (int i = 0; i < sharedPreferences.getInt(BuildConfig.SIZE_OF_POST_LIST, 0); i++)
            temp.add(gson.fromJson(sharedPreferences.getString(String.valueOf(i), ""), RedditPostWrapper.class));

        Log.d(TAG, "getCacheDataFromSharedPrefs: size of temp ---> " + temp.size());
        Log.d(TAG, "getCacheDataFromSharedPrefs: end");
        return temp;
    }


    private long getTheDownloadTimeFromSharedPrefs() {
        Context context = (Context) onDownloadComplete;
        setLastUpdateTimeInMilliSeconds(context.getSharedPreferences(BuildConfig.LAST_DOWNLOAD_FILE_NAME, MODE_PRIVATE).getLong(BuildConfig.LAST_DOWNLOAD_TIME_KEY, System.currentTimeMillis()));

        return getLastUpdateTimeInMilliSeconds();
    }


    private void setLastUpdateTimeInMilliSeconds(long lastUpdateTimeInMilliSeconds) {
        this.lastUpdateTimeInMilliSeconds = lastUpdateTimeInMilliSeconds;
    }

    private long getLastUpdateTimeInMilliSeconds() {
        return lastUpdateTimeInMilliSeconds;
    }


    public long getTimeDifferenceInMinutes() {
        long timeDifference = System.currentTimeMillis() - getTheDownloadTimeFromSharedPrefs();
        return TimeUnit.MILLISECONDS.toMinutes(timeDifference);
    }

    /*
    * An interface which sets the toolbar subtitle
     */
    public interface IPublishLastDownloadTimeInToolbar {
        void setMessageToToolbar(String message);

        void setMessageToToolbar(@StringRes int message);
    }

    /*
    * An interface which sends the download result back to the Caller who request the data in this case its MainActivity
     */

    public interface IOnDownloadComplete {
        /*
        * @Params responseCode is to check whether response is ok or not e.g server is up or not
        * @Params postList is to send the list of data which is downloaded and parse by the Retrofit to the Caller
         */
        void onDownloadCompleteListener(int responseCode, List<RedditPostWrapper> postsList);
    }
}