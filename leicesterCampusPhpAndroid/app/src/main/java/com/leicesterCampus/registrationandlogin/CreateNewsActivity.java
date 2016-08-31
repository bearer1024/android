package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateNewsActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText title,content,link;
    private Button buttonBackToMenu, buttonSubmit,buttonImageChoose,
            buttonImageUpload,buttonViewImage;
    private ProgressDialog pDialog;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    private Session session;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.newsContent);
//        link = (EditText)findViewById(R.id.relativeLink);
        buttonBackToMenu = (Button)findViewById(R.id.backButton);
        buttonSubmit = (Button)findViewById(R.id.submitButton);
        buttonImageChoose = (Button)findViewById(R.id.chooseFile);
        imageView = (ImageView)findViewById(R.id.imageView);
        link = (EditText)findViewById(R.id.relativeLink);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        buttonViewImage.setOnClickListener(this);
        buttonImageChoose.setOnClickListener(this);
        buttonImageUpload.setOnClickListener(this);
        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String newsTitle = title.getText().toString();
                String newsContent = content.getText().toString();

//                String newsLink = link.getText().toString();


                if(!newsTitle.isEmpty()&&!newsContent.isEmpty()){
//                    if(!newsLink.isEmpty()){
//                       createNewsFromAndroid(newsContent,newsLink,newsTitle);
//                    }
//                    createNewsFromAndroid(newsContent,newsTitle);
                    Date pubDate = getCurrentTime();
                    String pubDateToString = pubDate.toString();
                    creadNewsFromAndroidWithImage(newsContent,newsTitle,bitmap,pubDateToString);
                }else{
                    Snackbar.make(v,"title and content could not be null",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

        buttonBackToMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(CreateNewsActivity.this,
                                            MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void creadNewsFromAndroidWithImage(final String newsContent,
                                               final String newsTitle,
                                               final Bitmap bitmap,
                                               final String pubdate ){
        String tag_string_req = "req_createNews";
        session = new Session(CreateNewsActivity.this);
//        final String username = session.getUsername();
        pDialog.setMessage("publishing news now");
        final String uploadImage = getStringImage(bitmap);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ConfigPhpAndroid.INDEX_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    link.setText(response.toString());
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
//                hideDialog();
            }
        }){

            @Override
            protected Map<String,String> getParams() {
                //Posting params to createNews url
                Map<String,String> params = new HashMap<String,String>();
                params.put("tag", "createNews");
                params.put("newsContent",newsContent);
//                params.put("newsLink",newsLink);
                params.put("newsTitle",newsTitle);
                params.put("image",uploadImage);
                params.put("pubDate",pubdate);
//                params.put("userName",username);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
    }


    private void createNewsFromAndroid(final String newsContent,
                                       final String newsTitle) {
        String tag_string_req = "req_createNews";
        session = new Session(CreateNewsActivity.this);
//        final String username = session.getUsername();
        pDialog.setMessage("publishing news now");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
               ConfigPhpAndroid.INDEX_URL, new Response.Listener<String>() {
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
//                params.put("newsLink",newsLink);
                params.put("newsTitle",newsTitle);
//                params.put("userName",username);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
    }

    @Override
    public void onClick(View v){
        if(v == buttonImageChoose){
            showFileChooser();
        }
        if(v == buttonImageUpload){
//            uploadImage();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode ==
                RESULT_OK && data != null && data.getData()!=null){
            filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }


    private void showDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog(){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

    private Date getCurrentTime(){
        Date date = Calendar.getInstance().getTime();
        return date;
    }
}
