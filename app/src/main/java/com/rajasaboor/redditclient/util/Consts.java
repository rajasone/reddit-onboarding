package com.rajasaboor.redditclient.util;

/**
 * Created by default on 8/3/2017.
 * This class contains all the constants fields in it e.g base_uri, response code etc
 */

public class Consts {
    public static final int RESPONSE_CODE_OK = 200;
    public static final String BASE_URI = "https://www.reddit.com/";
    public static final String SHARED_PREFS_NAME = "post_lists";
    public static final String SIZE_OF_POST_LIST = "list_size";
    public static final String KEY_TO_CHECK_DATA = "1";
    public static final String[] TABS_NAMES = {"Posts", "Comments"};
    public static final String INDIVIDUAL_POST_ITEM_KEY = "post_item";
    public static final String POST_LOADING_STATUS = "post_loading";
    public static final String COMMENTS_KEY = "comments_key";
    public static final String COMMENTS_PAGE_PROGRESS = "comments_progress";
}
