package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadNewsActivity extends AppCompatActivity
        implements ListView.OnItemClickListener{

    private ListView listView;
    private Button buttonBackToMenu;
    private String JSON_STRING;
    private Session session;
    private Bitmap image;
    private ImageView newsImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        listView = (ListView)findViewById(R.id.readListView);
        buttonBackToMenu = (Button)findViewById(R.id.buttonBackTOMenu);
        newsImageView = (ImageView)findViewById(R.id.readNewsImageView);
        listView.setOnItemClickListener(this);
        session = new Session(getApplicationContext());
        getJson();

        buttonBackToMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                HashMap<String,String> user = session.getUserDetails();
                String userId = user.get(Session.KEY_ID);
                //to judge the status of user
                //if it's admin
                if (userId.equals("9")){
                    Intent intent = new Intent(ReadNewsActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(ReadNewsActivity.this,MainActivityForUsers.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void getJson(){
        class GetJSON extends AsyncTask<Void,Void,String> {
           ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(ReadNewsActivity.this,"Fetching data","wait....",
                        false,false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showNews();
            }

            @Override
            protected String doInBackground(Void... params){
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendGetRequest(ConfigPhpAndroid.URL_GET_ALL);
                return s;
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void showNews(){
        JSONObject jsonObj = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        try{
            jsonObj = new JSONObject(JSON_STRING);
            JSONArray result = jsonObj.getJSONArray(ConfigPhpAndroid.TAG_JSON_ARRAY);

            for(int i = 0;i<result.length();i++){
                JSONObject jsonObject = result.getJSONObject(i);
                String title = jsonObject.getString(ConfigPhpAndroid.TAG_NEWS_TITLE);
                String newsId = jsonObject.getString(ConfigPhpAndroid.TAG_NEWS_ID);



                HashMap<String,String> news = new HashMap<>();
                news.put(ConfigPhpAndroid.TAG_NEWS_ID,newsId);
                news.put(ConfigPhpAndroid.TAG_NEWS_TITLE,title);
//                setImageById(newsId);
                list.add(news);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
//                ReadNewsActivity.this,list,R.layout.activity_read_news_list_item,
//                new String[]{ConfigPhpAndroid.TAG_NEWS_ID,ConfigPhpAndroid.TAG_NEWS_CONTENT},
//                new int[]{R.id.listNewsTitle,R.id.listNewsContent}
                ReadNewsActivity.this,list,R.layout.activity_read_news_list_item,
                new String[]{ConfigPhpAndroid.TAG_NEWS_TITLE,ConfigPhpAndroid.TAG_NEWS_ID},
                new int[]{R.id.listNewsTitle}
        );


        listView.setDividerHeight(10);
        listView.setAdapter(adapter);
    }

    public void setImageById(String id){
             String addUrl = ConfigPhpAndroid.URL_GET_IMAGE+id;
                URL url = null;
                try {
                    url = new URL(addUrl);
                    InputStream inputStream = url.openConnection().getInputStream();
                    image = BitmapFactory.decodeStream(inputStream);
                    newsImageView.setImageBitmap(image);
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch( IOException e){
                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }

    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        HashMap<String,String> user = session.getUserDetails();
        String userId = user.get(Session.KEY_ID);
        if (userId.equals("9")){
            Intent intent = new Intent(this,ShowNewsForAdmin.class);
            HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
            String newsId = map.get(ConfigPhpAndroid.TAG_NEWS_ID).toString();
            intent.putExtra(ConfigPhpAndroid.NEWS_ID_INTENT,newsId);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,ShowNewsForUser.class);
            HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
            String newsId = map.get(ConfigPhpAndroid.TAG_NEWS_ID).toString();
            intent.putExtra(ConfigPhpAndroid.NEWS_ID_INTENT,newsId);
            startActivity(intent);
        }
    }
}
