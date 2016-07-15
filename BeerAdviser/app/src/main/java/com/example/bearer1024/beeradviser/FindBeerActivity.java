package com.example.bearer1024.beeradviser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class FindBeerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_beer);
    }

    public void onClickFindBeer(View view){
        //get a reference to the textView
        TextView brands =  (TextView)findViewById(R.id.brands);
        brands.setText("gootle of geer");

        //get a reference to the spinner
        Spinner color = (Spinner)findViewById(R.id.color);
        String beerType = String.valueOf(color.getSelectedItem());

        //Display the selected item
        brands.setText(beerType);
    }
}
