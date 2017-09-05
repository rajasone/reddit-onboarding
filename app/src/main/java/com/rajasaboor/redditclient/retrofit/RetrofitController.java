package com.rajasaboor.redditclient.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rajasaboor.redditclient.BuildConfig;
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
    private final IOnDownloadComplete onDownloadComplete; // An interface instance to call the listener method

    public RetrofitController(IOnDownloadComplete onDownloadComplete) {
        this.onDownloadComplete = onDownloadComplete;
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
            if (onDownloadComplete != null) {
                onDownloadComplete.onDownloadCompleteListener(BuildConfig.OK_RESPONSE_CODE, response.body().getData().getChildren());
            }
        } else {
            Log.e(TAG, "onResponse: error ===> " + response.errorBody() + " code ===> " + response.code());
        }
        Log.d(TAG, "onResponse: end");
    }

    public void saveTheDataInSharedPrefs(List<RedditPostWrapper> postWrapperList, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
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
            onDownloadComplete.onDownloadCompleteListener(BuildConfig.ERROR_RESPONSE_CODE, null);
        }
        Log.d(TAG, "onFailure: end");
    }


    public List<RedditPostWrapper> getCacheDataFromSharedPrefs(SharedPreferences preferences) {
        Log.d(TAG, "getCacheDataFromSharedPrefs: start");
        Gson gson = new Gson();
        List<RedditPostWrapper> temp = new ArrayList<>();

        for (int i = 0; i < preferences.getInt(BuildConfig.SIZE_OF_POST_LIST, 0); i++)
            temp.add(gson.fromJson(preferences.getString(String.valueOf(i), ""), RedditPostWrapper.class));

        Log.d(TAG, "getCacheDataFromSharedPrefs: size of temp ---> " + temp.size());
        Log.d(TAG, "getCacheDataFromSharedPrefs: end");
        return temp;
    }

    public void removeTheCacheData(SharedPreferences preferences) {
        int size = getCacheDataFromSharedPrefs(preferences).size();
        for (int i = 0; i < size; i++)
            preferences.edit().remove(String.valueOf(i)).apply();
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