package com.example.bearer1024.nasaimage;

/**
 * Created by bearer1024 on 7/10/16.
 */
public interface IotdHandlerListener {

    public void iotdParsed(String url, String title, String description,
                           String date);

}

