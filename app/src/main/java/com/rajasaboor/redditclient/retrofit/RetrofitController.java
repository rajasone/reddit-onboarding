package com.rajasaboor.redditclient.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rajasaboor.redditclient.model.RedditRespone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by default on 8/2/2017.
 */


interface IRetroApi {
    @GET(".json")
    Call<RedditRespone> loadChanges();
}

public class RetrofitController implements Callback<RedditRespone> {
    private static final String TAG = RetrofitController.class.getSimpleName();
    public static final String BASE_URI = "https://www.reddit.com/";

    public void start() {
        Log.d(TAG, "start: start");
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URI).addConverterFactory(GsonConverterFactory.create(gson));
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
            Log.d(TAG, "onResponse: successful size of list is ==> " + (response.body() != null ? response.body().getData().getChildren().size() : " null"));
        } else {
            Log.e(TAG, "onResponse: error ===> " + response.errorBody() + " code ===> " + response.code());
        }
        Log.d(TAG, "onResponse: end");
    }

    @Override
    public void onFailure(Call<RedditRespone> call, Throwable t) {
        Log.d(TAG, "onFailure: start");
        t.printStackTrace();
        Log.d(TAG, "onFailure: end");
    }
}