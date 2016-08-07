package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ShowNews extends AppCompatActivity implements View.OnClickListener{

    private String newsId;
    private EditText editTextNewsId;
    private EditText editTextNewsTitle;
    private EditText editTextNewsContent;
    private Button buttonUpdate;
    private Button buttonDelete;
    private ImageView imageView;
    Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        Intent intent = getIntent();

        newsId = intent.getStringExtra(ConfigPhpAndroid.NEWS_ID_INTENT);

        editTextNewsId = (EditText)findViewById(R.id.editTextNewsId);
        editTextNewsTitle = (EditText)findViewById(R.id.editTextNewsTitle);
        editTextNewsContent = (EditText)findViewById(R.id.editTextNewsContent);
        imageView = (ImageView)findViewById(R.id.showImage);
        buttonDelete = (Button)findViewById(R.id.buttonDelete);
        buttonUpdate = (Button)findViewById(R.id.buttonUpdate);


        buttonDelete.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);

        editTextNewsId.setText(newsId);

        getNews();

    }

    private void getNews(){
        class GetNews extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
                    protected  void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ShowNews.this,"Fetching...","wait...",false,false);
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
            String stringImage = jobj.getString(ConfigPhpAndroid.TAG_NEWS_IMAGE);
            getImage();
//            Bitmap bitmap = BitmapFactory.

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
                progressDialog = ProgressDialog.show(ShowNews.this,"Downloading....",null,true,true);
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

    private void updateNews(){
        final String title = editTextNewsTitle.getText().toString().trim();
        final String content = editTextNewsContent.getText().toString().trim();

        class UpdateNews extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ShowNews.this,"Updating...","wait",false,false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ShowNews.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void...params){
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(ConfigPhpAndroid.KEY_NEWS_ID,newsId);
                hashMap.put(ConfigPhpAndroid.KEY_NEWS_TITLE,title);
                hashMap.put(ConfigPhpAndroid.KEY_NEWS_CONTENT,content);

                RequestHandler requestHandler = new RequestHandler();

                String s = requestHandler.sendPostRequest(ConfigPhpAndroid.URL_UPDATE_NEWS,hashMap);
                return s;
            }
        }

        UpdateNews updateNews = new UpdateNews();
        updateNews.execute();
    }

    private void deleteNews(){
        class DeleteNews extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ShowNews.this,"deleting....","wait",false,false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ShowNews.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void...params){
                RequestHandler requestHandler = new RequestHandler();

                String s = requestHandler.sendGetRequestParam(ConfigPhpAndroid.URL_DELETE_NEWS,newsId);
                return s;
            }
        }

        DeleteNews deleteNews = new DeleteNews();
        deleteNews.execute();
    }

    private void confirmDeleteNews(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this news");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNews();
                        startActivity(new Intent(ShowNews.this,ReadNewsActivity.class));
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onClick(View v){
        if(v==buttonUpdate){
            updateNews();
        }

        if(v==buttonDelete){
            confirmDeleteNews();
        }
    }
}
