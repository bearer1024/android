package com.example.bearer1024.beeradviser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class FindBeerActivity extends AppCompatActivity {

    private BeerExpert expert = new BeerExpert();
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

        //get recommendation from BeerExpert class
        List<String> brandList = expert.getBrands(beerType);
        StringBuilder brandsFormatted = new StringBuilder();
        for(String brand : brandList){
            brandsFormatted.append(brand).append("\n");//display each brand on a new line
        }
        //Display the selected item
        brands.setText(brandsFormatted);
    }
}
