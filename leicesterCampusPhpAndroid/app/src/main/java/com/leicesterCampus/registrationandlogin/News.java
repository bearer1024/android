package com.leicesterCampus.registrationandlogin;

public class News {
    private String title;
    private String thumbNailUrl;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    private String newsId;

    public News(){

    }
    public News(String title, String thumbNailUrl) {
        this.title = title;
        this.thumbNailUrl = thumbNailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }
}
