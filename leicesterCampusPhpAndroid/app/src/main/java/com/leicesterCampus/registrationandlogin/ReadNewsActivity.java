package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadNewsActivity extends AppCompatActivity
        implements ListView.OnItemClickListener {

    private ListView listView;
    private Button buttonBackToMenu;
    private String JSON_STRING;
    private Session session;
    private CustomAdapter customAdapter;
    private List<News> newsList = new ArrayList<>();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        listView = (ListView) findViewById(R.id.readListView);
        buttonBackToMenu = (Button) findViewById(R.id.buttonBackTOMenu);
        listView.setOnItemClickListener(this);
        session = new Session(getApplicationContext());
        getJson();
        buttonBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> user = session.getUserDetails();
                String userId = user.get(Session.KEY_ID);
                //to judge the status of user
                //if it's admin
                if (userId.equals("9")) {
                    Intent intent = new Intent(ReadNewsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ReadNewsActivity.this, MainActivityForUsers.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        customAdapter = new CustomAdapter(this,newsList);
        listView.setAdapter(customAdapter);

    }
/*        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest newsReq = getRequest();

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(newsReq);

        customAdapter = new CustomAdapter(this,newsList);
        listView.setAdapter(customAdapter);
    }

    public JsonArrayRequest getRequest(){

        return new JsonArrayRequest(ConfigPhpAndroid.URL_GET_ALL_NEWS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(ConfigPhpAndroid.TAG_JSON_ARRAY, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                News news = new News();
                                news.setTitle(obj.getString(ConfigPhpAndroid.TAG_NEWS_TITLE));

                                news.setThumbNailUrl(ConfigPhpAndroid.URL_IMAGE_PREFIX+obj.getString(ConfigPhpAndroid.URL_GET_IMAGE));
                                newsList.add(news);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                       customAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(ConfigPhpAndroid.TAG_JSON_ARRAY, "Error: " + error.getMessage());
                hidePDialog();

            }
        });
    }*/

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
                String s = requestHandler.sendGetRequest(ConfigPhpAndroid.URL_GET_ALL_NEWS);
                return s;
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }



    private void showNews(){
        JSONObject jsonObj = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        customAdapter = new CustomAdapter(this,newsList);
        listView.setAdapter(customAdapter);
        try{
            jsonObj = new JSONObject(JSON_STRING);
            JSONArray result = jsonObj.getJSONArray(ConfigPhpAndroid.TAG_JSON_ARRAY);

            for(int i = 0;i<result.length();i++){
                JSONObject jsonObject = result.getJSONObject(i);
//                String title = jsonObject.getString(ConfigPhpAndroid.TAG_NEWS_TITLE);
//                String newsId = jsonObject.getString(ConfigPhpAndroid.TAG_NEWS_ID);
                News news = new News();
                news.setNewsId(jsonObject.getString(ConfigPhpAndroid.TAG_NEWS_ID));
                news.setTitle(jsonObject.getString(ConfigPhpAndroid.TAG_NEWS_TITLE));
                String newsUrl = ConfigPhpAndroid.URL_IMAGE_PREFIX+jsonObject.getString(ConfigPhpAndroid.TAG_NEWS_IMAGE);
                news.setThumbNailUrl(ConfigPhpAndroid.URL_IMAGE_PREFIX+jsonObject.getString(ConfigPhpAndroid.TAG_NEWS_IMAGE));
                newsList.add(news);

//                HashMap<String,String> news = new HashMap<>();
//                news.put(ConfigPhpAndroid.TAG_NEWS_ID,newsId);
//                news.put(ConfigPhpAndroid.TAG_NEWS_TITLE,title);
//                setImageById(newsId);
//                list.add(news);
            }
            customAdapter.notifyDataSetChanged();
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

//        listView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        HashMap<String,String> user = session.getUserDetails();
        String userId = user.get(Session.KEY_ID);
        int pos = listView.getPositionForView(view);

        News news = new News();
        news = (News) parent.getAdapter().getItem(pos);
        String newsId = news.getNewsId();
        if (userId.equals("9")){
            Intent intent = new Intent(this,ShowNewsForAdmin.class);
//            HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
//            String newsId = map.get(ConfigPhpAndroid.TAG_NEWS_ID).toString();
//            ArrayList<News> newsList = (ArrayList<News>)parent.getItemAtPosition(position);

            intent.putExtra(ConfigPhpAndroid.NEWS_ID_INTENT,newsId);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,ShowNewsForUser.class);
//            HashMap<String,String> map = (HashMap)parent.getItemAtPosition(position);
//            String newsId = map.get(ConfigPhpAndroid.TAG_NEWS_ID).toString();
            intent.putExtra(ConfigPhpAndroid.NEWS_ID_INTENT,newsId);
            startActivity(intent);
        }
    }
            private void hidePDialog() {
                if (pDialog != null) {
                    pDialog.dismiss();
                    pDialog = null;
                }
            }
}
