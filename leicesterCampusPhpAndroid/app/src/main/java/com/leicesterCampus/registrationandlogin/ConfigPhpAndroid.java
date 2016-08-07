package com.leicesterCampus.registrationandlogin;

/**
 * Created by bearer1024 on 7/31/16.
 */
public class ConfigPhpAndroid {
    public static final String URL_GET_ALL = "http://192.168.0.13/leicesterCampus/getAllNews.php";
    public static final String URL_GET_NEWS= "http://192.168.0.13/leicesterCampus/getNews.php?newsId=";
    public static final String URL_UPDATE_NEWS= "http://192.168.0.13/leicesterCampus/updateNews.php";
    public static final String URL_DELETE_NEWS= "http://192.168.0.13/leicesterCampus/deleteNews.php?newsId=";
    public static final String URL_GET_IMAGE= "http://192.168.0.13/leicesterCampus/getImage.php?newsId=";

//    public static final String URL_GET_ALL = "http://127.0.0.1/leicesterCampus/getAllNews.php";
//    public static final String URL_GET_NEWS= "http://127.0.0.1/leicesterCampus/getNews.php?newsId=";
//    public static final String URL_UPDATE_NEWS= "http://127.0.0.1/leicesterCampus/updateNews.php";
//    public static final String URL_DELETE_NEWS= "http://127.0.0.1/leicesterCampus/deleteNews.php?newsId=";

    //keys that will be used to send the request to php scrips
    public static final String KEY_NEWS_ID = "newsId";
    public static final String KEY_NEWS_TITLE= "title";
    public static final String KEY_NEWS_CONTENT= "content";

    //JSON tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_NEWS_ID= "newsId";
    public static final String TAG_NEWS_TITLE="title";
    public static final String TAG_NEWS_CONTENT= "content";
    public static final String TAG_NEWS_IMAGE= "image";

    //newsId to pass with intent
    public static final String NEWS_ID_INTENT = "newsIdIntent";
}
