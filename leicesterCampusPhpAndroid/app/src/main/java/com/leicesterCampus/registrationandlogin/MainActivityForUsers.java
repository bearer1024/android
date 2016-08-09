package com.leicesterCampus.registrationandlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivityForUsers extends AppCompatActivity {

    private Button btnLogout,btnReadNews;

    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_for_users);


            Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolBar);

            //reading news logic
            btnReadNews = (Button)findViewById(R.id.readNewsButton);
            btnReadNews.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(MainActivityForUsers.this,
                            ReadNewsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            //logout logic
            btnLogout = (Button) findViewById(R.id.btnLogout);
            session = new Session(MainActivityForUsers.this);

            if (!session.getLoggedIn()) {
                logoutUser();
            }

            btnLogout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    logoutUser();
                }
            });
        }


        private void logoutUser() {
            session.setLogin(false);
            Intent intent = new Intent(MainActivityForUsers.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
}




