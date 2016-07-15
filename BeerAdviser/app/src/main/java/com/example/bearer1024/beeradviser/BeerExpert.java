package com.example.bearer1024.beeradviser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearer1024 on 7/15/16.
 */
public class BeerExpert {
    List<String>getBrands(String color){
        List<String> brands = new ArrayList<String>();
        if(color.equals("amber")){
            brands.add("Jack Amber");
            brands.add("Red Moose");
        } else if(color.equals("light")){
            brands.add("Jail Pale Ale");
            brands.add("Gout Stout");
        } else if (color.equals("brown")){
            brands.add("whisky");
            brands.add("votga");
        } else {
            brands.add("wine");
        }
        return brands;
    }
}

