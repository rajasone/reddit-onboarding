package com.rajasaboor.redditclient;

import android.content.pm.ActivityInfo;
import android.util.Log;

import com.rajasaboor.redditclient.model.RedditPost;
import com.rajasaboor.redditclient.model.RedditPostWrapper;
import com.rajasaboor.redditclient.retrofit.RetrofitController;
import com.rajasaboor.redditclient.view_recycler.ViewActivity;
import com.schibsted.spain.barista.BaristaAssertions;
import com.schibsted.spain.barista.BaristaRecyclerViewActions;
import com.schibsted.spain.barista.BaristaRule;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.schibsted.spain.barista.BaristaSleepActions.sleep;

/**
 * Created by rajaSaboor on 8/28/2017.
 */

public class BaristaTesting {
    private static final String TAG = BaristaTesting.class.getSimpleName();
    @Rule
    public BaristaRule<ViewActivity> baristaRule = BaristaRule.create(ViewActivity.class);
    private ViewActivity viewActivity = null;
    private static final List<RedditPostWrapper> MOCK_DATA = new ArrayList<>();
    private RetrofitController controller;

    static {
        Log.d(TAG, "static initializer: start");
        MOCK_DATA.add(new RedditPostWrapper(new RedditPost("1st Test Title", "1st Author", "1st Comments Link", "1st Post URL", false, 30, 150, false)));
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

    /*

    @Before
    public void startUp() {
        Log.d(TAG, "startUp: start");
        baristaRule.launchActivity();
        viewActivity = baristaRule.getActivityTestRule().getActivity();
        callingTheCallBackToSetTheMockData();
        controller = new RetrofitController(viewActivity, viewActivity);
        controller.saveTheDataInSharedPrefs(BaristaTesting.MOCK_DATA); // saving the mock data in cache
        Log.d(TAG, "startUp: end");
    }

    private void callingTheCallBackToSetTheMockData() {
        Log.d(TAG, "callingTheCallBackToSetTheMockData: start");
        try {
            viewActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    viewActivity.onDownloadCompleteListener(200, BaristaTesting.MOCK_DATA);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        Log.d(TAG, "callingTheCallBackToSetTheMockData: end");
    }

    @Test
    public void validateTheRecyclerViewData() {
        Log.d(TAG, "validateTheRecyclerViewData: start");
        BaristaAssertions.assertDisplayed("1st Test Title");
        Log.d(TAG, "validateTheRecyclerViewData: end");
    }


    @Test
    public void noInternetAndNoSavedData() {
//        controller.removeTheCacheData(viewActivity);
        Log.d(TAG, "noInternetAndNoSavedData: Size of shared PRefs ===> " + controller.getCacheDataFromSharedPrefs(viewActivity).size());
        Assert.assertFalse(controller.getCacheDataFromSharedPrefs(viewActivity).size() > 0);
    }

    @Test
    public void clickOnPostAndOpenTheWebView() {
        BaristaRecyclerViewActions.clickRecyclerViewItem(R.id.posts_recycler_view, 0);
    }


    @Test
    public void changeOrientationAndValidateTitle() {
        changeOrientationToLandscape();
        BaristaAssertions.assertDisplayed("1st Test Title");
        changeOrientationToThePortrait();
        BaristaAssertions.assertDisplayed("1st Test Title");
    }

    private void changeOrientationToThePortrait() {
        viewActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void changeOrientationToLandscape() {
        viewActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    //    @Test
    public void shareTest() {
//        BaristaRecyclerViewActions.clickRecyclerViewItem(R.id.posts_recycler_view, 0);
//        BaristaMenuClickActions.clickMenu(R.id.share_menu);

        onView(withId(R.id.share_menu)).perform(click());
    }

    @Test
    public void switchingBetweenTabs() {
        BaristaRecyclerViewActions.clickRecyclerViewItem(R.id.posts_recycler_view, 0);
//        BaristaViewPagerActions.swipeViewPagerForward();
//        BaristaViewPagerActions.swipeViewPagerBack();

        // using the EXPRESSO method
        onView(withId(R.id.view_pager)).perform(swipeLeft());
        onView(withId(R.id.view_pager)).perform(swipeRight());

    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown: start");
        viewActivity.setPostWrapperList(null);
        Log.d(TAG, "tearDown: end");
    }
    */
}
