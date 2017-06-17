package com.example.ken.newsapp;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Donk on 6/16/2017.
 */

public class NetworkUtils {
    public static final String NEWSAPI_BASE_URL = "https://newsapi.org/v1/articles";
    public static final String PARAM_SOURCE = "source";
    public static final String PARAM_SORT = "sortBy";
    public static final String PARAM_APIKEY = "apiKey";

    public static URL makeURL(String sourceQuery, String sortBy, String apiKey){
        Uri uri = Uri.parse(NEWSAPI_BASE_URL).buildUpon().appendQueryParameter(PARAM_SOURCE, sourceQuery).appendQueryParameter(PARAM_SORT, sortBy).appendQueryParameter(PARAM_APIKEY, apiKey).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else{
                return null;
            }

        }finally{
            urlConnection.disconnect();
        }
    }
}
