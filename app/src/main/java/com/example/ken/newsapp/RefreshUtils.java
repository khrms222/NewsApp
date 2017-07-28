package com.example.ken.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ken.newsapp.data.DBHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RefreshUtils {

    /*
    *
    *   This method updates the database
    *   Delete all existing records in the database.
    *   Fetch the new json from the API and store it into the database.
    *
    * */
    public static void updateNewsArticles(Context context){
        ArrayList<NewsItem> result = null;
        URL url = NetworkUtils.makeURL();

        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();

        try {
            DatabaseUtils.deleteAll(db);
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            result = NetworkUtils.parseJSON(json);
            DatabaseUtils.bulkInsert(db, result);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
    }
}
