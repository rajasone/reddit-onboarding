package com.rajasaboor.redditclient.retrofit;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rajasaboor.redditclient.R;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.model.RedditRespone;
import com.rajasaboor.redditclient.util.Consts;
import com.rajasaboor.redditclient.util.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by default on 8/2/2017.
 * A connection class which connects to the internet and download the raw data from the {@link Consts.BASE_URI} and when the download is complete without any exception
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
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(Consts.BASE_URI).addConverterFactory(GsonConverterFactory.create(gson));
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
                onDownloadComplete.onDownloadCompleteListener(response.code(), response.body().getData().getChildren());
            }
        } else {
            Log.e(TAG, "onResponse: error ===> " + response.errorBody() + " code ===> " + response.code());
        }
        Log.d(TAG, "onResponse: end");
    }

    @Override
    public void onFailure(Call<RedditRespone> call, Throwable t) {
        Log.d(TAG, "onFailure: start");
        t.printStackTrace();
        if (onDownloadComplete != null) {
            Util.displayErrorDialog((Context) onDownloadComplete, (((Context) onDownloadComplete).getResources().getString(R.string.no_internet_connection)), false);
        }
        Log.d(TAG, "onFailure: end");
    }

    public IOnDownloadComplete getOnDownloadComplete() {
        return onDownloadComplete;
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