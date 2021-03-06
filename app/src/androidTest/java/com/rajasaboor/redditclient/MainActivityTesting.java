package com.rajasaboor.redditclient;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.retrofit.RetrofitController;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static org.junit.Assert.*;


import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajaSaboor on 8/17/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTesting {
    private static final String TAG = MainActivityTesting.class.getSimpleName();
    private static final List<RedditPostWrapper> MOCK_DATA = new ArrayList<>();
    private RetrofitController controller;


    static {
        Log.d(TAG, "static initializer: start");
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("1st Test Title", "1st Author", "1st Comments Link", "1st Post URL", true, 30, 150, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("2nd Test Title", "2nd Author", "2nd Comments Link", "2nd Post URL", true, 984, 550, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("3rd Test Title", "3rd Author", "3rd Comments Link", "3rd Post URL", true, 65486, 692, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("4th Test Title", "4th Author", "4th Comments Link", "4th Post URL", true, 654964, 770, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("5th Test Title", "5th Author", "5th Comments Link", "5th Post URL", true, 9848, 987, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("6th Test Title", "6th Author", "6th Comments Link", "6th Post URL", true, 978, 10069, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("7th Test Title", "7th Author", "7th Comments Link", "7th Post URL", true, 9898, 78965, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("8th Test Title", "8th Author", "8th Comments Link", "8th Post URL", true, 9864, 5696, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("9th Test Title", "9th Author", "9th Comments Link", "9th Post URL", true, 98654, 986, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("10th Test Title", "10th Author", "10th Comments Link", "10th Post URL", true, 99365, 9856, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("11th Test Title", "11th Author", "11th Comments Link", "11th Post URL", true, 6969, 98546, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("12th Test Title", "12th Author", "12th Comments Link", "12th Post URL", true, 222, 31564, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("13th Test Title", "13th Author", "13th Comments Link", "13th Post URL", true, 1111, 32164, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("14th Test Title", "14th Author", "14th Comments Link", "14th Post URL", true, 2585, 6698, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("15th Test Title", "15th Author", "15th Comments Link", "15th Post URL", true, 2654, 15698, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("16th Test Title", "16th Author", "16th Comments Link", "16th Post URL", true, 7841, 1123, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("17th Test Title", "17th Author", "17th Comments Link", "17th Post URL", true, 9875, 99658, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("18th Test Title", "18th Author", "18th Comments Link", "18th Post URL", true, 15256, 89545, false)));
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("19th Test Title", "19th Author", "19th Comments Link", "19th Post URL", true, 3652, 77585, false)));
        Log.d(TAG, "static initializer: end");
    }

    @Rule
    public ActivityTestRule<MainActivity> myRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);
    private MainActivity mainActivity = null;

    @Before
    public void beforeMethod() {
        Log.d(TAG, "beforeMethod: Start");
        mainActivity = myRule.getActivity();
        callingTheCallBackToSetTheMockData(); // calling this method to save the data in main activity list
        controller = new RetrofitController(mainActivity, mainActivity);
        controller.saveTheDataInSharedPrefs(mainActivity.getPostWrapperList()); // saving the mock data in cache
        Log.d(TAG, "beforeMethod: end");
    }

    private void callingTheCallBackToSetTheMockData() {
        Log.d(TAG, "callingTheCallBackToSetTheMockData: start");
        try {
            myRule.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mainActivity.onDownloadCompleteListener(200, MainActivityTesting.MOCK_DATA);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        Log.d(TAG, "callingTheCallBackToSetTheMockData: end");
    }

    @Test
    public void matchThePostTitle() {
//        onView(withId(R.id.posts_recycler_view))
//                .check(matches(hasDescendant(withText("2nd Test Title"))));

        //onView(withId(R.id.posts_recycler_view)).check(isDisplayed());

// Check item at position 0 has "Some content"
        onView(withRecyclerView(R.id.posts_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText("1st Test Title"))));
    }


    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }


    @Test
    public void noInternetConnectionAndNoSavedData() {
        Log.d(TAG, "noInternetConnectionAndNoSavedData: start");
        controller.removeTheCacheData(mainActivity);
        Log.d(TAG, "noInternetConnectionAndNoSavedData: Cache Size ===> " + controller.getCacheDataFromSharedPrefs(mainActivity).size());
        assertFalse(controller.getCacheDataFromSharedPrefs(mainActivity).size() != 0);
        Log.d(TAG, "noInternetConnectionAndNoSavedData: end");
    }


    @Test
    public void clickOnPositionAtRecyclerView() {
        Log.d(TAG, "clickOnPositionAtRecyclerView: start");
        //onView(withId(R.id.posts_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Click item at position 3
        onView(withId(R.id.posts_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Log.d(TAG, "clickOnPositionAtRecyclerView: end");
    }

    @Test
    public void validateDataWithRotation() {
        rotateDeviceToLandscape();
        onView(withId(R.id.posts_recycler_view)).perform(RecyclerViewActions.scrollToPosition(18));
        onView(withRecyclerView(R.id.posts_recycler_view).atPosition(18))
                .check(matches(hasDescendant(withText("19th Test Title"))));
        rotateDeviceToPortrait();
        onView(withId(R.id.posts_recycler_view)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withRecyclerView(R.id.posts_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText("1st Test Title"))));
    }

    private void rotateDeviceToLandscape() {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void rotateDeviceToPortrait() {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @After
    public void afterMethod() {
        Log.d(TAG, "afterMethod: start");
        Log.d(TAG, "afterMethod: end");
    }
}





