package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowNewsForUser extends AppCompatActivity {

    private String newsId;
    private EditText editTextNewsPubDate;
    private EditText editTextNewsTitle;
    private EditText editTextNewsContent;
    private ImageView imageView;
    Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_for_user);

        Intent intent = getIntent();

        newsId = intent.getStringExtra(ConfigPhpAndroid.NEWS_ID_INTENT);

        editTextNewsPubDate= (EditText)findViewById(R.id.editTextNewsPubDate);
        editTextNewsTitle = (EditText)findViewById(R.id.editTextNewsTitle);
        editTextNewsContent = (EditText)findViewById(R.id.editTextNewsContent);
        imageView = (ImageView)findViewById(R.id.showImage);

        getNews();
    }

    private void getNews(){
        class GetNews extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected  void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ShowNewsForUser.this,"Fetching...","wait...",false,false);
            }

            @Override
            protected  void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                showNewsInDetail(s);
            }

            @Override
            protected String doInBackground(Void... params){
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendGetRequestParam(ConfigPhpAndroid.URL_GET_NEWS,newsId);
                return s;
            }
        }
        GetNews getNews = new GetNews();
        getNews.execute();
    }

    private void showNewsInDetail(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(ConfigPhpAndroid.TAG_JSON_ARRAY);
            JSONObject jobj = result.getJSONObject(0);
            String title = jobj.getString(ConfigPhpAndroid.TAG_NEWS_TITLE);
            String content = jobj.getString(ConfigPhpAndroid.TAG_NEWS_CONTENT);
            String pubDate = jobj.getString(ConfigPhpAndroid.TAG_NEWS_PUBDATE);
            String stringImage = jobj.getString(ConfigPhpAndroid.TAG_NEWS_IMAGE);
            getImage();
//            Bitmap bitmap = BitmapFactory.
            editTextNewsPubDate.setText(pubDate);
            editTextNewsTitle.setText(title);
            editTextNewsContent.setText(content);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private  void getImage(){
        class GetImage extends AsyncTask<String,Void,Bitmap>{
            ProgressDialog progressDialog;

            @Override
            protected  void onPreExecute(){
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ShowNewsForUser.this,"Downloading....",null,true,true);
            }
            @Override
            protected void onPostExecute(Bitmap image){
                super.onPostExecute(image);
                progressDialog.dismiss();
                imageView.setImageBitmap(image);
            }
            @Override
            protected  Bitmap doInBackground(String... params){
                String newsId = params[0];
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
        }
        GetImage getImage = new GetImage();
        getImage.execute(newsId);
    }
}

