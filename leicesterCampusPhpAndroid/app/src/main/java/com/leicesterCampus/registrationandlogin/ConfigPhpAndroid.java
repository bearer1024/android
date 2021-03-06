package com.leicesterCampus.registrationandlogin;

public class ConfigPhpAndroid {
    public static String SERVER_IP = "http://192.168.0.11";
    public static String INDEX_URL = SERVER_IP+"/leicesterCampus/index.php";
    public static final String URL_GET_ALL_NEWS = SERVER_IP+"/leicesterCampus/getAllNews.php";
    public static final String URL_GET_NEWS= SERVER_IP+"/leicesterCampus/getNews.php?newsId=";
    public static final String URL_UPDATE_NEWS= SERVER_IP+"/leicesterCampus/updateNews.php";
    public static final String URL_DELETE_NEWS= SERVER_IP+"/leicesterCampus/deleteNews.php?newsId=";
    public static final String URL_IMAGE_PREFIX = SERVER_IP+"/leicesterCampus/";
    public static final String URL_GET_IMAGE_URL = URL_IMAGE_PREFIX+"image/";

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
    public static final String TAG_NEWS_PUBDATE= "pubDate";

    //newsId to pass with intent
    public static final String NEWS_ID_INTENT = "newsIdIntent";
}
