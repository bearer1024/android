package com.leicesterCampus.registrationandlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private TextView tvLogin;
    private EditText fullName, email_to_register,
            password_to_register,password_to_register_to_confirm;
    private Button registerButton;
    private Session session;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new Session(RegistrationActivity.this);

        if (session.getLoggedIn()) {
            Intent intent = new Intent(RegistrationActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
        registerButton = (Button) findViewById(R.id.register_button);
        fullName = (EditText) findViewById(R.id.fullname_register);
        email_to_register = (EditText) findViewById(R.id.email_register);
        password_to_register = (EditText) findViewById(R.id.password_register);
        password_to_register_to_confirm = (EditText)findViewById(R.id.password_register_confirm);
        tvLogin = (TextView) findViewById(R.id.tv_signin);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                String email = email_to_register.getText().toString();
                String password = password_to_register.getText().toString();
                String passwordToConfirm = password_to_register_to_confirm.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if(email.contains("@")){
                        if(password.equals(passwordToConfirm)) {
                            registerUser(name, email, password);
                        }else{
                            password_to_register_to_confirm.setError("Confirm password is not match");
                        }
                    }else{
                        email_to_register.setError("Please input correct format of email address");

                    }

                } else {
                    Snackbar.make(v, "Please input the necessary information!", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
     // final is used to guarantee name,email and password cannot be altered
    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ConfigPhpAndroid.INDEX_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Intent intent = new Intent(
                                RegistrationActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
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
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Add the request to the RequestQueue.
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
