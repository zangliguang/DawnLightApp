package com.liguang.dawnlightapp.constants;

import android.os.Environment;

/**
 * Created by baidu on 15/10/19.
 */
public class LocalConstants {
    public static  boolean DEBUGE =true;
    public static  String appname ="DawnLightApp";
    public static  String dbname ="dawnlight.db";

    public static final class Paths {
        public static final String LocalYoutubeVideoCategory = "VideoCategories.json";
        public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        public static final String DB_PATH =SDCARD_PATH+"/"+dbname;
//        public static final String INNER_PATH = DawnLightApplication.getInstance().getFilesDir().getAbsolutePath();
        public static final String IMAGE_LOADER_CACHE_PATH = "/DawnLightApp/Images/";
    }
    public static final class Params {
        public static final String LocalYoutubeVideoCommonItems = "items";
        public static final String LocalYoutubeVideoCommonSnippet = "snippet";
        public static final String LocalYoutubeVideoNextPageToken = "nextPageToken";
    }
}
