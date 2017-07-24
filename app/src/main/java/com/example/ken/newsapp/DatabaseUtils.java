package com.example.ken.newsapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ken.newsapp.data.Contract;

import java.util.ArrayList;

/**
 * Created by Donk on 7/23/2017.
 */

public class DatabaseUtils {

    public static Cursor getAll(SQLiteDatabase db){
        Cursor cursor = db.query(
                Contract.TABLE_ARTICLES.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHDATE + " DESC"
        );
        return cursor;
    }

    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> newsArticles) {

        db.beginTransaction();
        try {
            for (NewsItem article : newsArticles) {
                ContentValues cv = new ContentValues();
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE, article.getTitle());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION, article.getDescription());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_URLTOIMAGE, article.getUrlToImage());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHDATE, article.getPublishedAt());
                db.insert(Contract.TABLE_ARTICLES.TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.delete(Contract.TABLE_ARTICLES.TABLE_NAME, null, null);
    }
}
