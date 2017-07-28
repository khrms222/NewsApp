package com.example.ken.newsapp;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {
    //Base url to fetch json of news articles
    public static final String NEWSAPI_BASE_URL = "https://newsapi.org/v1/articles";

    //Url query parameter to filter by source
    public static final String PARAM_SOURCE = "source";

    //Url query parameter to sort
    public static final String PARAM_SORT = "sortBy";

    //Url query parameter to authenticate the user with an API Key
    public static final String PARAM_APIKEY = "apiKey";

    private static String sourceQuery = "the-next-web";
    private static String sortBy = "latest";
    private static String apiKey = "4bbc5a00be8d40448ad056a1acc0d68a";


    /*
    *
    *   Makes a url by appending the base url with its parameters
    *
    * */
    public static URL makeURL(){
        Uri uri = Uri.parse(NEWSAPI_BASE_URL).buildUpon().appendQueryParameter(PARAM_SOURCE, sourceQuery).appendQueryParameter(PARAM_SORT, sortBy).appendQueryParameter(PARAM_APIKEY, apiKey).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /*
    *
    *   Http call to fetch a json from a web service.
    *
    * */
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

    /*
    *
    *   Parse the json into an ArrayList of usable java objects
    *
    * */
    public static ArrayList<NewsItem> parseJSON(String json) throws JSONException{
        ArrayList<NewsItem> newsItemsList = new ArrayList<NewsItem>();

        JSONObject mainJSON = new JSONObject(json);
        JSONArray articleList = mainJSON.getJSONArray("articles");

        for(int i = 0; i < articleList.length(); i++){
            JSONObject article = articleList.getJSONObject(i);

            String author = article.getString("author");
            String title = article.getString("title");
            String description = article.getString("description");
            String url = article.getString("url");
            String urlToImage = article.getString("urlToImage");
            String publishedAt = article.getString("publishedAt");

            NewsItem newsItem = new NewsItem(author, title, description, url, urlToImage, publishedAt);
            newsItemsList.add(newsItem);
        }

        return newsItemsList;
    }
}
