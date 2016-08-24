package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadNewsActivity extends AppCompatActivity
        implements ListView.OnItemClickListener{

    private ListView listView;
    private Button buttonBackToMenu;
    private String JSON_STRING;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        listView = (ListView)findViewById(R.id.readListView);
        buttonBackToMenu = (Button)findViewById(R.id.buttonBackTOMenu);
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
                news.put(ConfigPhpAndroid.TAG_NEWS_CONTENT,title);
                list.add(news);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ReadNewsActivity.this,list,R.layout.activity_read_news_list_item,
                new String[]{ConfigPhpAndroid.TAG_NEWS_ID,ConfigPhpAndroid.TAG_NEWS_CONTENT},
                new int[]{R.id.listNewsTitle,R.id.listNewsContent}
        );

        listView.setAdapter(adapter);
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
