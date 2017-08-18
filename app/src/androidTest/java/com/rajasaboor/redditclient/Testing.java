package com.rajasaboor.redditclient;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Looper;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;

import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.junit.Assert.*;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajaSaboor on 8/17/2017.
 */
@RunWith(AndroidJUnit4.class)
public class Testing {
    private static final String TAG = Testing.class.getSimpleName();
    static List<RedditPostWrapper> mockData = new ArrayList<>();

    static {
        mockData.add(new RedditPostWrapper(new RedditPost("1st Test Title", "1st Author", "1st Comments Link", "1st Post URL", true, 30, 150, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("2nd Test Title", "2nd Author", "2nd Comments Link", "2nd Post URL", true, 984, 550, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("3rd Test Title", "3rd Author", "3rd Comments Link", "3rd Post URL", true, 65486, 692, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("4th Test Title", "4th Author", "4th Comments Link", "4th Post URL", true, 654964, 770, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("5th Test Title", "5th Author", "5th Comments Link", "5th Post URL", true, 9848, 987, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("6th Test Title", "6th Author", "6th Comments Link", "6th Post URL", true, 978, 10069, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("7th Test Title", "7th Author", "7th Comments Link", "7th Post URL", true, 9898, 78965, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("8th Test Title", "8th Author", "8th Comments Link", "8th Post URL", true, 9864, 5696, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("9th Test Title", "9th Author", "9th Comments Link", "9th Post URL", true, 98654, 986, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("10th Test Title", "10th Author", "10th Comments Link", "10th Post URL", true, 99365, 9856, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("11th Test Title", "11th Author", "11th Comments Link", "11th Post URL", true, 6969, 98546, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("12th Test Title", "12th Author", "12th Comments Link", "12th Post URL", true, 222, 31564, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("13th Test Title", "13th Author", "13th Comments Link", "13th Post URL", true, 1111, 32164, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("14th Test Title", "14th Author", "14th Comments Link", "14th Post URL", true, 2585, 6698, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("15th Test Title", "15th Author", "15th Comments Link", "15th Post URL", true, 2654, 15698, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("16th Test Title", "16th Author", "16th Comments Link", "16th Post URL", true, 7841, 1123, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("17th Test Title", "17th Author", "17th Comments Link", "17th Post URL", true, 9875, 99658, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("18th Test Title", "18th Author", "18th Comments Link", "18th Post URL", true, 15256, 89545, false)));
        mockData.add(new RedditPostWrapper(new RedditPost("19th Test Title", "19th Author", "19th Comments Link", "19th Post URL", true, 3652, 77585, false)));
    }

    @Rule
    public ActivityTestRule<MainActivity> myRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);
    private MainActivity mainActivity = null;

    @Before
    public void beforeMethod() {
        Log.d(TAG, "beforeMethod: Start");
        mainActivity = myRule.getActivity();
        Log.d(TAG, "beforeMethod: end");
    }

    //            @Test
    public void testMethod() throws Throwable {
        Log.d(TAG, "testMethod: start");
        assertNotNull(mainActivity);

        // when
        myRule.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mainActivity.onDownloadCompleteListener(200, Testing.mockData);
            }
        });

        Thread.sleep(5000);

        onView(withId(R.id.posts_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(mainActivity.getPostWrapperList().get(0).getData().getPostTitle())), click()));
        Thread.sleep(5000);
        // Then.
        assertEquals("hello", "hello");

        Log.d(TAG, "testMethod: Obj at 0 position " + mainActivity.getPostWrapperList().get(0).getData().getPostTitle());
        Log.d(TAG, "testMethod: Size of adapter ===> " + mainActivity.getPostWrapperList().size());
        Log.d(TAG, "testMethod: end");
    }

    //    @Test
    public void noInternetAndNoSavedData() {
        Log.d(TAG, "noInternetAndNoSavedData: start");
        Log.d(TAG, "noInternetAndNoSavedData: Size ===> " + mainActivity.getPostListFromSharedPrefs().size());
        assertTrue(mainActivity.getPostListFromSharedPrefs().size() >= 1);
        Log.d(TAG, "noInternetAndNoSavedData: end");
    }

    @Test
    public void rotationTest() {
        try {
            // Put on sleep the current thread to get to know the current state of the view
            Thread.sleep(3000);

            // changing the orientation
            myRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            // moving the scroll to the bottom
            onView(withId(R.id.posts_recycler_view)).perform(RecyclerViewActions.scrollToPosition(mainActivity.getPostListFromSharedPrefs().size() - 1));

            // Put on sleep a thread to see the orientation and the scroll changes
            Thread.sleep(3000);

            // changing the orientation
            myRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // moving the scroll to the bottom
            onView(withId(R.id.posts_recycler_view)).perform(RecyclerViewActions.scrollToPosition(mainActivity.getPostListFromSharedPrefs().size() - 1));
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void afterMethod() {
        Log.d(TAG, "afterMethod: start");

        Log.d(TAG, "afterMethod: end");
    }
}
