package com.leicesterCampus.registrationandlogin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter{
    Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private Bitmap image;
    String  newsId;
    private List<News> newsList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomAdapter(Activity activity,List<News> newsList){

        this.activity = activity;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_read_news_list_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.readNewsImageView);
            TextView newsTitleTextView = (TextView)convertView.findViewById(R.id.listNewsTitle);
            TextView newsContentTextView = (TextView)convertView.findViewById(R.id.listNewsContent);
        //get news data from row
        News news = newsList.get(position);

        //thumbNailImage
        thumbNail.setImageUrl(news.getThumbNailUrl(),imageLoader);

        //title
        newsTitleTextView.setText(news.getTitle());

        return convertView;
    }

}
