package com.leicesterCampus.registrationandlogin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{
    Context context;
    private Bitmap image;
    String  newsId;

    public CustomAdapter(ReadNewsActivity readNewsActivity, ArrayList arrayList, String newsId){
        this.newsId = newsId;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        NewsHolder newsHolder= new NewsHolder();
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.activity_read_news_list_item,null);
            TextView newsTitleTextView = (TextView)view.findViewById(R.id.listNewsTitle);
            TextView newsContentTextView = (TextView)view.findViewById(R.id.listNewsContent);
            ImageView newsImage = (ImageView)view.findViewById(R.id.readNewsImageView);
            newsHolder.imageView = newsImage;
            newsHolder.newsContent = newsContentTextView;
            newsHolder.newsTitle = newsTitleTextView;
            view.setTag(newsHolder);
        }else{
            newsHolder = (NewsHolder)view.getTag();
            TextView newsTitleTextView = (TextView)view.findViewById(R.id.listNewsTitle);
            TextView newsContentTextView = (TextView)view.findViewById(R.id.listNewsContent);
            ImageView newsImage = (ImageView)view.findViewById(R.id.readNewsImageView);
            newsHolder.imageView.setImageBitmap(getImageById(newsId));
            newsHolder.newsContent = newsContentTextView;
            newsHolder.newsTitle = newsTitleTextView;
        }
        return null;
    }
    public Bitmap getImageById(String newsId){
        String addUrl = ConfigPhpAndroid.URL_GET_IMAGE+newsId;
    URL url = null;
    try {
        url = new URL(addUrl);
        InputStream inputStream = url.openConnection().getInputStream();
        image = BitmapFactory.decodeStream(inputStream);
    }catch (MalformedURLException e){
        e.printStackTrace();
    }catch( IOException e){
        e.printStackTrace();
    }catch(Exception e){
        e.printStackTrace();
    }
        return image;
    }

    public class NewsHolder{
        public TextView newsTitle;
        public TextView newsContent;
        public ImageView imageView;
    }
}
