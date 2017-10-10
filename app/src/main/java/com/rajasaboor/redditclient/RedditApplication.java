package com.rajasaboor.redditclient;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by rajaSaboor on 10/9/2017.
 */

public class RedditApplication extends Application {
    private Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus(ThreadEnforcer.MAIN);
    }

    public Bus getBus() {
        return this.bus;
    }
}
