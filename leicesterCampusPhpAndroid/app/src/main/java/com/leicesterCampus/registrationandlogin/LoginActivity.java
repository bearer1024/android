package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Button registrationButton, loginButton;
    private EditText email_to_login, password_to_login;

    private ProgressDialog progressDialog;
    private Session session;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        session = new Session(LoginActivity.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        registrationButton = (Button) findViewById(R.id.registration_button);
        loginButton = (Button) findViewById(R.id.signin_button);
        email_to_login = (EditText) findViewById(R.id.email_to_login);
        password_to_login = (EditText) findViewById(R.id.password_to_login);


        registrationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_to_login.getText().toString();
                String password = password_to_login.getText().toString();

                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    checkLogin(email, password,v);
                } else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

    private void checkLogin(final String email, final String password, final View view) {
        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ConfigPhpAndroid.INDEX_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean FailToLogin= jsonObject.getBoolean("error");
                if(!FailToLogin) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String userId = jObj.getString("user_id");
//                    jObj.gety()
//                    String userName = jObj.getString("user_name");
//                    String userMail = jObj.getString("user_email");
//                    session.createLoginSession();
//                    String username = jObj.getString("username");

//                    if(username != null) {
//                        session.setUsername(username);
//                    }

                        if (userId != null) {
                            session.setLogin(true);
                            session.createLoginSession(userId, email);
                            //if you are admin
                            if (userId.equals("9")) {
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivityForUsers.class);
                                startActivity(intent);
                                finish();

                            }
                        } else {
                            String errorMsg = jObj.getString("error_msg");
//                        Snackbar.make(view,"email or password incorrect",Snackbar.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Failed to login
                else {
                        Snackbar.make(view,"email or password incorrect",Snackbar.LENGTH_LONG).show();
                }

                }
                catch(Exception e){
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}