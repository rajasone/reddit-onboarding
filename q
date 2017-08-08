[1mdiff --git a/app/src/main/java/com/rajasaboor/redditclient/MainActivity.java b/app/src/main/java/com/rajasaboor/redditclient/MainActivity.java[m
[1mindex e761fc0..48198c1 100644[m
[1m--- a/app/src/main/java/com/rajasaboor/redditclient/MainActivity.java[m
[1m+++ b/app/src/main/java/com/rajasaboor/redditclient/MainActivity.java[m
[36m@@ -253,9 +253,6 @@[m [mpublic class MainActivity extends AppCompatActivity implements RetrofitControlle[m
             case Consts.RESPONSE_CODE_OK:[m
                 Log.d(TAG, "onDownloadCompleteListener: Response code is 200 now updating the Adapter");[m
                 Log.d(TAG, "onDownloadCompleteListener: Download data at ===> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US).format(new Date(System.currentTimeMillis())));[m
[31m-//                setLastUpdateTimeInMilliSeconds(System.currentTimeMillis()); // set the current time when the list is updated[m
[31m-//[m
[31m-//                addDownloadTimeToSharedPrefs();[m
 [m
                 setPostWrapperList(postsList); // setting the List field of the MainActivity[m
                 itemsAdapter.updateAdapter(postWrapperList); // sending the actual data which is downloaded and parsed by the Retrofit[m
[1mdiff --git a/app/src/main/res/layout/activity_detail.xml b/app/src/main/res/layout/activity_detail.xml[m
[1mindex a392d02..517fcc0 100644[m
[1m--- a/app/src/main/res/layout/activity_detail.xml[m
[1m+++ b/app/src/main/res/layout/activity_detail.xml[m
[36m@@ -1,5 +1,6 @@[m
 <?xml version="1.0" encoding="utf-8"?>[m
 <android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"[m
[32m+[m[32m    xmlns:app="http://schemas.android.com/apk/res-auto"[m
     android:layout_width="match_parent"[m
     android:layout_height="match_parent">[m
 [m
[36m@@ -15,7 +16,8 @@[m
         <android.support.design.widget.TabLayout[m
             android:id="@+id/appbar_parent_tab"[m
             android:layout_width="match_parent"[m
[31m-            android:layout_height="wrap_content">[m
[32m+[m[32m            android:layout_height="wrap_content"[m
[32m+[m[32m            app:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">[m
 [m
             <android.support.design.widget.TabItem[m
                 android:id="@+id/post_tab"[m
