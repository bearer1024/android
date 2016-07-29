package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateNewsActivity extends AppCompatActivity {

    private EditText title,content,link;
    private Button backbtn,submitbtn;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.newsContent);
        link = (EditText)findViewById(R.id.relativeLink);
        backbtn = (Button)findViewById(R.id.backButton);
        submitbtn = (Button)findViewById(R.id.submitButton);

        submitbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String newsTitle = title.getText().toString();
                String newsContent = content.getText().toString();
                String newsLink = link.getText().toString();

                if(!newsTitle.isEmpty()&&!newsContent.isEmpty()){
                    if(!newsLink.isEmpty()){
                       createNewsFromAndroid(newsContent,newsLink,newsTitle);
                    }
                }else{
                    Snackbar.make(v,"title and content could not be null",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(CreateNewsActivity.this,
                                            MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createNewsFromAndroid(final String newsContent,final String newsLink,
                                       final String newsTitle) {
        String tag_string_req = "req_createNews";

        pDialog.setMessage("publishing news now");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL_addnews, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if(!error){

                        Intent intent = new Intent(CreateNewsActivity.
                                this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg,
                                Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String,String> getParams() {
                //Posting params to createNews url
                Map<String,String> params = new HashMap<String,String>();
                params.put("tag", "createNews");
                params.put("newsContent",newsContent);
                params.put("newsLink",newsTitle);
                params.put("newsTitle",newsTitle);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
    }

    private void showDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog(){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }
}
